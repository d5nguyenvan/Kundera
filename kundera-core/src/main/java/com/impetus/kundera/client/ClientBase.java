/*******************************************************************************
 * * Copyright 2012 Impetus Infotech.
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *      http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 ******************************************************************************/
package com.impetus.kundera.client;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.impetus.kundera.db.RelationHolder;
import com.impetus.kundera.graph.Node;
import com.impetus.kundera.graph.NodeLink;
import com.impetus.kundera.graph.NodeLink.LinkProperty;
import com.impetus.kundera.index.IndexManager;
import com.impetus.kundera.metadata.KunderaMetadataManager;
import com.impetus.kundera.metadata.MetadataUtils;
import com.impetus.kundera.metadata.model.EntityMetadata;
import com.impetus.kundera.metadata.model.Relation;
import com.impetus.kundera.metadata.model.Relation.ForeignKey;

/**
 * Base class for all Client implementations providing common utility methods to
 * them all.
 * 
 * @author amresh
 * 
 */
public abstract class ClientBase
{

    /** The index manager. */
    protected IndexManager indexManager;

    /** persistence unit */
    protected String persistenceUnit;

    protected boolean isUpdate;

    /*
     * (non-Javadoc)
     * 
     * @see com.impetus.kundera.client.Client#getIndexManager()
     */
    public final IndexManager getIndexManager()
    {
        return indexManager;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.impetus.kundera.client.Client#getPersistenceUnit()
     */
    public String getPersistenceUnit()
    {
        return persistenceUnit;
    }

    /**
     * Method to handle
     * 
     * @param node
     */
    public void persist(Node node)
    {
        Object entity = node.getData();
        Object id = node.getEntityId();
        EntityMetadata metadata = KunderaMetadataManager.getEntityMetadata(node.getDataClass());
        isUpdate = node.isUpdate();
        List<RelationHolder> relationHolders = getRelationHolders(node);
        onPersist(metadata, entity, id, relationHolders);
        indexNode(node, metadata);
    }

    /**
     * @param node
     * @return
     */
    protected List<RelationHolder> getRelationHolders(Node node)
    {

        List<RelationHolder> relationsHolder = new ArrayList<RelationHolder>();

        // Add column value for all parent nodes linked to this node
        Map<NodeLink, Node> parents = node.getParents();
        Map<NodeLink, Node> children = node.getChildren();

        if (parents != null && !parents.isEmpty())
        {

            for (NodeLink parentNodeLink : parents.keySet())
            {
                String linkName = (String) parentNodeLink.getLinkProperty(LinkProperty.LINK_NAME);
                Object linkValue = parentNodeLink.getLinkProperty(LinkProperty.LINK_VALUE);
                boolean isSharedByPrimaryKey = (Boolean) parentNodeLink
                        .getLinkProperty(LinkProperty.IS_SHARED_BY_PRIMARY_KEY);
                Relation.ForeignKey multiplicity = parentNodeLink.getMultiplicity();

                if (linkName != null && linkValue != null && !isSharedByPrimaryKey
                        && multiplicity.equals(ForeignKey.ONE_TO_MANY))
                {
                    RelationHolder relationHolder = new RelationHolder(linkName, linkValue);
                    relationsHolder.add(relationHolder);
                }
            }
        }

        // Add column value for all child nodes linked to this node
        if (children != null && !children.isEmpty())
        {
            for (NodeLink childNodeLink : children.keySet())
            {
                String linkName = (String) childNodeLink.getLinkProperty(LinkProperty.LINK_NAME);
                Object linkValue = childNodeLink.getLinkProperty(LinkProperty.LINK_VALUE);
                boolean isSharedByPrimaryKey = (Boolean) childNodeLink
                        .getLinkProperty(LinkProperty.IS_SHARED_BY_PRIMARY_KEY);
                Relation.ForeignKey multiplicity = childNodeLink.getMultiplicity();

                if (linkName != null && linkValue != null && !isSharedByPrimaryKey)
                {
                    if(multiplicity.equals(ForeignKey.ONE_TO_ONE) || multiplicity.equals(ForeignKey.MANY_TO_ONE))
                    {
                        RelationHolder relationHolder = new RelationHolder(linkName, linkValue);
                        relationsHolder.add(relationHolder);
                    }
                    else if(multiplicity.equals(ForeignKey.MANY_TO_MANY) 
                            && ((Field)childNodeLink.getLinkProperty(LinkProperty.PROPERTY)).getType().isAssignableFrom(Map.class))
                    {                        
                        Object relationTo = ((Node)children.get(childNodeLink)).getData();
                        RelationHolder relationHolder = new RelationHolder(linkName, relationTo,linkValue);
                        relationsHolder.add(relationHolder);
                    }                
                    
                }
            }
        }
        return relationsHolder;
    }

    /**
     * @param node
     * @param entityMetadata
     */
    protected void indexNode(Node node, EntityMetadata entityMetadata)
    {
        
        if (indexManager != null)
        {
            if (!MetadataUtils.useSecondryIndex(getPersistenceUnit()))
            {
                Map<NodeLink, Node> parents = node.getParents();
                if (parents != null)
                {
                    for (NodeLink parentNodeLink : parents.keySet())
                    {
                        indexManager.update(entityMetadata, node.getData(), parentNodeLink
                                .getLinkProperty(LinkProperty.LINK_VALUE), parents.get(parentNodeLink).getDataClass());
                    }

                }
                else if (node.getChildren() != null)
                {

                    Map<NodeLink, Node> children = node.getChildren();
                    for (NodeLink childNodeLink : children.keySet())
                    {
                        if (childNodeLink.getMultiplicity().equals(ForeignKey.MANY_TO_ONE))
                        {
                            indexManager.update(entityMetadata, node.getData(), childNodeLink
                                    .getLinkProperty(LinkProperty.LINK_VALUE), children.get(childNodeLink)
                                    .getDataClass());
                        }
                        else
                        {
                            indexManager.update(entityMetadata, node.getData(), null, null);
                        }
                    }
                }
                else
                {
                    indexManager.update(entityMetadata, node.getData(), null, null);
                }
            }
        }
    }

    /**
     * Method to be implemented by inherited classes. On receiving persist event
     * specific client need to implement this method.
     * 
     * @param entityMetadata
     *            entity metadata.
     * @param entity
     *            entity object.
     * @param id
     *            entity id.
     * @param rlHolders
     *            relation holders. This field is only required in case Entity
     *            is holding up any associations with other entities.
     */
    protected abstract void onPersist(EntityMetadata entityMetadata, Object entity, Object id,
            List<RelationHolder> rlHolders);
}
