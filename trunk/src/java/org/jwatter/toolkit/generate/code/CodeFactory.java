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

public class CodeFactory
{
	public static ClassDefinition createClassDefinition ( int modifiers,
			String classname )
	{
		return new ClassDefinition(modifiers, classname);
	}

	public static ClassDefinition createClassDefinition ( int modifiers,
			String classname, Class<?> baseclass )
	{
		return new ClassDefinition(modifiers, classname, baseclass);
	}

	public static ClassDefinition createClassDefinition ( int modifiers,
			String classname, String baseclassname )
	{
		return new ClassDefinition(modifiers, classname, baseclassname);
	}

	public static FieldDeclaration createFieldDeclaration ( int modifiers,
			Class<?> type, String fieldname )
	{
		return new FieldDeclaration(modifiers, type, fieldname);
	}

	public static FieldDeclaration createFieldDeclaration ( int modifiers,
			String classname, String fieldname )
	{
		return new FieldDeclaration(modifiers, classname, fieldname);
	}

	public static FieldDeclaration createFieldDeclaration ( int modifiers,
			Class<?> type, String fieldname, String initializer )
	{
		return new FieldDeclaration(modifiers, type, fieldname, initializer);
	}

	public static FieldDeclaration createFieldDeclaration ( int modifiers,
			String classname, String fieldname, String initializer )
	{
		return new FieldDeclaration(modifiers, classname, fieldname,
				initializer);
	}

	public static MethodBuilder createNewMethodDefinition ( int modifiers,
			Class<?> methodtype, String methodname, Class<?>[] parametertypes,
			String[] parameternames, Class<?>[] exceptiontypes )
	{
		return new MethodBuilder(modifiers, methodtype, methodname,
				parametertypes, parameternames, exceptiontypes);
	}

	public static MethodBuilder createNewConstructorDefinition ( int modifiers,
			String classname, Class<?>[] parametertypes,
			String[] parameternames, Class<?>[] exceptiontypes )
	{
		return new MethodBuilder(modifiers, classname, parametertypes,
				parameternames, exceptiontypes);
	}
}
