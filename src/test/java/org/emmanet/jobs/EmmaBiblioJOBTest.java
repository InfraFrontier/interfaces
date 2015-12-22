/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.jobs;

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

import java.util.List;
import org.emmanet.model.BibliosDAO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author mrelac
 */
public class EmmaBiblioJOBTest {
    
    public EmmaBiblioJOBTest() {
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

//    /**
//     * Test of main method, of class EmmaBiblioJOB.
//     */
//    @Test
//    public void testMain() {
//        System.out.println("main");
//        String[] args = null;
//        EmmaBiblioJOB.main(args);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }

    /**
     * Test of fetchPaper method with invalid pubmed_id.
     */
    @Ignore
    @Test
    public void testFetchPaperInvalidPubmed_id() {
        System.out.println("fetchPaper invalid pubmed_id");
        int pubmed_id = 0;
        EmmaBiblioJOB instance = new EmmaBiblioJOB();
        EmmaBiblioJOB.FetchBiblio result = instance.fetchPaper(pubmed_id);
        assertEquals(result.status, EmmaBiblioJOB.NOT_FOUND);
        assertEquals(result.errorMessage, "");
    }

    /**
     * Test of fetchPaper method with valid pubmed_id.
     */
    @Test
    public void testFetchPaperValidPubmed_id() {
        System.out.println("fetchPaper valid pubmed_id");
        int pubmed_id = 1901381;
        EmmaBiblioJOB instance = new EmmaBiblioJOB();
        EmmaBiblioJOB.FetchBiblio result = instance.fetchPaper(pubmed_id);
        assertEquals(result.status, EmmaBiblioJOB.OK);
        assertEquals(result.errorMessage, "");
    }

//    /**
//     * Test of getDbInfo method, of class EmmaBiblioJOB.
//     */
//    @Test
//    public void testGetDbInfo() {
//        System.out.println("getDbInfo");
//        EmmaBiblioJOB instance = new EmmaBiblioJOB();
//        String expResult = "";
//        String result = instance.getDbInfo();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of save method, of class EmmaBiblioJOB.
//     */
//    @Test
//    public void testSave() {
//        System.out.println("save");
//        List<BibliosDAO> biblioList = null;
//        EmmaBiblioJOB instance = new EmmaBiblioJOB();
//        int expResult = 0;
//        int result = instance.save(biblioList);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of validateAll method, of class EmmaBiblioJOB.
//     */
//    @Test
//    public void testValidateAll() {
//        System.out.println("validateAll");
//        EmmaBiblioJOB instance = new EmmaBiblioJOB();
//        int expResult = 0;
//        int result = instance.validateAll();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of validateAndUpdateCandidates method, of class EmmaBiblioJOB.
//     */
//    @Test
//    public void testValidateAndUpdateCandidates() {
//        System.out.println("validateAndUpdateCandidates");
//        EmmaBiblioJOB instance = new EmmaBiblioJOB();
//        int expResult = 0;
//        int result = instance.validateAndUpdateCandidates();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//
//    /**
//     * Test of validateUpdateCandidates method, of class EmmaBiblioJOB.
//     */
//    @Test
//    public void testValidateUpdateCandidates() {
//        System.out.println("validateUpdateCandidates");
//        EmmaBiblioJOB instance = new EmmaBiblioJOB();
//        int expResult = 0;
//        int result = instance.validateUpdateCandidates();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}