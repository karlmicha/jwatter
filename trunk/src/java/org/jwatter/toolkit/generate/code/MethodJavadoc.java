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
package org.jwatter.toolkit.generate.code;

public class MethodJavadoc extends Javadoc
{
	public void addDescription ( String description )
	{
		println(description);
		println();
	}

	public void addParam ( String name, String doc )
	{
		ensureNewline();
		print("@param ");
		println(name);
		print("           ");
		println(doc);
	}

	public void addReturn ( String doc )
	{
		ensureNewline();
		print("@return ");
		println(doc);
	}

	public void addThrows ( Class<?> exceptionType, String doc )
	{
		ensureNewline();
		print("@throws ");
		println(exceptionType.getSimpleName());
		print("           ");
		println(doc);
	}
}
