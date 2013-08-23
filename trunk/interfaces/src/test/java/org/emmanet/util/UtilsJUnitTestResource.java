/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.emmanet.util;

import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;

/**
 *
 * @author mrelac
 */
public class UtilsJUnitTestResource extends TestCase {
    
    public UtilsJUnitTestResource(String testName) {
        super(testName);
    }
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}

    // Look for existing 'mailserver' property in emma.properties.
    public void testResourceMailserver() {
        String mailserver = Utils.getProperty(Constants.DEFAULT_EMMA_PROPERTIES_FILE, Constants.MAILSERVER);
        assertEquals("mx1.ebi.ac.uk", mailserver);
    }
    
    // Look for existing 'mailserver' property in default emma properties file.
    public void testEmmaResourceMailServer() {
        String mailserver = Utils.getEmmaProperty(Constants.MAILSERVER);
        assertEquals("mx1.ebi.ac.uk", mailserver);
    }
    
    // Look for property using invalid property file.
    public void testResourceInvalidFile() {
        String invalidHost = Utils.getProperty("JunkFile", Constants.DBHOST);
        assertEquals(null, invalidHost);
    }
    
    // Look for invalid property using valid property file.
    public void testResourceInvalidProperty() {
        String invalidPropertyName = Utils.getProperty(Constants.DEFAULT_EMMA_PROPERTIES_FILE, "InvalidPropertyName");
        assertEquals(null, invalidPropertyName);
    }
    
    // Look for existing resource with null delimiter.
    public void testResourceNullDelimiter() {
        String testBcc[] = Utils.getProperty(Constants.DEFAULT_EMMA_PROPERTIES_FILE, "TESTBCC", null);
        assertEquals(1, testBcc.length);
        assertEquals("philw@ebi.ac.uk,    mrelac@ebi.ac.uk", testBcc[0]);
    }
    
    // Look for existing resource with blank delimiter.
    public void testResourceBlankDelimiter() {
        String testBcc[] = Utils.getProperty(Constants.DEFAULT_EMMA_PROPERTIES_FILE, "TESTBCC", "  ");
        assertEquals(1, testBcc.length);
        assertEquals("philw@ebi.ac.uk,    mrelac@ebi.ac.uk", testBcc[0]);
    }
    
    // Look for existing "from" property (single value).
    // Should return a single array entry with the 'from' value.
    public void testResourceCommaDelimiterSingleValue() {
        String from[] = Utils.getProperty(Constants.DEFAULT_EMMA_PROPERTIES_FILE, Constants.FROM, ",");
        assertEquals(1, from.length);
        assertEquals("emma@emmanet.org", from[0]);
    }
    
    // Look for existing 'to' property (multiple values).
    public void testToResourceCommaDelimiterMultipleValues() {
        String to[] = Utils.getProperty(Constants.DEFAULT_EMMA_PROPERTIES_FILE, Constants.TO, ",");
        assertEquals(2, to.length);
        assertEquals("mrelac@ebi.ac.uk", to[0]);
        assertEquals("philw@ebi.ac.uk", to[1]);
    }
    
    // Look for existing 'TESTBCC' property (multiple values) with comma delimiter.
    public void testCcResourceCommaDelimiterMultipleValues() {
        String cc[] = Utils.getProperty(Constants.DEFAULT_EMMA_PROPERTIES_FILE, "TESTBCC", ",");
        assertEquals(2, cc.length);
        assertEquals("philw@ebi.ac.uk", cc[0]);
        assertEquals("mrelac@ebi.ac.uk", cc[1]);
    }
    
    // Look for non-existent 'bcc' property (multiple values) with comma delimiter.
    public void testMissingBccResourceCommaDelimiterMultipleValues() {
        String bcc[] = Utils.getProperty(Constants.DEFAULT_EMMA_PROPERTIES_FILE, Constants.BCC, ",");
        assertEquals(null, bcc);
    }
    
    // Look for non-existent 'bcc' property using getEmmaProperty. Should return null.
    public void testEmmaMissingBccResourceCommaDelimiterMultipleValues() {
        String bcc[] = Utils.getEmmaProperty(Constants.BCC, ",");
        assertEquals(null, bcc);
    }
    
    // Look for existing 'to' property (multiple values) with comma delimiter,
    // using emmaGetProperty. Should return mrelac, then philw.
    public void testEmmaResourceCommaDelimiterMultipleValues() {
        String to[] = Utils.getEmmaProperty(Constants.TO, ",");
        assertEquals(2, to.length);
        assertEquals("mrelac@ebi.ac.uk", to[0]);
        assertEquals("philw@ebi.ac.uk", to[1]);
    }
}
