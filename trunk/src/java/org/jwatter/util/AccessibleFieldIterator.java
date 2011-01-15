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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

/**
 * An iterator over the fields that are accessible in a given class and any subclass of that class.
 * Any non private field declared in the given class is accessible in that class and its subclasses.
 * A field declared in a superclass of the given class is only accessible in that class and its
 * subclasses if it is not private and if it is not hidden by another field with the same name.
 * Synthetic fields are not returned by the iterator.
 * 
 * Fields are returned beginning with the accessible fields declared in the given class, followed by
 * any accessible fields in the given class's superclass, and so on. Fields are returned in the
 * order in which they are declared within each class.
 */
public class AccessibleFieldIterator
        implements Iterator<Field> {

    protected Class<?> klass;
    protected Class<?> currentClass;
    protected LinkedList<Field> fieldQueue;
    protected AppendableIterator<Field> fieldQueueIterator;
    protected Set<String> visibleFieldNames;

    /**
     * Creates a new iterator that iterates over the fields that are accessible in the given class
     * and its subclasses.
     * 
     * @param cls
     *        the class whose accessible fields the iterator will return.
     */
    public AccessibleFieldIterator (Class<?> cls) {
        this.klass = cls;
        this.currentClass = cls;
        this.fieldQueue = new LinkedList<Field>();
        this.fieldQueueIterator = new AppendableIterator<Field>(fieldQueue);
        this.visibleFieldNames = new HashSet<String>();
        this.addMoreFields();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext () {
        this.ensureMoreFields();
        return this.fieldQueueIterator.hasNext();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#next()
     */
    public Field next () {
        this.ensureMoreFields();
        return this.fieldQueueIterator.next();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Iterator#remove()
     */
    public void remove () {
        throw new UnsupportedOperationException("remove is not supported by this iterator");
    }

    protected void ensureMoreFields () {

        if (this.currentClass == null) {
            // all fields have been returned
            return;
        }

        if (this.fieldQueueIterator.hasNext()) {
            // there are still fields in the queue
            return;
        }

        // more fields exist, add them to the queue
        this.addMoreFields();
    }

    // search for accessible fields in the current class and add them to the queue
    // if the current class has no accessible fields, move up in the class hierarchy
    // until some fields are found or there is no more superclass
    protected void addMoreFields () {
        boolean needMoreFields = true;
        while (needMoreFields) {

            for (Field field : this.currentClass.getDeclaredFields()) {

                if (!(field.isSynthetic() || Modifier.isPrivate(field.getModifiers()) || this.visibleFieldNames
                                                                                                               .contains(field
                                                                                                                              .getName()))) {
                    try {
                        field.setAccessible(true);
                        this.fieldQueueIterator.add(field);
                        this.visibleFieldNames.add(field.getName());
                        needMoreFields = false;
                    }
                    catch (SecurityException e) {
                        // ignore fields that cannot be set accessible
                    }
                }
            }

            if (needMoreFields) {
                // found no accessible fields in the current class
                this.currentClass = this.currentClass.getSuperclass();
                if (this.currentClass == null) {
                    // no more fields
                    return;
                }
            }
        }
    }
}
