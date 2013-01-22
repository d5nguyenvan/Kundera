/**
 * Copyright 2012 Impetus Infotech.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.impetus.client.neo4j;

import com.impetus.kundera.persistence.TransactionResource;

/**
 * Defines transaction boundaries for Neo4J client, in case  
 * user opts for transaction support (kundera.transaction.resource)
 * @author amresh.singh
 */
public class Neo4JTransaction implements TransactionResource
{

    @Override
    public void onBegin()
    {
    }

    @Override
    public void onCommit()
    {
    }

    @Override
    public void onRollback()
    {
    }

    @Override
    public void onFlush()
    {
    }

    @Override
    public Response prepare()
    {
        return null;
    }

    @Override
    public boolean isActive()
    {
        return false;
    }

}
