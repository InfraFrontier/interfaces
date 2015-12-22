/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.model;

/*
 * #%L
 * InfraFrontier
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 EMBL-European Bioinformatics Institute
 * %%
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
 * #L%
 */

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:jobApplicationContext.xml" })
@TransactionConfiguration
@Transactional
*/

/**
 *
 * @author mrelac
 */
public class BibliosManagerTest {
    /**
    @Autowired
    BibliosManager instance;
    */
    public BibliosManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

////////    /**
////////     * Test of getBibStrainsByID method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testGetBibStrainsByID() {
////////        System.out.println("getBibStrainsByID");
////////        int id = 0;
////////        BibliosManager instance = new BibliosManager();
////////        BibliosStrainsDAO expResult = null;
////////        BibliosStrainsDAO result = instance.getBibStrainsByID(id);
////////        assertEquals(expResult, result);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
////////
////////    /**
////////     * Test of getBibByID method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testGetBibByID() {
////////        System.out.println("getBibByID");
////////        int id = 0;
////////        BibliosManager instance = new BibliosManager();
////////        BibliosDAO expResult = null;
////////        BibliosDAO result = instance.getBibByID(id);
////////        assertEquals(expResult, result);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
////////
////////    /**
////////     * Test of getPubmedIDByID method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testGetPubmedIDByID() {
////////        System.out.println("getPubmedIDByID");
////////        int pmid = 0;
////////        BibliosManager instance = new BibliosManager();
////////        BibliosDAO expResult = null;
////////        BibliosDAO result = instance.getPubmedIDByID(pmid);
////////        assertEquals(expResult, result);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
////////
////////    /**
////////     * Test of getBibliosByPubmed_id method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testGetBibliosByPubmed_id() {
////////        System.out.println("getBibliosByPubmed_id");
////////        String pubmed_id = "";
////////        BibliosManager instance = new BibliosManager();
////////        List expResult = null;
////////        List result = instance.getBibliosByPubmed_id(pubmed_id);
////////        assertEquals(expResult, result);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
////////
////////    /**
////////     * Test of getUpdateCandidateBiblios method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testGetUpdateCandidateBiblios() {
////////        System.out.println("getUpdateCandidateBiblios");
////////        BibliosManager instance = new BibliosManager();
////////        List expResult = null;
////////        List result = instance.getUpdateCandidateBiblios();
////////        assertEquals(expResult, result);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
////////
////////    /**
////////     * Test of biblios method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testBiblios() {
////////        System.out.println("biblios");
////////        int id = 0;
////////        BibliosManager instance = new BibliosManager();
////////        List expResult = null;
////////        List result = instance.biblios(id);
////////        assertEquals(expResult, result);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
////////
////////    /**
////////     * Test of submissionBiblios method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testSubmissionBiblios() {
////////        System.out.println("submissionBiblios");
////////        int id = 0;
////////        BibliosManager instance = new BibliosManager();
////////        List expResult = null;
////////        List result = instance.submissionBiblios(id);
////////        assertEquals(expResult, result);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
////////
////////    /**
////////     * Test of bibliosStrains method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testBibliosStrains() {
////////        System.out.println("bibliosStrains");
////////        int id = 0;
////////        BibliosManager instance = new BibliosManager();
////////        List expResult = null;
////////        List result = instance.bibliosStrains(id);
////////        assertEquals(expResult, result);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
////////
////////    /**
////////     * Test of bibliosStrainCount method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testBibliosStrainCount() {
////////        System.out.println("bibliosStrainCount");
////////        Integer id = null;
////////        BibliosManager instance = new BibliosManager();
////////        BigInteger expResult = null;
////////        BigInteger result = instance.bibliosStrainCount(id);
////////        assertEquals(expResult, result);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
////////
////////    /**
////////     * Test of getSubBiblioBySubBiblioID method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testGetSubBiblioBySubBiblioID() {
////////        System.out.println("getSubBiblioBySubBiblioID");
////////        int subBibID = 0;
////////        BibliosManager instance = new BibliosManager();
////////        SubmissionBibliosDAO expResult = null;
////////        SubmissionBibliosDAO result = instance.getSubBiblioBySubBiblioID(subBibID);
////////        assertEquals(expResult, result);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
////////
////////    /**
////////     * Test of save method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testSave_BibliosDAO() {
////////        System.out.println("save");
////////        BibliosDAO bDAO = null;
////////        BibliosManager instance = new BibliosManager();
////////        instance.save(bDAO);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
////////
////////    /**
////////     * Test of save method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testSave_SubmissionBibliosDAO() {
////////        System.out.println("save");
////////        SubmissionBibliosDAO sbDAO = null;
////////        BibliosManager instance = new BibliosManager();
////////        instance.save(sbDAO);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
////////
////////    /**
////////     * Test of delete method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testDelete() {
////////        System.out.println("delete");
////////        int ID = 0;
////////        BibliosManager instance = new BibliosManager();
////////        instance.delete(ID);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
////////
////////    /**
////////     * Test of save method, of class BibliosManager.
////////     */
////////    @Test
////////    public void testSave_BibliosStrainsDAO() {
////////        System.out.println("save");
////////        BibliosStrainsDAO bsDAO = null;
////////        BibliosManager instance = new BibliosManager();
////////        instance.save(bsDAO);
////////        // TODO review the generated test code and remove the default call to fail.
////////        fail("The test case is a prototype.");
////////    }
    
    /**
     * Do nothing.
     */
    @Test
    public void testDoNothing() {
        System.out.println("doNothing");

        assertEquals(true, true);
    }
    
    
    /**
     * Test of exists method, of class BibliosManager.
     */
//    @Test
//    public void testExists() {
//        System.out.println("exists");
////        fail ("Please make sure pubmed_id 1901382 exists in the database first. Then comment out this fail call.");
//        PubmedID pubmed_id = new PubmedID(1901382);
////        BibliosManager instance = new BibliosManager();
//        boolean retVal = instance.exists(pubmed_id);
//        assertEquals(retVal, true);
//    }
    
//    /**
//     * Test of exists method, of class BibliosManager.
//     */
//    @Test
//    public void testDoesntExist() {
//        System.out.println("doesn't exist");
//        PubmedID pubmed_id = new PubmedID(-1);
//        BibliosManager instance = new BibliosManager();
//        boolean retVal = instance.exists(pubmed_id);
//        assertEquals(retVal, false);
//    }
}