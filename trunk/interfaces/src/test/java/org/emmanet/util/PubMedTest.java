/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 /**
 * Copyright © 2013 EMBL - European Bioinformatics Institute
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); 
 * you may not use this file except in compliance with the License.  
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.emmanet.util;

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

import ebi.ws.client.Authors;
import java.util.List;
import org.emmanet.model.BibliosDAO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author mrelac
 */
public class PubMedTest {
    private static ApplicationContext ac;
    private static JdbcTemplate jdbcTemplate;
    public PubMedTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ac = new ClassPathXmlApplicationContext("/jobApplicationContext.xml");  // Get job application context.
        jdbcTemplate = (JdbcTemplate)ac.getBean("jdbcTemplate");                // Get JdbcTemplate.
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
     * Test of getPubmedId method, of class PubMed.
     */
    @Test
    public void testGetPubmedIdDefault() {
        System.out.println("getPubmedIdDefault");
        PubMed instance = new PubMed();
        String expResult = "";
        String result = instance.getPubmedId();
        assertEquals(expResult, result);
    }

    /**
     * Test of setPubmedId method, of class PubMed.
     */
    @Test
    public void testSetPubmedId1() {
        System.out.println("setPubmedId");
        String pubmedId = "96264";
        PubMed instance = new PubMed();
        instance.setPubmedId(96264);
        assertEquals(true, instance.isValid());
        assertEquals(pubmedId, instance.getPubmedId());
        assertEquals("Blum B", instance.getAuthor1());
        assertEquals("", instance.getOtherAuthors());
        Authors author1 = new Authors();
        author1.setFullName("Blum B");
        Authors[] authors = new Authors[] { author1};
        assertEquals("Blum B", authors[0].getFullName());
        assertEquals("Blum B", instance.getAuthorsString());
        assertEquals("", instance.getOtherAuthors());
        assertEquals("1", instance.getIssue());
        assertEquals("Journal of medical primatology", instance.getJournal());
        assertEquals("44-52", instance.getPages());
        assertEquals("", instance.getPaperid());
        assertEquals(pubmedId, instance.getPubmedId());
        assertEquals("The pyramidal system of the cat and of the monkey: neurophysiologic aspects of phylogenetic advancement.", instance.getTitle());
        assertEquals("7", instance.getVolume());
        assertEquals(1978, instance.getYear());
    }
    
    @Test
    public void testSetPubmedId2() {
        System.out.println("setPubmedId");
        String pubmedId = "24248598";
        PubMed instance = new PubMed();
        instance.setPubmedId(24248598);
        assertEquals(true, instance.isValid());
        assertEquals(pubmedId, instance.getPubmedId());
        assertEquals("Wienerroither S", instance.getAuthor1());
        assertEquals("Rauch I, Rosebrock F, Jamieson AM, Bradner J, Muhar M, Zuber J, Müller M, Decker T", instance.getOtherAuthors());
        List<Authors> authorsList = instance.getAuthors();
        assertEquals(9, authorsList.size());
        assertEquals("Wienerroither S", authorsList.get(0).getFullName());
        assertEquals("Rauch I", authorsList.get(1).getFullName());
        assertEquals("Rosebrock F", authorsList.get(2).getFullName());
        assertEquals("Jamieson AM", authorsList.get(3).getFullName());
        assertEquals("Bradner J", authorsList.get(4).getFullName());
        assertEquals("Muhar M", authorsList.get(5).getFullName());
        assertEquals("Zuber J", authorsList.get(6).getFullName());
        assertEquals("Müller M", authorsList.get(7).getFullName());
        assertEquals("Decker T", authorsList.get(8).getFullName());
        assertEquals("Wienerroither S, Rauch I, Rosebrock F, Jamieson AM, Bradner J, Muhar M, Zuber J, Müller M, Decker T", instance.getAuthorsString());
        assertEquals("Rauch I, Rosebrock F, Jamieson AM, Bradner J, Muhar M, Zuber J, Müller M, Decker T", instance.getOtherAuthors());
        assertEquals("3", instance.getIssue());
        assertEquals("Molecular and cellular biology", instance.getJournal());
        assertEquals("415-427", instance.getPages());
        assertEquals("", instance.getPaperid());
        assertEquals("24248598", instance.getPubmedId());
        //assertEquals("Regulation of NO synthesis, local inflammation, and innate immunity to pathogens by BET family proteins.", instance.getTitle());
        assertEquals("34", instance.getVolume());
        assertEquals(2014, instance.getYear());
    }

    /**
     * Test of isValid method, of class PubmedID.
     */
    @Test
    public void testIsValidLong() {
        System.out.println("isValid");
        PubMed instance = new PubMed(24248598);
        boolean expResult = true;
        boolean result = instance.isValid();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testIsValidString() {
        System.out.println("isValidString");
        PubMed instance = new PubMed("24248598");
        boolean expResult = true;
        boolean result = instance.isValid();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testIsValidNull() {
        System.out.println("isValidNull");
        PubMed instance = new PubMed(null);
        boolean expResult = false;
        boolean result = instance.isValid();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testIsValidNegative() {
        System.out.println("isValidNegative");
        PubMed instance = new PubMed(-1);
        boolean expResult = false;
        boolean result = instance.isValid();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testIsValidEmpty() {
        System.out.println("isValidEmpty");
        PubMed instance = new PubMed("");
        boolean expResult = false;
        boolean result = instance.isValid();
        assertEquals(expResult, result);
    }
    
    @Test
    public void testIsValidAlpha() {
        System.out.println("isValidAlpha");
        PubMed instance = new PubMed("abc");
        boolean expResult = false;
        boolean result = instance.isValid();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class PubmedID.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        PubMed instance = new PubMed(24248598);
        String expResult = "24248598";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
    /**
     * See jira bug #EMMA-525 for the motivation for this test.
     */
    @Test
    public void testIsValidWebService() {
        System.out.println("isValidWebService");
        String pubmedId = "24248598";
        PubMed instance = new PubMed(24248598);
        assertEquals(true, instance.isValid());
        assertEquals(pubmedId, instance.getPubmedId());
        //assertEquals("Regulation of NO Synthesis, Local Inflammation, and Innate Immunity to Pathogens by BET Family Proteins.", instance.getTitle());
        assertEquals("Wienerroither S, Rauch I, Rosebrock F, Jamieson AM, Bradner J, Muhar M, Zuber J, Müller M, Decker T", instance.getAuthorsString());
    }
    
    /** This takes a long time to run. It validates all of the biblios table records, printing warnings (but continuing to run)
      for all biblios records.
      */
    @Ignore
    @Test
    public void testAllPubmed() {
        System.out.println("allPubmed");
        List<BibliosDAO> biblioDAOList;
        final String statement = 
          "SELECT * FROM biblios\n"
        + "WHERE pubmed_id IS NOT NULL\n";
        
        biblioDAOList = jdbcTemplate.query(statement, new BibliosDAO());
        int i = 0;
        for (BibliosDAO biblioDAO : biblioDAOList) {
            String pubmedId = biblioDAO.getPubmed_id();
            PubMed pubmed = new PubMed(pubmedId);
            if (pubmed.isValid())
                assertEquals("pubmed id " + pubmedId + " [" + i + " of " + biblioDAOList.size() + "]: ", pubmedId, pubmed.getPubmedId());
            else
                System.out.println("Warning: pubmed id '" + pubmedId + "' [" + i + " of " + biblioDAOList.size() + "] is not valid.");
            i++;
        }
        
        System.out.println(i + "valid pubmed id records.");
    }
    
    
    
}
