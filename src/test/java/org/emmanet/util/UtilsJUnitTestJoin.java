/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
    
    // Test with null.
    public void testTryParseDoubleWithNull() {
        System.out.println("testTryParseDoubleWithNull");
        Double expResult = null;
        Double result = Utils.tryParseDouble(null);
        assertEquals(expResult, result);
    }
    
    // Test with int.
    public void testTryParseDoubleWithInt() {
        System.out.println("testTryParseDoubleWithInt");
        Double expResult = 7.0;
        Double result = Utils.tryParseDouble(7);
        assertEquals(expResult, result);
    }
    
    // Test with int with trailing decimal point.
    public void testTryParseDoubleWithIntWithTrailingDecimalPoint() {
        System.out.println("testTryParseDoubleWithIntWithTrailingDecimalPoint");
        Double expResult = 7.;
        Double result = Utils.tryParseDouble(7);
        assertEquals(expResult, result);
    }
    
    // Test with float.
    public void testTryParseDoubleWithFloat() {
        System.out.println("testTryParseDoubleWithFloat");
        Double expResult = 7.0;
        Double result = Utils.tryParseDouble(7);
        assertEquals(expResult, result);
    }
    
    // Test with garbage.
    public void testTryParseDoubleWithGarbage() {
        System.out.println("testTryParseDoubleWithGarbage");
        Double expResult = null;
        Double result = Utils.tryParseDouble("abc");
        assertEquals(expResult, result);
    }
}
