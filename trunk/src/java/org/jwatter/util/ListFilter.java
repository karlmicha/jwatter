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

import java.util.LinkedList;
import java.util.List;

/**
 * A list filter provides a method to filter the elements of a given list and
 * return a list of only those elements that satisfy some condition.
 * 
 * A concrete subclass of this class must implement the abstract method eval.
 * Elements for which eval returns true pass the filter.
 * 
 * @author kschneider
 * 
 */
public abstract class ListFilter<T> {

    /**
     * Test whether the specified element should pass the filter.
     * 
     * @param element the element to test
     * @return true if the element passes the filter, else false
     */
	public abstract boolean eval (T element);

	/**
	 * Filters a list.
	 * 
	 * @param list the input list to the filter
	 * @return the filtered list
	 */
	public List<T> filter (List<T> list) {
		List<T> filtered = new LinkedList<T>();
		for( T element : list ) {
			if( eval(element) )
				filtered.add(element);
		}
		return filtered;
	}
}
