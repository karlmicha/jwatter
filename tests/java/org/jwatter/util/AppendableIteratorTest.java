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

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.junit.Test;

import org.jwatter.util.AppendableIterator;

public class AppendableIteratorTest
{

	@Test
	public void testAppendableIterator ()
	{
		AppendableIterator<String> i = new AppendableIterator<String>();
		Collection<String> c = i.getUnderlyingCollection();
		assertEquals(HashSet.class, c.getClass());
		assertEquals(0, c.size());
	}

	public void testAppendableIteratorSet ()
	{
		Set<String> s = new HashSet<String>();
		s.add("a");
		s.add("b");
		s.add("c");
		AppendableIterator<String> i = new AppendableIterator<String>(s);
		Set<String> t = new HashSet<String>();
		while ( i.hasNext() )
		{
			String e = i.next();
			assertTrue(s.contains(e));
			t.add(e);
		}
		assertEquals(s, t);
	}

	public void testAppendableIteratorList ()
	{
		List<String> s = new LinkedList<String>();
		s.add("a");
		s.add("b");
		s.add("a");
		s.add("c");
		AppendableIterator<String> i = new AppendableIterator<String>(s);
		assertTrue(s == i.getUnderlyingCollection());
		assertEquals("a", i.next());
		assertEquals("b", i.next());
		assertEquals("a", i.next());
		assertEquals("c", i.next());
		assertFalse(i.hasNext());
	}

	@Test(expected = NullPointerException.class)
	public void testAppendableIteratorCollectionNull ()
	{
		new AppendableIterator<String>(null);
	}

	@Test
	public void testAddSet ()
	{
		AppendableIterator<String> i = new AppendableIterator<String>();
		assertTrue(i.add("a"));
		assertTrue(i.add("b"));
		assertFalse(i.add("a"));
		assertTrue(i.add("c"));
		assertEquals(3, i.getUnderlyingCollection().size());
		assertEquals("a", i.next());
		assertEquals("b", i.next());
		assertEquals("a", i.next());
		assertEquals("c", i.next());
		assertFalse(i.hasNext());
	}

	@Test
	public void testAddList ()
	{
		AppendableIterator<String> i =
				new AppendableIterator<String>(new LinkedList<String>());
		assertTrue(i.add("a"));
		assertTrue(i.add("b"));
		assertTrue(i.add("a"));
		assertTrue(i.add("c"));
		assertEquals(4, i.getUnderlyingCollection().size());
		assertEquals("a", i.next());
		assertEquals("b", i.next());
		assertEquals("a", i.next());
		assertEquals("c", i.next());
		assertFalse(i.hasNext());
	}

	@Test
	public void testAddNewSet ()
	{
		AppendableIterator<String> i = new AppendableIterator<String>();
		assertTrue(i.addNew("a"));
		assertTrue(i.addNew("b"));
		assertFalse(i.addNew("a"));
		assertTrue(i.addNew("c"));
		assertEquals(3, i.getUnderlyingCollection().size());
		assertEquals("a", i.next());
		assertEquals("b", i.next());
		assertEquals("c", i.next());
		assertFalse(i.hasNext());
	}

	@Test
	public void testAddNewList ()
	{
		AppendableIterator<String> i =
				new AppendableIterator<String>(new LinkedList<String>());
		assertTrue(i.addNew("a"));
		assertTrue(i.addNew("b"));
		assertTrue(i.addNew("a"));
		assertTrue(i.addNew("c"));
		assertEquals(4, i.getUnderlyingCollection().size());
		assertEquals("a", i.next());
		assertEquals("b", i.next());
		assertEquals("a", i.next());
		assertEquals("c", i.next());
		assertFalse(i.hasNext());
	}

	@Test
	public void testAddAllSet ()
	{
		List<String> c = new LinkedList<String>();
		c.add("a");
		c.add("b");
		c.add("a");
		c.add("c");
		AppendableIterator<String> i = new AppendableIterator<String>();
		assertTrue(i.addAll(c));
		assertEquals(3, i.getUnderlyingCollection().size());
		assertTrue(i.getUnderlyingCollection().containsAll(c));
		assertEquals("a", i.next());
		assertEquals("b", i.next());
		assertEquals("a", i.next());
		assertEquals("c", i.next());
		assertFalse(i.hasNext());
	}

	@Test
	public void testAddAllList ()
	{
		List<String> c = new LinkedList<String>();
		c.add("a");
		c.add("b");
		c.add("a");
		c.add("c");
		AppendableIterator<String> i =
				new AppendableIterator<String>(new LinkedList<String>());
		assertTrue(i.addAll(c));
		assertEquals(c, i.getUnderlyingCollection());
		assertEquals("a", i.next());
		assertEquals("b", i.next());
		assertEquals("a", i.next());
		assertEquals("c", i.next());
		assertFalse(i.hasNext());
	}

	@Test
	public void testAddAllNewSet ()
	{
		List<String> c = new LinkedList<String>();
		c.add("a");
		c.add("b");
		c.add("a");
		c.add("c");
		AppendableIterator<String> i = new AppendableIterator<String>();
		assertTrue(i.add("c"));
		assertTrue(i.addAllNew(c));
		assertEquals(3, i.getUnderlyingCollection().size());
		assertTrue(i.getUnderlyingCollection().containsAll(c));
		assertEquals("c", i.next());
		assertEquals("a", i.next());
		assertEquals("b", i.next());
		assertFalse(i.hasNext());
	}

	@Test
	public void testAddAllNewList ()
	{
		List<String> c = new LinkedList<String>();
		c.add("a");
		c.add("b");
		c.add("a");
		c.add("c");
		AppendableIterator<String> i =
				new AppendableIterator<String>(new LinkedList<String>());
		assertTrue(i.add("c"));
		assertTrue(i.addAll(c));
		assertEquals(5, i.getUnderlyingCollection().size());
		assertEquals("c", i.next());
		assertEquals("a", i.next());
		assertEquals("b", i.next());
		assertEquals("a", i.next());
		assertEquals("c", i.next());
		assertFalse(i.hasNext());
	}

	@Test
	public void testAddSetAfterNext ()
	{
		AppendableIterator<String> i = new AppendableIterator<String>();
		assertFalse(i.hasNext());
		assertTrue(i.add("a"));
		assertTrue(i.hasNext());
		assertEquals("a", i.next());
		assertFalse(i.hasNext());
		assertFalse(i.add("a"));
		assertTrue(i.hasNext());
		assertEquals("a", i.next());
		assertFalse(i.hasNext());
		assertEquals(1, i.getUnderlyingCollection().size());
		assertTrue(i.getUnderlyingCollection().contains("a"));
	}

	@Test
	public void testAddListAfterNext ()
	{
		AppendableIterator<String> i =
				new AppendableIterator<String>(new LinkedList<String>());
		assertFalse(i.hasNext());
		assertTrue(i.add("a"));
		assertTrue(i.hasNext());
		assertEquals("a", i.next());
		assertFalse(i.hasNext());
		assertTrue(i.add("a"));
		assertTrue(i.hasNext());
		assertEquals("a", i.next());
		assertFalse(i.hasNext());
		assertEquals(2, i.getUnderlyingCollection().size());
	}

	@Test
	public void testAddNewSetAfterNext ()
	{
		AppendableIterator<String> i = new AppendableIterator<String>();
		assertFalse(i.hasNext());
		assertTrue(i.addNew("a"));
		assertTrue(i.hasNext());
		assertEquals("a", i.next());
		assertFalse(i.hasNext());
		assertFalse(i.addNew("a"));
		assertFalse(i.hasNext());
		assertEquals(1, i.getUnderlyingCollection().size());
		assertTrue(i.getUnderlyingCollection().contains("a"));
	}

	@Test
	public void testAddNewListAfterNext ()
	{
		AppendableIterator<String> i =
				new AppendableIterator<String>(new LinkedList<String>());
		assertFalse(i.hasNext());
		assertTrue(i.addNew("a"));
		assertTrue(i.hasNext());
		assertEquals("a", i.next());
		assertFalse(i.hasNext());
		assertTrue(i.addNew("a"));
		assertTrue(i.hasNext());
		assertEquals("a", i.next());
		assertFalse(i.hasNext());
		assertEquals(2, i.getUnderlyingCollection().size());
	}

	@Test
	public void testAddAllSetTwice ()
	{
		List<String> c = new LinkedList<String>();
		c.add("a");
		c.add("b");
		c.add("a");
		c.add("c");
		AppendableIterator<String> i = new AppendableIterator<String>();
		assertTrue(i.addAll(c));
		assertFalse(i.addAll(c));
		assertEquals(3, i.getUnderlyingCollection().size());
		int n = 0;
		for ( ; i.hasNext() ; i.next() , n++ )
			;
		assertEquals(8, n);
	}

	@Test
	public void testAddAllListTwice ()
	{
		List<String> c = new LinkedList<String>();
		c.add("a");
		c.add("b");
		c.add("a");
		c.add("c");
		AppendableIterator<String> i =
				new AppendableIterator<String>(new LinkedList<String>());
		assertTrue(i.addAll(c));
		assertTrue(i.addAll(c));
		assertEquals(8, i.getUnderlyingCollection().size());
		int n = 0;
		for ( ; i.hasNext() ; i.next() , n++ )
			;
		assertEquals(8, n);
	}

	@Test
	public void testAddAllNewSetTwice ()
	{
		List<String> c = new LinkedList<String>();
		c.add("a");
		c.add("b");
		c.add("a");
		c.add("c");
		AppendableIterator<String> i = new AppendableIterator<String>();
		assertTrue(i.addAllNew(c));
		assertFalse(i.addAllNew(c));
		assertEquals(3, i.getUnderlyingCollection().size());
		int n = 0;
		for ( ; i.hasNext() ; i.next() , n++ )
			;
		assertEquals(3, n);
	}

	@Test
	public void testAddAllNewListTwice ()
	{
		List<String> c = new LinkedList<String>();
		c.add("a");
		c.add("b");
		c.add("a");
		c.add("c");
		AppendableIterator<String> i =
				new AppendableIterator<String>(new LinkedList<String>());
		assertTrue(i.addAllNew(c));
		assertTrue(i.addAllNew(c));
		assertEquals(8, i.getUnderlyingCollection().size());
		int n = 0;
		for ( ; i.hasNext() ; i.next() , n++ )
			;
		assertEquals(8, n);
	}

	@Test
	public void testHasNext ()
	{
		AppendableIterator<String> i = new AppendableIterator<String>();
		assertFalse(i.hasNext());
		i.add("a");
		assertTrue(i.hasNext());
		assertEquals("a", i.next());
		assertFalse(i.hasNext());
		i.add("b");
		i.add("c");
		assertTrue(i.hasNext());
		i.next();
		assertTrue(i.hasNext());
		i.next();
		assertFalse(i.hasNext());
	}

	@Test(expected = NoSuchElementException.class)
	public void testNextEmpty ()
	{
		new AppendableIterator<String>().next();
	}

	@Test
	public void testRemove ()
	{
		AppendableIterator<String> i = new AppendableIterator<String>();
		i.add("a");
		i.add("b");
		i.add("c");
		i.next();
		String removed = i.next();
		i.remove();
		assertTrue(i.hasNext());
		i.next();
		assertFalse(i.hasNext());
		assertEquals(2, i.getUnderlyingCollection().size());
		assertFalse(i.getUnderlyingCollection().contains(removed));
	}

	@Test(expected = IllegalStateException.class)
	public void testRemoveEmpty ()
	{
		new AppendableIterator<String>().remove();
	}

	@Test(expected = IllegalStateException.class)
	public void testRemove2 ()
	{
		AppendableIterator<String> i = new AppendableIterator<String>();
		i.add("a");
		i.next();
		i.remove();
		i.remove();
	}

	@Test
	public void testGetUnderlyingCollection ()
	{
		Collection<String> s = new LinkedList<String>();
		assertTrue(s == new AppendableIterator<String>(s)
				.getUnderlyingCollection());
	}

}
