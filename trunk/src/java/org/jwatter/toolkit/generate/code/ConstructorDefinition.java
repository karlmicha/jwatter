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

import java.lang.reflect.Modifier;

public class ConstructorDefinition extends MethodDefinition
{
	protected AlternateConstructorCall alternateConstructorCall = null;

	public ConstructorDefinition ( int modifiers, String classname,
			Class<?>[] parametertypes, String[] parameternames,
			Class<?>[] exceptiontypes )
	{
		super(modifiers, null, classname, parametertypes, parameternames,
				exceptiontypes);
	}

	public void addAlternateConstructorCall ( ExpressionList arguments )
			throws MalformedCodeError
	{
		if ( alternateConstructorCall != null )
		{
			throw new MalformedCodeError("multiple constructor calls");
		}
		alternateConstructorCall = new AlternateConstructorCall(arguments);
	}

	public void addAlternateConstructorCall ( Expression... arguments )
	{
		addAlternateConstructorCall(new ExpressionList(arguments));
	}

	public void addAlternateConstructorCall ( String... arguments )
	{
		addAlternateConstructorCall(new ExpressionList(arguments));
	}

	public void addSuperConstructorCall ( ExpressionList arguments )
			throws MalformedCodeError
	{
		if ( alternateConstructorCall != null )
		{
			throw new MalformedCodeError("multiple constructor calls");
		}
		alternateConstructorCall =
				new AlternateConstructorCall(arguments, true);
	}

	public void addSuperConstructorCall ( Expression... arguments )
	{
		addSuperConstructorCall(new ExpressionList(arguments));
	}

	public void addSuperConstructorCall ( String... arguments )
	{
		addSuperConstructorCall(new ExpressionList(arguments));
	}

	@Override
	protected void formatName ( StringBuilder formatted )
	{
		formatted.append(Modifier.toString(modifiers)).append(" ");
		formatted.append(methodname);
	}

	@Override
	protected void formatBody ( StringBuilder formatted )
	{
		if ( alternateConstructorCall != null )
		{
			formatted.append("\t\t").append(alternateConstructorCall.format())
					.append("\n");
		}
		super.formatBody(formatted);
	}
}
