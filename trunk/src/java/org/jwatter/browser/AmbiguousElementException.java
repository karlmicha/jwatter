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
package org.jwatter.browser;


/**
 * Exception thrown when a requested element is not unique.
 * 
 * @author kschneider
 * 
 */
public class AmbiguousElementException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new exception instance. The form of the detail message
	 * depends on the number of arguments passed to the constructor:
	 * <ul>
	 * <li>With no arguments, the detail message is set to null.
	 * <li>With one argument, that argument is used as the detail message.
	 * <li>If the number of arguments is &gt;=2 and even, the arguments are
	 * interpreted as a list of attribute name/value pairs, and the detail
	 * message refers to an element with the specified attribute values.
	 * <li>If the number of arguments is &gt;=3 and uneven, the first argument
	 * is interpreted as an element name and the remaining arguments are
	 * interpreted as a list of attribute name/value pairs, and the detail
	 * message refers to an element with the specified element name and
	 * attribute values.
	 * </ul>
	 * 
	 * @param args
	 *            the arguments for constructing the detail message
	 */
	public AmbiguousElementException (String... args) {
		super(args.length == 0 ? null : args.length == 1 ? args[0]
				: "ambiguous element: "
						+ NoSuchElementException
								.constructElementDescription(args));
	}
}
