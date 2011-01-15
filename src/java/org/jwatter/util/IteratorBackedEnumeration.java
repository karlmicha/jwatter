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

import java.util.Enumeration;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class IteratorBackedEnumeration<E>
        implements Enumeration<E> {

    protected Iterator<E> backingIterator;

    /**
     * Creates an enumeration that is backed by the specified iterator.
     * 
     * @param iterator
     *        the iterator to back this enumeration
     */
    public IteratorBackedEnumeration (Iterator<E> iterator) {
        this.backingIterator = iterator;
    }

    public boolean hasMoreElements () {
        return this.backingIterator.hasNext();
    }

    public E nextElement ()
            throws NoSuchElementException {
        return this.backingIterator.next();
    }

}
