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
import static org.junit.Assert.assertTrue;

import java.util.NoSuchElementException;

import org.junit.Test;

public class AccessibleFieldIteratorTest {

    @Test
    public void testNoAccessibleFields ()
            throws Exception {
        AccessibleFieldIterator iterator = new AccessibleFieldIterator(C1.class);
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testAccessibleFields ()
            throws Exception {
        AccessibleFieldIterator iterator = new AccessibleFieldIterator(C2.class);
        assertTrue(iterator.hasNext());
        assertEquals(C2.class.getDeclaredField("field1"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(C2.class.getDeclaredField("field2"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSuperclassFields ()
            throws Exception {
        AccessibleFieldIterator iterator = new AccessibleFieldIterator(C3.class);
        assertTrue(iterator.hasNext());
        assertEquals(C3.class.getDeclaredField("field3"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(C2.class.getDeclaredField("field1"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(C2.class.getDeclaredField("field2"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testHiddenFields ()
            throws Exception {
        AccessibleFieldIterator iterator = new AccessibleFieldIterator(C4.class);
        assertTrue(iterator.hasNext());
        assertEquals(C4.class.getDeclaredField("field2"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(C2.class.getDeclaredField("field1"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testOnlySuperclassFields ()
            throws Exception {
        AccessibleFieldIterator iterator = new AccessibleFieldIterator(C5.class);
        assertTrue(iterator.hasNext());
        assertEquals(C2.class.getDeclaredField("field1"), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(C2.class.getDeclaredField("field2"), iterator.next());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNoSuchElementException ()
            throws Exception {
        AccessibleFieldIterator iterator = new AccessibleFieldIterator(C1.class);
        iterator.next();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemove ()
            throws Exception {
        AccessibleFieldIterator iterator = new AccessibleFieldIterator(C1.class);
        iterator.remove();
    }

    protected class C1 {

        @SuppressWarnings("unused")
        private int inaccessibleField;
    }

    protected class C2
            extends C1 {

        protected int field1;
        public int field2;
    }

    protected class C3
            extends C2 {

        protected int field3;
    }

    protected class C4
            extends C2 {

        protected String field2;
    }

    protected class C5
            extends C2 {}
}
