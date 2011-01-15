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

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * An iterator over a collection of elements that allows to add new elements to
 * it (and to the underlying collection). New elements are always appended to
 * the iteration sequence.
 * 
 * Note that modifying the underlying collection externally (using any other
 * method than those provided by the iterator) does not affect the iterator.
 * 
 * @author kschneider
 * 
 */
public class AppendableIterator<T> implements Iterator<T>
{

	protected Collection<T> elements;
	protected LinkedList<T> iteratorQueue;
	protected T last;
	protected boolean nextCalled;

	/**
	 * Creates a new iterator, using a HashSet as the underlying set. Initially,
	 * the underlying set is empty.
	 */
	public AppendableIterator ()
	{
		this(new HashSet<T>());
	}

	/**
	 * Creates a new iterator, using the specified collection as the underlying
	 * collection. The iteration order of the elements that are present in the
	 * collection at the time the iterator is created is the same as if
	 * iterating over the collection.
	 * 
	 * @param initialElements
	 *            the underlying set for this iterator
	 * @throws NullPointerException
	 *             if the specified set is null
	 */
	public AppendableIterator ( Collection<T> initialElements )
	{
		if ( initialElements == null )
		{
			throw new NullPointerException("underlying set must not be null");
		}
		elements = initialElements;
		iteratorQueue = new LinkedList<T>(initialElements);
		nextCalled = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#hasNext()
	 */
	public boolean hasNext ()
	{
		return iteratorQueue.size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#next()
	 */
	public T next ()
	{
		if ( iteratorQueue.size() == 0 )
		{
			throw new NoSuchElementException("no more elements");
		}
		last = iteratorQueue.poll();
		nextCalled = true;
		return last;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.util.Iterator#remove()
	 */
	public void remove ()
	{
		if ( !nextCalled )
		{
			throw new IllegalStateException(
					"next() must be called before remove()");
		}
		nextCalled = false;
		elements.remove(last);
	}

	/**
	 * Adds the specified element to this iterator and to the underlying
	 * collection. The element is appended to the iteration sequence.
	 * 
	 * @param e
	 *            the element to add
	 * @return true if the underlying collection has changed as a result of the
	 *         call
	 */
	public boolean add ( T e )
	{
		iteratorQueue.add(e);
		return elements.add(e);
	}

	/**
	 * Adds the specified element to the underlying collection. If the
	 * underlying collection changed as a result of the call, the element is
	 * appended to the iteration sequence. Otherwise the iteration sequence is
	 * not changed.
	 * 
	 * In particular, if the underlying collection already contains the
	 * specified element and does not permit duplicates, the iterator is not
	 * changed.
	 * 
	 * @param e
	 *            the element to add
	 * @return true if the underlying collection (and the iteration sequence)
	 *         changed as a result of the call, else false
	 */
	public boolean addNew ( T e )
	{
		if ( elements.add(e) )
		{
			iteratorQueue.add(e);
			return true;
		}
		else
		{
			return false;
		}
	}

	/**
	 * Adds all elements in the specified collection to this iterator and to the
	 * underlying collection. Elements are appended to the iteration sequence in
	 * the order given when iterating over the specified collection.
	 * 
	 * @param els
	 *            the elements to add
	 * @return true if the underlying collection changed as a result of the call
	 */
	public boolean addAll ( Collection<T> els )
	{
		iteratorQueue.addAll(els);
		return elements.addAll(els);
	}

	/**
	 * Adds each element from the specified collection to the underlying
	 * collection. If the underlying collection changes as a result of adding an
	 * element, the element is appended to the iteration sequence. Elements are
	 * added in the order given when iterating over the specified collection.
	 * 
	 * In particular, if the underlying collection does not permit duplicate
	 * elements, only elements that are not already contained in the underlying
	 * collection are added to the iterator.
	 * 
	 * @param els
	 *            the elements to add
	 * @return true if any elements were added to the iterator, else false
	 */
	public boolean addAllNew ( Collection<T> els )
	{
		boolean changed = false;
		for ( T e : els )
		{
			if ( addNew(e) ) changed = true;
		}
		return changed;
	}

	/**
	 * Returns the underlying collection.
	 * 
	 * @return the underlying collection
	 */
	public Collection<T> getUnderlyingCollection ()
	{
		return elements;
	}
}
