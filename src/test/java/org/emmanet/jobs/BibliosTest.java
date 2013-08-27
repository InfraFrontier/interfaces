/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.jobs;

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
public class BibliosTest {
    
    public BibliosTest() {
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
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:

    // Test using a property name that does not exist in the property file
    // and no programatically added property name/value pairs.
     @Test
     public void testEmptyConfigFileCcNoProgrammatic() {
         EmmaBiblioJOB ebj = new EmmaBiblioJOB();
         String[] actualResult = ebj.getCc();
         String[] expectedResult = null;
         assertArrayEquals(expectedResult, actualResult);
     }
    
    // Test using a property name that does not exist in the property file
    // and no programatically added property name/value pairs.
     @Test
     public void testEmptyConfigFileBccNoProgrammatic() {
         EmmaBiblioJOB ebj = new EmmaBiblioJOB();
         String[] actualResult = ebj.getBcc();
         String[] expectedResult = null;
         assertArrayEquals(expectedResult, actualResult);
     }

    // Test using a property name that exists in the property file
    // and no programatically added property name/value pairs.
     @Test
     public void testNonemptyConfigFileFromNoProgrammatic() {
         EmmaBiblioJOB ebj = new EmmaBiblioJOB();
         String[] actualResult = ebj.getFrom();
         String[] expectedResult = new String[] {"emma@emmanet.org"};
         assertArrayEquals(expectedResult, actualResult);
     }
     
    // Test using a property name that exists in the property file
    // and no programatically added property name/value pairs.
     @Test
     public void testNonemptyConfigFileToNoProgrammatic() {
         EmmaBiblioJOB ebj = new EmmaBiblioJOB();
         String[] actualResult = ebj.getTo();
         String[] expectedResult = new String[] {"mrelac@ebi.ac.uk", "philw@ebi.ac.uk"};
         assertArrayEquals(expectedResult, actualResult);
     }

    // Test using a property name that does not exist in the property file
    // and programatically added property name/value pairs.
     @Test
     public void testEmptyConfigFileCcProgrammatic() {
         EmmaBiblioJOB ebj = new EmmaBiblioJOB();
         ebj.addCc(new String[] {"cc1", "cc2", "cc3"});
         String[] actualResult = ebj.getCc();
         String[]expectedResult = new String[] {"cc1", "cc2", "cc3"};
         assertArrayEquals(expectedResult, actualResult);
     }
     
    // Test using a property name that does not exist in the property file
    // and programatically added property name/value pairs.
     @Test
     public void testEmptyConfigFileBccProgrammatic() {
         EmmaBiblioJOB ebj = new EmmaBiblioJOB();
         ebj.addBcc(new String[] {"bcc1", "bcc2", "bcc3", "bcc4"});
         
         String[] actualResult = ebj.getBcc();
         String[]expectedResult = new String[] {"bcc1", "bcc2", "bcc3", "bcc4"};
         assertArrayEquals(expectedResult, actualResult);
     }

    // Test using a property name that exists in the property file
    // and programatically added property name/value pairs.
     @Test
     public void testNonemptyConfigFileFromProgrammatic() {
         EmmaBiblioJOB ebj = new EmmaBiblioJOB();
         ebj.addFrom(new String[] {"from1"});
         
         String[] actualResult = ebj.getFrom();
         String[] expectedResult = new String[] {"emma@emmanet.org", "from1"};
         assertArrayEquals(expectedResult, actualResult);
     }
     
    // Test using a property name that exists in the property file
    // and programatically added property name/value pairs.
     @Test
     public void testNonemptyConfigFileToProgrammatic() {
         EmmaBiblioJOB ebj = new EmmaBiblioJOB();
         ebj.addTo(new String[] {"to1", "to2"});
         
         String[] actualResult = ebj.getTo();
         String[] expectedResult = new String[] {"mrelac@ebi.ac.uk", "philw@ebi.ac.uk", "to1", "to2"};
         assertArrayEquals(expectedResult, actualResult);
     }
}