/*
Copyright 2011 Karl-Michael Schneider

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package org.jwatter.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

public class OrderedPropertiesTest {

    protected static final String PROPERTIES_COMMENT = "OrderedProperties";
    protected static final String[][] inputPropertiesArray =
        new String[][] { { "H.a37.c", "14" }, { "S.i9.i", "u_i" }, { "H.i7.s", "/c/p/T/p/i" },
                        { "S.i5.s", "/c/p/R/p/i/s" }, { "S.a8.c", "S" }, { "V.i1.s", "https://" },
                        { "H.b2.n", "dash" }, { "S.a13.c", "B S" }, { "H.a7.c", "I" },
                        { "H.a31.c", "8" }, { "O.a15.c", "_Mail_" }, { "H.a34.c", "11" },
                        { "S.a2.c", "Preferences" }, { "O.a18.c", "About " }, { "H.a47.c", "24" },
                        { "S.a5.c", "Home" }, { "O.i5.s", "/c/p/T/p/i/c" }, { "O.b1.i", "mQB" },
                        { "S.a10.c", "Simple Search" }, { "H.b2.i", "dash" }, { "O.a12.c", "O S" } };
    protected static final String[][] addPropertiesArray =
        new String[][] { { "V.b2.i", "d_b" }, { "H.a15.c", "M" }, { "H.a41.c", "18" },
                        { "H.i3.s", "/c/p/T/p/i" }, { "S.i1.s", "/c/p/l" }, { "H.a18.c", "S" },
                        { "H.a44.c", "21" }, { "V.i4.s", "https://" }, { "S.i2.s", "/c/p/b" },
                        { "V.f1.n", "header" }, { "H.a12.c", "Received" } };
    protected static final String[] newProperty = new String[] { "new.key", "new.value" };

    protected String inputPropertiesString;
    protected OrderedProperties properties;
    protected int randomInputPropertiesIndex;
    protected String randomInputPropertyName;
    protected String randomInputPropertyValue;

    @Before
    public void setUp () {
        properties = new OrderedProperties();
        generateInputPropertiesString();
        System.out.println(inputPropertiesString);
        randomInputPropertiesIndex = new Random().nextInt(inputPropertiesArray.length);
        randomInputPropertyName = inputPropertiesArray[randomInputPropertiesIndex][0];
        randomInputPropertyValue = inputPropertiesArray[randomInputPropertiesIndex][1];
    }

    @Test
    public void testProperties ()
            throws Exception {
        setProperties(inputPropertiesArray);
        checkAll(inputPropertiesArray);
    }

    @Test
    public void testLoad ()
            throws Exception {
        loadProperties(inputPropertiesString);
        checkAll(inputPropertiesArray);
    }

    @Test
    public void testStore ()
            throws Exception {
        setProperties(inputPropertiesArray);
        String outputPropertiesString = storeProperties();
        assertEquals(inputPropertiesString, outputPropertiesString);
    }

    @Test
    public void testLoadStore ()
            throws Exception {
        loadProperties(inputPropertiesString);
        String outputPropertiesString = storeProperties();
        assertEquals(inputPropertiesString, outputPropertiesString);
    }

    @Test
    public void testStoreLoad ()
            throws Exception {
        setProperties(inputPropertiesArray);
        String outputPropertiesString = storeProperties();
        properties = new OrderedProperties();
        loadProperties(outputPropertiesString);
        checkAll(inputPropertiesArray);
    }

    @Test
    public void testSetPropertyAfterLoad ()
            throws Exception {
        loadProperties(inputPropertiesString);
        setProperties(addPropertiesArray);
        checkAppended(inputPropertiesArray, addPropertiesArray);
    }

    @Test
    public void testLoadAfterSetProperty ()
            throws Exception {
        setProperties(addPropertiesArray);
        loadProperties(inputPropertiesString);
        checkAppended(addPropertiesArray, inputPropertiesArray);
    }

    @Test
    public void testPut ()
            throws Exception {
        setProperties(inputPropertiesArray);
        putProperties(addPropertiesArray);
        checkAppended(inputPropertiesArray, addPropertiesArray);
    }

    @Test
    public void testPutAfterLoad ()
            throws Exception {
        loadProperties(inputPropertiesString);
        putProperties(addPropertiesArray);
        checkAppended(inputPropertiesArray, addPropertiesArray);
    }

    @Test
    public void testPutAll ()
            throws Exception {
        Map<String, String> inputMap = new LinkedHashMap<String, String>();
        for (String[] prop : inputPropertiesArray) {
            inputMap.put(prop[0], prop[1]);
        }
        properties.putAll(inputMap);
        checkAll(inputPropertiesArray);
    }

    @Test
    public void testSetPropertyNewKey ()
            throws Exception {
        setProperties(inputPropertiesArray);
        Object old = properties.setProperty(newProperty[0], newProperty[1]);
        assertNull(old);
        checkAdded(inputPropertiesArray, newProperty);
    }

    @Test
    public void testSetPropertyKeyExists ()
            throws Exception {
        setProperties(inputPropertiesArray);
        Object old = properties.setProperty(randomInputPropertyName, newProperty[1]);
        assertEquals(randomInputPropertyValue, old);
        checkReplaced(inputPropertiesArray, randomInputPropertiesIndex, newProperty[1]);
    }

    @Test
    public void testPutNewKey ()
            throws Exception {
        setProperties(inputPropertiesArray);
        Object old = properties.put(newProperty[0], newProperty[1]);
        assertNull(old);
        checkAdded(inputPropertiesArray, newProperty);
    }

    @Test
    public void testPutKeyExists ()
            throws Exception {
        setProperties(inputPropertiesArray);
        Object old = properties.put(randomInputPropertyName, newProperty[1]);
        assertEquals(randomInputPropertyValue, old);
        checkReplaced(inputPropertiesArray, randomInputPropertiesIndex, newProperty[1]);
    }

    @Test
    public void testRemove ()
            throws Exception {
        setProperties(inputPropertiesArray);
        Object old = properties.remove(randomInputPropertyName);
        assertEquals(randomInputPropertyValue, old);
        checkDeleted(inputPropertiesArray, randomInputPropertiesIndex);
    }

    @Test
    public void testRemoveKeyDoesNotExist ()
            throws Exception {
        setProperties(inputPropertiesArray);
        Object old = properties.remove(newProperty[0]);
        assertNull(old);
        checkAll(inputPropertiesArray);
    }

    @Test
    public void testClear ()
            throws Exception {
        setProperties(inputPropertiesArray);
        properties.clear();
        checkAll(new String[][] {});
    }

    @Test
    public void testKeySetRemove ()
            throws Exception {
        setProperties(inputPropertiesArray);
        boolean removed = properties.keySet().remove(randomInputPropertyName);
        assertTrue(removed);
        checkDeleted(inputPropertiesArray, randomInputPropertiesIndex);
    }

    @Test
    public void testKeySetRemoveKeyDoesNotExist ()
            throws Exception {
        setProperties(inputPropertiesArray);
        boolean removed = properties.keySet().remove(newProperty[0]);
        assertFalse(removed);
        checkAll(inputPropertiesArray);
    }

    @Test
    public void testKeySetClear ()
            throws Exception {
        setProperties(inputPropertiesArray);
        properties.keySet().clear();
        checkAll(new String[][] {});
    }

    @Test
    public void testKeySetIteratorRemove ()
            throws Exception {
        setProperties(inputPropertiesArray);
        Iterator<Object> keyIterator = properties.keySet().iterator();
        for (int i = 0; i <= randomInputPropertiesIndex; i++) {
            keyIterator.next();
        }
        keyIterator.remove();
        checkDeleted(inputPropertiesArray, randomInputPropertiesIndex);
    }

    @Test
    public void testValuesRemove ()
            throws Exception {
        setProperties(inputPropertiesArray);
        boolean removed = properties.values().remove(randomInputPropertyValue);
        assertTrue(removed);
        int index = 0;
        while (!inputPropertiesArray[index][1].equals(randomInputPropertyValue))
            index++;
        checkDeleted(inputPropertiesArray, index);
    }

    @Test
    public void testValuesRemoveValueDoesNotExist ()
            throws Exception {
        setProperties(inputPropertiesArray);
        boolean removed = properties.values().remove(newProperty[1]);
        assertFalse(removed);
        checkAll(inputPropertiesArray);
    }

    @Test
    public void testValuesClear ()
            throws Exception {
        setProperties(inputPropertiesArray);
        properties.values().clear();
        checkAll(new String[][] {});
    }

    @Test
    public void testValuesIteratorRemove ()
            throws Exception {
        setProperties(inputPropertiesArray);
        Iterator<Object> iterator = properties.values().iterator();
        for (int i = 0; i <= randomInputPropertiesIndex; i++) {
            iterator.next();
        }
        iterator.remove();
        checkDeleted(inputPropertiesArray, randomInputPropertiesIndex);
    }

    @Test
    public void testEntrySetRemove ()
            throws Exception {
        setProperties(inputPropertiesArray);
        boolean removed =
            properties.entrySet()
                      .remove(
                              properties.new Entry<Object, Object>(randomInputPropertyName,
                                                                   randomInputPropertyValue));
        assertTrue(removed);
        checkDeleted(inputPropertiesArray, randomInputPropertiesIndex);
    }

    @Test
    public void testEntrySetRemoveEntryDoesNotExist ()
            throws Exception {
        setProperties(inputPropertiesArray);
        boolean removed =
            properties.entrySet().remove(
                                         properties.new Entry<Object, Object>(newProperty[0],
                                                                              newProperty[1]));
        assertFalse(removed);
        checkAll(inputPropertiesArray);
    }

    @Test
    public void testEntrySetClear ()
            throws Exception {
        setProperties(inputPropertiesArray);
        properties.entrySet().clear();
        checkAll(new String[][] {});
    }

    @Test
    public void testEntrySetIteratorRemove ()
            throws Exception {
        setProperties(inputPropertiesArray);
        Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
        for (int i = 0; i <= randomInputPropertiesIndex; i++) {
            iterator.next();
        }
        iterator.remove();
        checkDeleted(inputPropertiesArray, randomInputPropertiesIndex);
    }

    @Test
    public void testEntrySetEntrySetValue ()
            throws Exception {
        setProperties(inputPropertiesArray);
        int i = 0;
        for (Map.Entry<Object, Object> e : properties.entrySet()) {
            if (i == randomInputPropertiesIndex) {
                e.setValue(newProperty[1]);
                break;
            }
            i++;
        }
        checkReplaced(inputPropertiesArray, randomInputPropertiesIndex, newProperty[1]);
    }

    @Test
    public void testEntrySetIteratorEntrySetValue ()
            throws Exception {
        setProperties(inputPropertiesArray);
        Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
        for (int i = 0; i < randomInputPropertiesIndex; i++) {
            iterator.next();
        }
        iterator.next().setValue(newProperty[1]);
        checkReplaced(inputPropertiesArray, randomInputPropertiesIndex, newProperty[1]);
    }

    protected void setProperties (String[][] propertiesArray)
            throws Exception {
        for (String[] prop : propertiesArray) {
            properties.setProperty(prop[0], prop[1]);
        }
    }

    protected void putProperties (String[][] propertiesArray)
            throws Exception {
        for (String[] prop : propertiesArray) {
            properties.put(prop[0], prop[1]);
        }
    }

    protected void loadProperties (String propertiesString)
            throws IOException {
        properties.load(new ByteArrayInputStream(propertiesString.getBytes()));
    }

    protected String storeProperties ()
            throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        properties.store(bytes, PROPERTIES_COMMENT);
        return bytes.toString();
    }

    protected void generateInputPropertiesString () {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println('#' + PROPERTIES_COMMENT);
        pw.println('#' + new Date().toString());
        for (String[] prop : inputPropertiesArray) {
            pw.println(prop[0] + '=' + prop[1].replace(":", "\\:"));
        }
        pw.close();
        inputPropertiesString = sw.toString();
    }

    protected void copyArray (String[][] source, String[][] dest) {
        copyArray(source, dest, 0, 0, source.length);
    }

    protected void copyArray (String[][] source, String[][] dest, int sourceStart, int destStart,
                              int count) {
        for (int i = 0; i < count; i++) {
            int s = sourceStart + i;
            int d = destStart + i;
            dest[d] = new String[source[s].length];
            for (int j = 0; j < source[s].length; j++) {
                dest[d][j] = source[s][j];
            }
        }
    }

    protected String[][] deleteArrayElement (final String[][] array, int deleteIndex) {
        String[][] newArray = new String[array.length - 1][];
        copyArray(array, newArray, 0, 0, deleteIndex);
        copyArray(array, newArray, deleteIndex + 1, deleteIndex, array.length - deleteIndex - 1);
        return newArray;
    }

    protected String[][] addArrayElement (final String[][] array, final String[] newElement) {
        return appendArrays(array, new String[][] { newElement });
    }

    protected String[][] replaceArrayValue (final String[][] array, int replaceIndex,
                                            String newValue) {
        String[][] newArray = new String[array.length][];
        copyArray(array, newArray);
        newArray[replaceIndex][1] = newValue;
        return newArray;
    }

    protected String[][] appendArrays (final String[][] array, final String[][] append) {
        String[][] newArray = new String[array.length + append.length][];
        copyArray(array, newArray);
        copyArray(append, newArray, 0, array.length, append.length);
        return newArray;
    }

    protected void checkDeleted (String[][] propertiesArray, int deleted) {
        checkAll(deleteArrayElement(propertiesArray, deleted));
        assertFalse(properties.containsKey(propertiesArray[deleted][0]));
        assertNull(properties.get(propertiesArray[deleted][0]));
        assertNull(properties.getProperty(propertiesArray[deleted][0]));
    }

    protected void checkAdded (String[][] propertiesArray, String[] added) {
        checkAll(addArrayElement(propertiesArray, added));
    }

    protected void checkReplaced (String[][] propertiesArray, int replaced, String replacement) {
        checkAll(replaceArrayValue(propertiesArray, replaced, replacement));
    }

    protected void checkAppended (String[][] propertiesArray, String[][] appendedArray) {
        checkAll(appendArrays(propertiesArray, appendedArray));
    }

    protected void checkAll (String[][] propertiesArray) {
        checkSize(propertiesArray);
        checkProperties(propertiesArray);
        checkKeySet(propertiesArray);
        checkKeySetIterator(propertiesArray);
        checkKeys(propertiesArray);
        checkPropertyNames(propertiesArray);
        checkValues(propertiesArray);
        checkValuesIterator(propertiesArray);
        checkElements(propertiesArray);
        checkEntrySet(propertiesArray);
        checkEntrySetIterator(propertiesArray);
    }

    protected void checkSize (String[][] propertiesArray) {
        assertEquals(propertiesArray.length, properties.size());
        assertEquals(propertiesArray.length, properties.keySet().size());
        assertEquals(propertiesArray.length, properties.values().size());
        assertEquals(propertiesArray.length, properties.entrySet().size());
    }

    protected void checkProperties (String[][] propertiesArray) {
        for (String[] p : propertiesArray) {
            assertTrue(properties.containsKey(p[0]));
            assertEquals(p[1], properties.get(p[0]));
            assertEquals(p[1], properties.getProperty(p[0]));
        }
    }

    protected void checkKeySet (String[][] propertiesArray) {
        int i = 0;
        for (Object key : properties.keySet()) {
            assertEquals(propertiesArray[i++][0], key);
        }
        assertEquals(propertiesArray.length, i);
    }

    protected void checkKeySetIterator (String[][] propertiesArray) {
        Iterator<Object> iterator = properties.keySet().iterator();
        for (String[] p : propertiesArray) {
            assertTrue(iterator.hasNext());
            assertEquals(p[0], iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    protected void checkKeys (String[][] propertiesArray) {
        Enumeration<Object> e = properties.keys();
        for (String[] p : propertiesArray) {
            assertTrue(e.hasMoreElements());
            assertEquals(p[0], e.nextElement());
        }
        assertFalse(e.hasMoreElements());
    }

    protected void checkPropertyNames (String[][] propertiesArray) {
        Enumeration<?> e = properties.propertyNames();
        for (String[] p : propertiesArray) {
            assertTrue(e.hasMoreElements());
            assertEquals(p[0], e.nextElement());
        }
        assertFalse(e.hasMoreElements());
    }

    protected void checkValues (String[][] propertiesArray) {
        int i = 0;
        for (Object value : properties.values()) {
            assertEquals(propertiesArray[i++][1], value);
        }
        assertEquals(propertiesArray.length, i);
    }

    protected void checkValuesIterator (String[][] propertiesArray) {
        Iterator<Object> iterator = properties.values().iterator();
        for (String[] p : propertiesArray) {
            assertTrue(iterator.hasNext());
            assertEquals(p[1], iterator.next());
        }
        assertFalse(iterator.hasNext());
    }

    protected void checkElements (String[][] propertiesArray) {
        Enumeration<Object> e = properties.elements();
        for (String[] p : propertiesArray) {
            assertTrue(e.hasMoreElements());
            assertEquals(p[1], e.nextElement());
        }
        assertFalse(e.hasMoreElements());
    }

    protected void checkEntrySet (String[][] propertiesArray) {
        int i = 0;
        for (Entry<Object, Object> e : properties.entrySet()) {
            assertEquals(propertiesArray[i][0], e.getKey());
            assertEquals(propertiesArray[i][1], e.getValue());
            i++;
        }
        assertEquals(propertiesArray.length, i);
    }

    protected void checkEntrySetIterator (String[][] propertiesArray) {
        Iterator<Map.Entry<Object, Object>> iterator = properties.entrySet().iterator();
        for (String[] p : propertiesArray) {
            assertTrue(iterator.hasNext());
            Map.Entry<Object, Object> entry = iterator.next();
            assertEquals(p[0], entry.getKey());
            assertEquals(p[1], entry.getValue());
        }
        assertFalse(iterator.hasNext());
    }
}
