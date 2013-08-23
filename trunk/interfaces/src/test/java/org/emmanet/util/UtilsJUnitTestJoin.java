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
public class UtilsJUnitTestJoin extends TestCase {
    
    public UtilsJUnitTestJoin(String testName) {
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

    // Test with array1 Integer null.
    public void testJoinArray1Null() {
        Integer[] ia1 = null;
        Integer[] ia2 = {0, 1, 2};
        Integer[] result = Utils.join(ia1, ia2);
        for (Integer i = 0; i < 3; i++)
            assertEquals(i, result[i]);
    }
    
    // Test with array1 empty.
    public void testJoinArray1Empty() {
        Integer[] ia1 = { };
        Integer[] ia2 = {0, 1, 2};
        Integer[] result = Utils.join(ia1, ia2);
        for (Integer i = 0; i < 3; i++)
            assertEquals(i, result[i]);
    }
    
    // Test with array2 null.
    public void testJoinArray2Null() {
        Integer[] ia1 = {0, 1, 2};
        Integer[] ia2 = null;
        Integer[] result = Utils.join(ia1, ia2);
        for (Integer i = 0; i < 3; i++)
            assertEquals(i, result[i]);
    }
    
    // Test with array2 empty.
    public void testJoinArray2Empty() {
        Integer[] ia1 = { 0, 1, 2};
        Integer[] ia2 = { };
        Integer[] result = Utils.join(ia1, ia2);
        for (Integer i = 0; i < 3; i++)
            assertEquals(i, result[i]);
    }
    
    // Test with array1 and array2 null.
    public void testJoinArray1NullArray2Null() {
        Integer[] result = Utils.join(null, null);
        assertEquals(null, result);
    }
    
    // Test with array1 and array2 empty.
    public void testJoinArray1EmptyArray2Empty() {
        Integer[] ia1 = { };
        Integer[] ia2 = { };
        Integer[] result = Utils.join(ia1, ia2);
        assertEquals(null, result);
    }
    
    // Test with array1 null and array2 empty.
    public void testJoinArray1NullArray2Empty() {
        Integer[] ia1 = null;
        Integer[] ia2 = { };
        Integer[] result = Utils.join(ia1, ia2);
        assertEquals(null, result);
    }
    
    // Test with array1 empty and array2 null.
    public void testJoinArray1EmptyArray2Null() {
        Integer[] ia1 = { };
        Integer[] ia2 = null;
        Integer[] result = Utils.join(ia1, ia2);
        assertEquals(null, result);
    }
    
    // Test with array1 and array2 as Integers.
    public void testJoinArrayInteger() {
        Integer[] ia1 = {0, 1, 2};
        Integer[] ia2 = {3, 4, 5};
        Integer[] result = Utils.join(ia1, ia2);
        for (Integer i = 0; i < 6; i++)
            assertEquals(i, result[i]);
    }
    
    // Test with array1 and array2 as Strings.
    public void testJoinArrayString() {
        String[] ia1 = {"0", "1", "2"};
        String[] ia2 = {"3", "4", "5"};
        String[] result = Utils.join(ia1, ia2);
        for (Integer i = 0; i < 6; i++)
            assertEquals(i.toString(), result[i]);
    }
}
