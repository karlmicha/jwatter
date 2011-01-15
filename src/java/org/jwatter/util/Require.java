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

import org.jwatter.util.UnsatisfiedRequirementError;

/**
 * A set of static methods to set requirements in tests, similar to
 * org.junit.Assert. If a test does not satisfy a requirement, an
 * {@link UnsatisfiedRequirementError} is thrown, which results in a test error.
 * 
 * @author kschneider
 * 
 */
public class Require
{

	/**
	 * Requires that two objects are equal.
	 * 
	 * @param required
	 *            the required value
	 * @param actual
	 *            the value to check against the required value
	 */
	public static void requireEquals ( Object required, Object actual )
	{
		if ( !required.equals(actual) )
		{
			throw new UnsatisfiedRequirementError("required: " + required
					+ ", actual: " + actual);
		}
	}

	/**
	 * Requires that two objects are equal.
	 * 
	 * @param message
	 *            the message to show when the two objects are not equal
	 * @param required
	 *            the required value
	 * @param actual
	 *            the value to check against the required value
	 */
	public static void requireEquals ( String message, Object required,
			Object actual )
	{
		if ( !required.equals(actual) )
		{
			throw new UnsatisfiedRequirementError(message + " required: "
					+ required + ", actual: " + actual);
		}
	}

	/**
	 * Requires that a condition is true.
	 * 
	 * @param condition
	 *            the condition to check
	 */
	public static void requireTrue ( Boolean condition )
	{
		requireTrue("required condition is not true", condition);
	}

	/**
	 * Requires that a condition is true.
	 * 
	 * @param message
	 *            the message to show when the condition is not true
	 * @param condition
	 *            the condition to check
	 */
	public static void requireTrue ( String message, Boolean condition )
	{
		if ( !condition )
		{
			throw new UnsatisfiedRequirementError(message);
		}
	}

	/**
	 * Requires that a condition is false.
	 * 
	 * @param condition
	 *            the condition to check
	 */
	public static void requireFalse ( Boolean condition )
	{
		requireFalse("required condition is not false", condition);
	}

	/**
	 * Requires that a condition is false.
	 * 
	 * @param message
	 *            the message to show when the condition is not false
	 * @param condition
	 *            the condition to check
	 */
	public static void requireFalse ( String message, Boolean condition )
	{
		if ( condition )
		{
			throw new UnsatisfiedRequirementError(message);
		}
	}

	/**
	 * Requires that an object is null.
	 * 
	 * @param object
	 *            the object to check
	 */
	public static void requireNull ( Object object )
	{
		requireNull("object is not null", object);
	}

	/**
	 * Requires that an object is null.
	 * 
	 * @param message
	 *            the message to show when the object is not null
	 * @param object
	 *            the object to check
	 */
	public static void requireNull ( String message, Object object )
	{
		if ( object != null )
		{
			throw new UnsatisfiedRequirementError(message);
		}
	}

	/**
	 * Requires that an object is not null.
	 * 
	 * @param object
	 *            the object to check
	 */
	public static void requireNotNull ( Object object )
	{
		requireNotNull("object is null", object);
	}

	/**
	 * Requires that an object is not null.
	 * 
	 * @param message
	 *            the message to show when the object is null
	 * @param object
	 *            the object to check
	 */
	public static void requireNotNull ( String message, Object object )
	{
		if ( object == null )
		{
			throw new UnsatisfiedRequirementError(message);
		}
	}

}
