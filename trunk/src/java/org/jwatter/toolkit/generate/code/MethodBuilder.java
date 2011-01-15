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

import java.lang.reflect.Method;

public class MethodBuilder
{
	protected MethodDefinition methoddefinition;

	protected MethodBuilder ( int modifiers, Class<?> methodtype,
			String methodname, Class<?>[] parametertypes,
			String[] parameternames, Class<?>[] exceptiontypes )
	{
		methoddefinition =
				new MethodDefinition(modifiers, methodtype, methodname,
						parametertypes, parameternames, exceptiontypes);
	}

	protected MethodBuilder ( int modifiers, String classname,
			Class<?>[] parametertypes, String[] parameternames,
			Class<?>[] exceptiontypes )
	{
		methoddefinition =
				new ConstructorDefinition(modifiers, classname, parametertypes,
						parameternames, exceptiontypes);
	}

	public void addAlternateConstructorCall ( String... arguments )
			throws MalformedCodeError
	{
		if ( !(methoddefinition instanceof ConstructorDefinition) )
		{
			throw new MalformedCodeError(
					"cannot call alternate constructor in non constructor method");
		}
		((ConstructorDefinition) methoddefinition)
				.addAlternateConstructorCall(arguments);
	}

	public void addSuperConstructorCall ( String... arguments )
			throws MalformedCodeError
	{
		if ( !(methoddefinition instanceof ConstructorDefinition) )
		{
			throw new MalformedCodeError(
					"cannot call super constructor in non constructor method");
		}
		((ConstructorDefinition) methoddefinition)
				.addSuperConstructorCall(arguments);
	}

	public void addStatement ( Statement statement )
	{
		methoddefinition.addToBody(statement);
	}

	public void addMethodCall ( Method method, String... arguments )
	{
		methoddefinition.addToBody(new ExpressionStatement(new MethodCall(
				method, arguments)));
	}

	public void addMethodCall ( String instancevar, Method method,
			String... arguments )
	{
		methoddefinition.addToBody(new ExpressionStatement(new MethodCall(
				instancevar, method, arguments)));
	}

	public void addJavadoc ( String description )
	{
		addJavadoc(description, null, null, null);
	}

	public void addJavadoc ( String description, String[] parameterdoc )
	{
		addJavadoc(description, parameterdoc, null, null);
	}

	public void addJavadoc ( String[] parameterdoc )
	{
		addJavadoc(null, parameterdoc, null, null);
	}

	public void addJavadoc ( String description, String returndoc )
	{
		addJavadoc(description, null, returndoc, null);
	}

	public void addJavadoc ( String description, String[] parameterdoc,
			String returndoc )
	{
		addJavadoc(description, parameterdoc, returndoc, null);
	}

	public void addJavadoc ( String[] parameterdoc, String returndoc )
	{
		addJavadoc(null, parameterdoc, returndoc, null);
	}

	public void addJavadoc ( String description, String[] parameterdoc,
			String[] exceptiondoc )
	{
		addJavadoc(description, parameterdoc, null, exceptiondoc);
	}

	public void addJavadoc ( String[] parameterdoc, String[] exceptiondoc )
	{
		addJavadoc(null, parameterdoc, null, exceptiondoc);
	}

	public void addJavadoc ( String description, String returndoc,
			String[] exceptiondoc )
	{
		addJavadoc(description, null, returndoc, exceptiondoc);
	}

	public void addJavadoc ( String description, String[] parameterdoc,
			String returndoc, String[] exceptiondoc )
	{
		methoddefinition.addJavadoc(description, parameterdoc, returndoc,
				exceptiondoc);
	}

	public void addJavadoc ( String[] parameterdoc, String returndoc,
			String[] exceptiondoc )
	{
		addJavadoc(null, parameterdoc, returndoc, exceptiondoc);
	}

	public MethodDefinition getMethodDefinition ()
	{
		return methoddefinition;
	}
}
