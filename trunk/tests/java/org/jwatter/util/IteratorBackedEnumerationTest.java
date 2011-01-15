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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class IteratorBackedEnumerationTest {

    protected static final int MAX_LENGTH = 1000;

    protected Iterator<Integer> iterator;
    protected IteratorBackedEnumeration<Integer> enumeration;

    @Before
    public void setUp () {
        Random rand = new Random();
        List<Integer> list = new LinkedList<Integer>();
        int len = rand.nextInt(MAX_LENGTH);
        for (int i = 0; i < len; i++) {
            list.add(rand.nextInt());
        }
        iterator = list.iterator();
        enumeration = new IteratorBackedEnumeration<Integer>(list.iterator());
    }

    @Test
    public void testHasMoreElements ()
            throws Exception {
        int elpos = 0;
        while (iterator.hasNext()) {
            elpos++;
            assertTrue("hasMoreElements returned false at element " + elpos,
                       enumeration.hasMoreElements());
            iterator.next();
            enumeration.nextElement();
        }
        assertFalse(enumeration.hasMoreElements());
    }

    @Test
    public void testNextElement ()
            throws Exception {
        int elpos = 0;
        while (iterator.hasNext()) {
            elpos++;
            assertEquals("nextElement returned wrong value for element " + elpos, iterator.next(),
                         enumeration.nextElement());
        }
    }

    /**
     * Test that enumeration throws NoSuchElementException <i>after</i> hasMoreElements() returns
     * false.
     * 
     * @throws Exception
     */
    @Test(expected = NoSuchElementException.class)
    public void testNextElementException ()
            throws Exception {
        while (enumeration.hasMoreElements()) {
            enumeration.nextElement();
        }
        enumeration.nextElement();
    }

    @Test
    public void testHasMoreElementsEmptyEnumeration ()
            throws Exception {
        enumeration = new IteratorBackedEnumeration<Integer>(new LinkedList<Integer>().iterator());
        assertFalse(enumeration.hasMoreElements());
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextElementEmptyEnumeration ()
            throws Exception {
        enumeration = new IteratorBackedEnumeration<Integer>(new LinkedList<Integer>().iterator());
        enumeration.nextElement();
    }

    /**
     * Test that hasMoreElements() returns false on subsequent calls after the elements have been
     * exhausted.
     * 
     * @throws Exception
     */
    @Test
    public void testHasMoreElementsExhausted ()
            throws Exception {
        while (enumeration.hasMoreElements()) {
            enumeration.nextElement();
        }
        assertFalse(enumeration.hasMoreElements());
    }

}
