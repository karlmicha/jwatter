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
 * Exception thrown when a method is called that is not implemented.
 * 
 * @author kschneider
 * 
 */
public class NotImplementedException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Constructs a new exception instance. The detail message includes the
	 * class name, method name and source file location of the calling method.
	 */
	public NotImplementedException () {
		super("not implemented: "
				+ Thread.currentThread().getStackTrace()[3].toString());
	}

	/**
	 * Constructs a new exception instance. The detail message includes the
	 * signature of the missing method.
	 * 
	 * @param methodName
	 *            the name of the missing method
	 * @param args
	 *            the types of the arguments of the missing method
	 */
	public NotImplementedException (String methodName, Class<?>... args) {
		super("not implemented: " + constructSignatureString(methodName, args));
	}

	protected static String constructSignatureString (String methodName,
			Class<?>... args) {
		StringBuffer sig = new StringBuffer();
		sig.append(methodName);
		String sep = "(";
		for( Class<?> arg : args ) {
			sig.append(sep).append(arg.getName());
			sep = ",";
		}
		sig.append(")");
		return sig.toString();
	}
}
