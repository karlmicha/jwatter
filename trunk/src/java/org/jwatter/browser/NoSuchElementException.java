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
 * Exception thrown when a requested element does not exist in the current page.
 * 
 * @author kschneider
 * 
 */
public class NoSuchElementException extends Exception {

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
	public NoSuchElementException (String... args) {
		super(constructDetailMessage(args));
	}

	/**
	 * Constructs a new exception instance. The detail message refers to a
	 * specific element in a list of matching elements.
	 * 
	 * @param which
	 *            specifies which of the matching elements was requested (1
	 *            based)
	 * @param numberOfElements
	 *            specifies the number of matching elements
	 * @param args
	 *            the arguments for constructing the detail message (see
	 *            {@link #NoSuchElementException(String...)})
	 */
	public NoSuchElementException (int which, int numberOfElements,
			String... args) {
		super(constructDetailMessage(args) + " (" + numberOfElements + " match"
				+ (numberOfElements == 1 ? "" : "es") + ", " + which
				+ " required)");
	}

	static String constructDetailMessage (String... args) {
		if( args.length == 0 ) {
			return null;
		} else if( args.length == 1 ) {
			return args[0];
		} else {
			return "no such element: " + constructElementDescription(args);
		}
	}

	static String constructElementDescription (String... args) {
		if( args.length == 0 ) {
			return null;
		}
		StringBuffer message = new StringBuffer();
		int attstart = 0;
		if( args.length % 2 == 0 ) {
			message.append("element");
		} else {
			message.append(args[attstart++]);
		}
		for( int attp = 0; attstart + attp < args.length; attp += 2 ) {
			if( attp == 0 ) {
				message.append("[");
			} else {
				message.append(", ");
			}
			message.append(args[attstart + attp]).append("=\"").append(
					args[attstart + attp + 1]).append("\"");
		}
		if( args.length >= 2 ) {
			message.append("]");
		}
		return message.toString();
	}
}
