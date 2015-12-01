/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.healthcheck;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.emmanet.model.BibliosDAO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mrelac
 */
public class EmmaBiblioValidatorTest {
    
    public EmmaBiblioValidatorTest() {
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

    /**
     * Test of hasErrors method, of class EmmaBiblioValidator.
     */
    @Test
    public void testHasErrors() {
        System.out.println("testHasErrors");
        EmmaBiblioValidator instance = new EmmaBiblioValidator();
        boolean expResult = false;
        boolean result = instance.hasErrors();
        assertEquals(expResult, result);
    }
    
    /**
     * Test validation: null and empty BibliosDAO list.
     */
    @Test
    public void testNullList() {
        System.out.println("testNullList");
        EmmaBiblioValidator instance = new EmmaBiblioValidator();
        List<BibliosDAO> inputList = null;
        Map<Integer, BibliosDAO> actuals;
        actuals = instance.validate(inputList);
        assert(actuals.isEmpty());
    }
    
    /**
     * Test validation: null and empty BibliosDAO list.
     */
    @Test
    public void testEmptyList() {
        System.out.println("testEmptyList");
        EmmaBiblioValidator instance = new EmmaBiblioValidator();
        List<BibliosDAO> inputList = new ArrayList<>();
        Map<Integer, BibliosDAO> actuals;
        actuals = instance.validate(inputList);
        assert(actuals.isEmpty());
    }
    
    /**
     * Test validation: valid empty DAO.
     */
    @Test
    public void testValidEmptyDAO() {
        System.out.println("testValidEmptyDAO");
        EmmaBiblioValidator instance = new EmmaBiblioValidator();
        List<BibliosDAO> inputList = new ArrayList<>();
        inputList.add(new BibliosDAO());
        Map<Integer, BibliosDAO> actuals;
        actuals = instance.validate(inputList);
        assert(actuals.isEmpty());
    }
    
    /**
     * Test validation: invalid year.
     */
    @Test
    public void testInvalidYear() {
        System.out.println("testInvalidYear");
        EmmaBiblioValidator instance = new EmmaBiblioValidator();
        List<BibliosDAO> inputList = new ArrayList<>();
        BibliosDAO dao = new BibliosDAO();
        inputList.add(dao);                     // Add an empty dao to the list just to make the list longer than 1 element.
        dao = new BibliosDAO();
        dao.setId_biblio(999);
        dao.setYear("invalid number");
        inputList.add(dao);
        Map<Integer, BibliosDAO> actuals;
        actuals = instance.validate(inputList);
        assert(actuals.size() == 1);
        BibliosDAO resultDAO = actuals.get(999);
        assertEquals(dao, resultDAO);
    }
    
    /**
     * Test validation: invalid year.
     */
    @Test
    public void testInvalidPubmed_id() {
        System.out.println("testInvalidPubmed_id");
        EmmaBiblioValidator instance = new EmmaBiblioValidator();
        List<BibliosDAO> inputList = new ArrayList<>();
        BibliosDAO dao = new BibliosDAO();
        inputList.add(dao);                     // Add an empty dao to the list just to make the list longer than 1 element.
        dao = new BibliosDAO();
        dao.setId_biblio(888);
        dao.setPubmed_id("invalid number");
        inputList.add(dao);
        Map<Integer, BibliosDAO> actuals;
        actuals = instance.validate(inputList);
        assert(actuals.size() == 1);
        BibliosDAO resultDAO = actuals.get(888);
        assertEquals(dao, resultDAO);
    }
    
    /**
     * Test validation: invalid year.
     */
    @Test
    public void testPubmed_idWithWhitespace() {
        System.out.println("testPubmed_idWithWhitespace");
        EmmaBiblioValidator instance = new EmmaBiblioValidator();
        List<BibliosDAO> inputList = new ArrayList<>();
        BibliosDAO dao = new BibliosDAO();
        inputList.add(dao);                     // Add an empty dao to the list just to make the list longer than 1 element.
        dao = new BibliosDAO();
        dao.setId_biblio(777);
        dao.setPubmed_id("   12345  ");              // NOTE: Leading or trailing whitespace fails the int conversion.
        inputList.add(dao);
        Map<Integer, BibliosDAO> actuals;
        actuals = instance.validate(inputList);
        assert(actuals.isEmpty());
    }

}