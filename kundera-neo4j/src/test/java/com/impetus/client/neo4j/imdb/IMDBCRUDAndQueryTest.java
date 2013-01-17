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
package com.impetus.client.neo4j.imdb;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case using IMDB example for CRUD and query 
 * Demonstrates M-2-M Association between two entitites using Map
 * @author amresh.singh
 */
public class IMDBCRUDAndQueryTest
{
    EntityManagerFactory emf;
    EntityManager em;   
    


    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception
    {
        emf = Persistence.createEntityManagerFactory("imdb");
        em = emf.createEntityManager();        
        
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception
    {
        em.close();
        emf.close();
    }  
    
    @Test
    public void testCRUD()
    {
        insert();
        findById();
        merge();
        delete();
        
    }
    
    private void merge()
    {
        Actor actor1 = em.find(Actor.class, 1);
        Actor actor2 = em.find(Actor.class, 2);
        
        assertActors(actor1, actor2);
        
        actor1.setName("Amresh");
        actor2.setName("Amir");
        
        em.merge(actor1);
        em.merge(actor2);
        
        em.clear();
        
        Actor actor1AfterMerge = em.find(Actor.class, 1);
        Actor actor2AfterMerge = em.find(Actor.class, 2);
        
        assertUpdatedActors(actor1AfterMerge, actor2AfterMerge);
        
    }
    
    private void delete()
    {
        Actor actor1 = em.find(Actor.class, 1);
        Actor actor2 = em.find(Actor.class, 2);
        assertUpdatedActors(actor1, actor2);
        
        em.remove(actor1);
        em.remove(actor2);
        
        em.clear(); //clear cache
        Actor actor1AfterDeletion = em.find(Actor.class, 1);
        Actor actor2AfterDeletion = em.find(Actor.class, 2);
        
        Assert.assertNull(actor1AfterDeletion);
        Assert.assertNull(actor2AfterDeletion);
    }
    
    private void findById()
    {
        //Find actor by ID
        Actor actor1 = em.find(Actor.class, 1);
        Actor actor2 = em.find(Actor.class, 2);
        
        assertActors(actor1, actor2);  
        
    }

    /**
     * @param actor1
     * @param actor2
     */
    private void assertActors(Actor actor1, Actor actor2)
    {
        Assert.assertNotNull(actor1);
        Assert.assertEquals(1, actor1.getId());
        Assert.assertEquals("Tom Cruise", actor1.getName());
        
        Assert.assertNotNull(actor2);
        Assert.assertEquals(2, actor2.getId());
        Assert.assertEquals("Emmanuelle Béart", actor2.getName());
    }
    
    /**
     * @param actor1
     * @param actor2
     */
    private void assertUpdatedActors(Actor actor1, Actor actor2)
    {
        Assert.assertNotNull(actor1);
        Assert.assertEquals(1, actor1.getId());
        Assert.assertEquals("Amresh", actor1.getName());
        
        Assert.assertNotNull(actor2);
        Assert.assertEquals(2, actor2.getId());
        Assert.assertEquals("Amir", actor2.getName());
    }
    
    private void insert()
    {
        //Actors
        Actor actor1 = new Actor(1, "Tom Cruise");
        Actor actor2 = new Actor(2, "Emmanuelle Béart");     
        
        //Movies
        Movie movie1 = new Movie("m1", "War of the Worlds", 2005);
        Movie movie2 = new Movie("m2", "Mission Impossible", 1996);       
        Movie movie3 = new Movie("m3", "Hell", 2005);
        
        //Roles
        Role role1 = new Role("Ray Ferrier", "Lead Actor"); role1.setActor(actor1); role1.setMovie(movie1);
        Role role2 = new Role("Ethan Hunt", "Lead Actor"); role2.setActor(actor1); role2.setMovie(movie2);
        Role role3 = new Role("Claire Phelps", "Lead Actress"); role3.setActor(actor2); role1.setMovie(movie2);
        Role role4 = new Role("Sophie", "Supporting Actress"); role4.setActor(actor2); role1.setMovie(movie3);
        
        //Relationships
        actor1.addMovie(role1, movie1); actor1.addMovie(role2, movie2);
        actor2.addMovie(role3, movie2); actor2.addMovie(role4, movie3);
        
        movie1.addActor(role1, actor1);
        movie2.addActor(role2, actor1); movie2.addActor(role3, actor2);
        movie3.addActor(role4, actor2);
        
        em.persist(actor1);
        em.persist(actor2); 
       
    }

}
