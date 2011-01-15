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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * A set of properties with stable iteration order. In particular, iterating over properties loaded
 * from a file returns the properties in the same order as they are in the file, and saving
 * properties to a file writes them in the same order as in the file they were loaded from, or
 * added. New properties are always added at the end of the properties list.
 * 
 */
public class OrderedProperties
        extends Properties {

    private static final long serialVersionUID = 1L;

    protected RemoveTiedLinkedHashSet<Object> keys;
    protected RemoveTiedArrayList<Object> values;
    protected RemoveTiedLinkedHashSet<Map.Entry<Object, Object>> entrySet;
    protected ArrayList<Map.Entry<Object, Object>> entryOrder;
    protected HashMap<Object, Integer> keyPos;

    /**
     * Creates an empty property list with no default values.
     */
    public OrderedProperties () {
        super();
        this._init();
    }

    /**
     * Creates an empty property list with the specified defaults.
     * 
     * @param defaults
     *        the defaults
     */
    public OrderedProperties (Properties defaults) {
        super(defaults);
        this._init();
    }

    protected void _init () {
        keys = new RemoveTiedLinkedHashSet<Object>(this);
        values = new RemoveTiedArrayList<Object>(this);
        entrySet = new RemoveTiedLinkedHashSet<Map.Entry<Object, Object>>(this);
        entryOrder = new ArrayList<Map.Entry<Object, Object>>();
        keyPos = new HashMap<Object, Integer>();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Hashtable#put(java.lang.Object, java.lang.Object)
     */
    @Override
    public synchronized Object put (Object key, Object value) {
        Object previous = this._put(key, value);
        if (previous == null) {
            Map.Entry<Object, Object> entry = new RemoveTiedEntry<Object, Object>(key, value, this);
            entryOrder.add(entry);
            keyPos.put(key, keys.size());
            keys.add(key);
            values.add(value);
            entrySet.add(entry);
        }
        else {
            int pos = keyPos.get(key);
            entryOrder.get(pos).setValue(value);
            values.set(pos, value);
        }
        return previous;
    }

    protected Object _put (Object key, Object value) {
        return super.put(key, value);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Hashtable#remove(java.lang.Object)
     */
    @Override
    public synchronized Object remove (Object key) {
        if (!this.containsKey(key))
            return null;
        Object value = this.getProperty((String)key);
        this.remove(key, null);
        return value;
    }

    protected int getValueIndex (Object value) {
        int index = 0;
        for (Map.Entry<Object, Object> entry : entryOrder) {
            if (entry.getValue().equals(value))
                return index;
            index++;
        }
        return -1;
    }

    protected void remove (Object arg, Object notifier) {
        Map.Entry<?, ?> entry;
        int index;
        if (notifier == null || notifier == keys) {
            index = keyPos.get(arg);
            entry = entryOrder.get(index);
        }
        else if (notifier == values) {
            // cannot use values to get index because arg is no longer in values
            index = this.getValueIndex(arg);
            entry = entryOrder.get(index);
        }
        else if (notifier == entrySet) {
            // arg no longer in entrySet
            index = keyPos.get(((Map.Entry<?, ?>)arg).getKey());
            entry = (Map.Entry<?, ?>)arg;
        }
        else {
            throw new IllegalArgumentException("unknown notifier");
        }
        this.remove(entry, index, notifier);
    }

    protected void remove (int index, Object notifier) {
        if (index < 0) {
            throw new IllegalArgumentException("invalid index " + index);
        }
        this.remove(entryOrder.get(index), index, notifier);
    }

    protected void remove (Map.Entry<?, ?> entry, int index, Object notifier) {
        this._remove(entry.getKey());
        if (notifier != keys) {
            keys._remove(entry.getKey());
        }
        if (notifier != values) {
            values._remove(index);
        }
        if (notifier != entrySet) {
            entrySet._remove(entry);
        }
        entryOrder.remove(index);
        keyPos.remove(entry.getKey());
    }

    protected Object _remove (Object key) {
        return super.remove(key);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Hashtable#clear()
     */
    @Override
    public synchronized void clear () {
        this.clear(null);
    }

    protected void clear (Object notifier) {
        this._clear();
        if (notifier != keys) {
            keys._clear();
        }
        if (notifier != values) {
            values._clear();
        }
        if (notifier != entrySet) {
            entrySet._clear();
        }
        entryOrder.clear();
        keyPos.clear();
    }

    protected void _clear () {
        super.clear();
    }

    /**
     * Called by an embedded data structure when a property has been removed from that data
     * structure. Removes the property from the other embedded data structures and from the
     * properties set.
     * 
     * @param notifier
     *        the embedded data structure from which a property was removed
     * @param arg
     *        the element being removed (a key, value or entry)
     */
    protected void removed (Object notifier, Object arg) {
        if (!(notifier == keys || notifier == values || notifier == entrySet)) {
            throw new IllegalArgumentException("unknown observed object: "
                + (notifier == null ? null : notifier.getClass().getName()));
        }
        this.remove(arg, notifier);
    }

    /**
     * Called by an embedded data structure when a property has been removed from that data
     * structure. Removes the property from the other embedded data structures and from the
     * properties set.
     * 
     * @param notifier
     *        the embedded data structure from which a property was removed
     * @param index
     *        the index of the property in the properties list
     */
    protected void removed (Object notifier, int index) {
        if (!(notifier == keys || notifier == values || notifier == entrySet)) {
            throw new IllegalArgumentException("unknown observed object: "
                + (notifier == null ? null : notifier.getClass().getName()));
        }
        this.remove(index, notifier);
    }

    /**
     * Called by an embedded data structure that has been cleared. Clears the other embedded data
     * structures and the properties set.
     * 
     * @param notifier
     *        the embedded data structure that was cleared
     */
    protected void cleared (Object notifier) {
        if (!(notifier == keys || notifier == values || notifier == entrySet)) {
            throw new IllegalArgumentException("unknown observed object: "
                + (notifier == null ? null : notifier.getClass().getName()));
        }
        this.clear(notifier);
    }

    /**
     * Called by {@link RemoveTiedEntry#setValue(Object)} to indicate that the value of an entry in
     * {@link OrderedProperties#entrySet} has changed. Updates the value in
     * {@link OrderedProperties#values} and in the properties set.
     * 
     * @param entry
     *        the entry that has changed.
     */
    protected void changed (Map.Entry<?, ?> entry) {
        int index = this.keyPos.get(entry.getKey());
        this.values.set(index, entry.getValue());
        this._put(entry.getKey(), entry.getValue());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Properties#propertyNames()
     */
    @Override
    public Enumeration<?> propertyNames () {
        return new IteratorBackedEnumeration<Object>(keys.iterator());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Hashtable#keys()
     */
    @Override
    public synchronized Enumeration<Object> keys () {
        return new IteratorBackedEnumeration<Object>(keys.iterator());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Hashtable#keySet()
     */
    @Override
    public Set<Object> keySet () {
        return Collections.synchronizedSet(keys);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Hashtable#values()
     */
    @Override
    public Collection<Object> values () {
        return Collections.synchronizedCollection(values);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Hashtable#elements()
     */
    @Override
    public Enumeration<Object> elements () {
        return new IteratorBackedEnumeration<Object>(values.iterator());
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Hashtable#entrySet()
     */
    @Override
    public Set<Map.Entry<Object, Object>> entrySet () {
        return Collections.synchronizedSet(entrySet);
    }

    /**
     * Implementation of the {@link Map.Entry} interface for (key, value) pairs in
     * {@link OrderedProperties}.
     * 
     * @param <K>
     *        the key type (for {@link OrderedProperties} entries this is {@link Object})
     * @param <V>
     *        the value type (for {@link OrderedProperties} entries this is {@link Object})
     */
    public class Entry<K, V>
            implements Map.Entry<K, V> {

        protected K key;
        protected V value;
        protected volatile int hash;

        /**
         * Creates a new (key, value) pair.
         * 
         * @param key
         *        the key of the entry
         * @param value
         *        the value of the entry
         */
        public Entry (K key, V value) {
            this.key = key;
            this.value = value;
            hash();
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Map.Entry#getKey()
         */
        public K getKey () {
            return key;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Map.Entry#getValue()
         */
        public V getValue () {
            return value;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.util.Map.Entry#setValue(java.lang.Object)
         */
        public synchronized V setValue (V value) {
            V old = this.value;
            this.value = value;
            hash();
            return old;
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#equals(java.lang.Object)
         */
        @Override
        public boolean equals (Object o) {
            return o != null
                && (o instanceof Entry<?, ?>)
                && (key == null ? (((Entry<?, ?>)o).getKey() == null)
                               : (key.equals(((Entry<?, ?>)o).getKey())))
                && (value == null ? (((Entry<?, ?>)o).getValue() == null)
                                 : (value.equals(((Entry<?, ?>)o).getValue())));
        }

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Object#hashCode()
         */
        @Override
        public int hashCode () {
            return hash;
        }

        protected void hash () {
            hash = (key == null ? 0 : key.hashCode()) ^ (value == null ? 0 : value.hashCode());
        }
    }

    protected class RemoveTiedEntry<K, V>
            extends Entry<K, V> {

        protected OrderedProperties orderedProperties;

        protected RemoveTiedEntry (K key, V value, OrderedProperties orderedProperties) {
            super(key, value);
            this.orderedProperties = orderedProperties;
        }

        @Override
        public synchronized V setValue (V value) {
            V old = super.setValue(value);
            this.orderedProperties.changed(this);
            return old;
        }
    }

    protected class RemoveTiedArrayList<E>
            extends ArrayList<E> {

        private static final long serialVersionUID = 1L;

        protected OrderedProperties orderedProperties;

        protected RemoveTiedArrayList (OrderedProperties orderedProperties) {
            super();
            this.orderedProperties = orderedProperties;
        }

        @Override
        public boolean remove (Object o) {
            boolean removed = _remove(o);
            if (!removed)
                return removed;
            this.orderedProperties.removed(this, o);
            return removed;
        }

        @Override
        public void clear () {
            this._clear();
            this.orderedProperties.cleared(this);
        }

        @Override
        public Iterator<E> iterator () {
            RemoveTiedIterator<E> observedIterator =
                new RemoveTiedIterator<E>(super.iterator(), this.orderedProperties, this);
            return observedIterator;
        }

        protected void _clear () {
            super.clear();
        }

        protected E _remove (int index) {
            return super.remove(index);
        }

        protected boolean _remove (Object o) {
            return super.remove(o);
        }
    }

    protected class RemoveTiedLinkedHashSet<E>
            extends LinkedHashSet<E> {

        private static final long serialVersionUID = 1L;

        protected OrderedProperties orderedProperties;

        protected RemoveTiedLinkedHashSet (OrderedProperties orderedProperties) {
            super();
            this.orderedProperties = orderedProperties;
        }

        @Override
        public boolean remove (Object o) {
            boolean removed = _remove(o);
            if (!removed)
                return removed;
            this.orderedProperties.removed(this, o);
            return removed;
        }

        @Override
        public void clear () {
            this._clear();
            this.orderedProperties.cleared(this);
        }

        @Override
        public Iterator<E> iterator () {
            return new RemoveTiedIterator<E>(super.iterator(), orderedProperties, this);
        }

        protected void _clear () {
            super.clear();
        }

        protected boolean _remove (Object o) {
            return super.remove(o);
        }
    }

    protected class RemoveTiedIterator<E>
            implements Iterator<E> {

        protected Iterator<E> iterator;
        protected int index;
        protected OrderedProperties orderedProperties;
        protected Object iteratorParent;

        protected RemoveTiedIterator (Iterator<E> iterator, OrderedProperties orderedProperties,
                                      Object iteratorParent) {
            this.iterator = iterator;
            this.orderedProperties = orderedProperties;
            this.iteratorParent = iteratorParent;
            this.index = -1;
        }

        public boolean hasNext () {
            return this.iterator.hasNext();
        }

        public E next () {
            this.index++;
            return this.iterator.next();
        }

        public void remove () {
            this.iterator.remove();
            this.orderedProperties.removed(this.iteratorParent, this.index);
        }
    }
}
