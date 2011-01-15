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

/**
 * Thrown to indicate that a requirement is not satisfied in a test. This will
 * result in a test error.
 * 
 * @author kschneider
 * 
 */
public class UnsatisfiedRequirementError extends Error {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance with null as its detail message.
	 */
	public UnsatisfiedRequirementError () {
		super();
	}

	/**
	 * Creates a new instance with the specified detail message.
	 * 
	 * @param message
	 *            the detail message
	 */
	public UnsatisfiedRequirementError (String message) {
		super(message);
	}

}
