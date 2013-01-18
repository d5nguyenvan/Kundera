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
package com.impetus.client.neo4j.imdb.datatype;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case  
 * @author amresh.singh
 */
public class IMDBAllDataTypeTest
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
        //findById();
        //merge();
       // delete();
        
    }
    
    private void insert()
    {
        Calendar cal = Calendar.getInstance();
        ActorAllDataType actor1 = new ActorAllDataType(1, "Tom Cruise", 23456789l, true, 'C', (byte) 8, (short) 5, (float) 10.0,
                163.12, new Date(
                Long.parseLong("1351667541111")), new Date(Long.parseLong("1351667542222")), new Date(
                        Long.parseLong("1351667543333")), 2, new Long(3634521523423L), new Double(7.23452342343), 
                        new BigInteger("123456789"), new BigDecimal(123456789), cal);
        
        ActorAllDataType actor2 = new ActorAllDataType(2, "Emmanuelle Béart", 23456790l, false, 'D', (byte) 9, (short) 6, (float) 11.3,
                161.99, new Date(
                Long.parseLong("1351667544444")), new Date(Long.parseLong("1351667545555")), new Date(
                        Long.parseLong("1351667546666")), 2, new Long(3634521523453L), new Double(8.23452342343), 
                        new BigInteger("123456790"), new BigDecimal(123456790), cal);
        

        
    }
    
   
   
    
    

}
