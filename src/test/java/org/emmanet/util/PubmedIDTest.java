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

import ebi.ws.client.ResponseWrapper;
import ebi.ws.client.Result;
import ebi.ws.client.WSCitationImpl;
import ebi.ws.client.WSCitationImplService;
import org.junit.BeforeClass;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import javax.sql.DataSource;
import org.emmanet.model.BibliosDAO;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

/**
 *
 * @author mrelac
 */


public class PubmedIDTest {
    private static ApplicationContext ac;
    private static DataSource dataSource;
    private static Connection connection;
    private static JdbcTemplate jdbcTemplate;
    private static PlatformTransactionManager jdbcTran;
    
    public PubmedIDTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        ac = new ClassPathXmlApplicationContext("/jobApplicationContext.xml");  // Get job application context.
        dataSource = (DataSource)ac.getBean("dataSource");
        jdbcTemplate = (JdbcTemplate)ac.getBean("jdbcTemplate");                // Get JdbcTemplate.
        jdbcTran = (PlatformTransactionManager)ac.getBean("transactionManager");// Get JdbcTransactionManager.
        try {
            connection = dataSource.getConnection();
        }
        catch( SQLException e) {
        }
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
     * Test of isValid method, of class PubmedID.
     */
    @Test
    public void testIsValidLong() {
        System.out.println("isValid");
        PubmedID instance = new PubmedID(24248598);
        boolean expResult = true;
        boolean result = instance.isValid();
        assertEquals(expResult, result);
    }
    
//    @Test
//    public void testIsValidString() {
//        System.out.println("isValid");
//        PubmedID instance = new PubmedID("24248598");
//        boolean expResult = true;
//        boolean result = instance.isValid();
//        assertEquals(expResult, result);
//    }
//
//    /**
//     * Test of toString method, of class PubmedID.
//     */
//    @Test
//    public void testToString() {
//        System.out.println("toString");
//        PubmedID instance = new PubmedID(24248598);
//        String expResult = "24248598";
//        String result = instance.toString();
//        assertEquals(expResult, result);
//    }
//    
//    /**
//     * See jira bug #EMMA-525 for the motivation for this test.
//     */
//    @Test
//    public void testIsValidWebService() {
//        String pubmedId = "24248598";
//
//        Result result = getResult(pubmedId, 0);
//        assertNotNull(result);
//        assertEquals("24248598", result.getId());
//        assertEquals("Regulation of NO synthesis, local inflammation and innate immunity to pathogens by BET family proteins.", result.getTitle());
//        assertEquals("Wienerroither S, Rauch I, Rosebrock F, Jamieson AM, Bradner J, Muhar M, Zuber J, Müller M, Decker T.", result.getAuthorString());
//    }
//    
//    @Test
//    public void testAllPubmedid() {
//        List<BibliosDAO> biblioDAOList = null;
//        final String statement = 
//          "SELECT * FROM biblios\n"
//        + "WHERE pubmed_id IS NOT NULL\n";
//        
//        biblioDAOList = jdbcTemplate.query(statement, new BibliosDAO());
//        int i = 0;
//        for (BibliosDAO biblioDAO : biblioDAOList) {
//            String rawPubmedId = biblioDAO.getPubmed_id();
////                if (rawPubmedId.equals("11070163")){
//                if (rawPubmedId.equals("96264")){
//                    int m = 7;
//                    m += 9;
//                }
//            PubmedID pubmedId = new PubmedID(rawPubmedId);
//            String realId = getResult(rawPubmedId, i).getId();
//            if ( pubmedId.isValid()) {
//                
//                
//                assertEquals("[" + i + "] ", rawPubmedId, getResult(rawPubmedId, i).getId());
//            } else {
//                fail ("[" + i + "] According to PubmedID, " + pubmedId.pubmed_id + " is not a valid pubmedId.");
//            }
//            i++;
//        }
//        
//        System.out.println(i + "valid pubmed id records.");
//    }
//    
//    
//    // PRIVATE METHODS
//    
//    
//    private Result getResult(String pubmedId, int i) {
//        WSCitationImplService service = new WSCitationImplService();
//        WSCitationImpl port = service.getWSCitationImplPort();
//        String dataSet = "metadata";
//        String resultType = "core";
//        int offset = 0;
//        boolean synonym = false;
//        String email = "";
//        
//        ResponseWrapper resultsBean = null;
//        try {
//            resultsBean = port.searchPublications(pubmedId, dataSet, resultType, offset, synonym, email);
//        }
//        catch (Exception e) {
//            fail(e.getLocalizedMessage());
//        }
//        
//        if ((resultsBean == null)  || (resultsBean.getResultList() == null) || (resultsBean.getResultList().getResult() == null))
//            return null;
//Result aResult = null;
//        for (Result result : resultsBean.getResultList().getResult()) {
//            Integer cid = result.getCitationId();
//            String id = result.getId();
//            String pmid = result.getPmid();
//            if ((pmid != null) && (pmid.equals(pubmedId)))
//                aResult = result;
//        }
//        
//        return aResult;
//
//    }
    
    
}
