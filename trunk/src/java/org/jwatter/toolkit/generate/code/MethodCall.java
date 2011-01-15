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

public class MethodCall extends Expression implements Formattable
{
	protected Expression instance;
	protected Class<?> returntype;
	protected String methodname;
	protected ExpressionList arguments;

	protected MethodCall ( Expression instance, String methodname,
			int numparameters, ExpressionList arguments )
	{
		this(instance, methodname, arguments);
		if ( numparameters != arguments.length() )
		{
			throw new MalformedCodeError(
					"number of formal and actual parameters for method "
							+ methodname + " does not match");
		}
	}

	public MethodCall ( String methodname )
	{
		this(null, methodname);
	}

	public MethodCall ( Expression instance, String methodname )
	{
		this(instance, methodname, new ExpressionList());
	}

	public MethodCall ( Method method ) throws MalformedCodeError
	{
		this(null, method);
	}

	public MethodCall ( Expression instance, Method method )
			throws MalformedCodeError
	{
		this(instance, method, new ExpressionList());
	}

	public MethodCall ( String methodname, ExpressionList arguments )
	{
		this(null, methodname, arguments);
	}

	public MethodCall ( Expression instance, String methodname,
			ExpressionList arguments )
	{
		super((instance != null ? instance.format() + "." : "") + methodname
				+ "(" + arguments.format() + ")");

		this.instance = instance;
		this.methodname = methodname;
		this.arguments = arguments;
		this.returntype = null;
	}

	public MethodCall ( Method method, ExpressionList arguments )
			throws MalformedCodeError
	{
		this(null, method, arguments);
	}

	public MethodCall ( Expression instance, Method method,
			ExpressionList arguments ) throws MalformedCodeError
	{
		this(instance, method.getName(), method.getParameterTypes().length,
				arguments);
		this.returntype = method.getReturnType();
	}

	public MethodCall ( String methodname, Expression... arguments )
	{
		this(null, methodname, arguments);
	}

	public MethodCall ( Expression instance, String methodname,
			Expression... arguments )
	{
		this(instance, methodname, new ExpressionList(arguments));
	}

	public MethodCall ( Method method, Expression... arguments )
			throws MalformedCodeError
	{
		this(method, new ExpressionList(arguments));
	}

	public MethodCall ( Expression instance, Method method,
			Expression... arguments ) throws MalformedCodeError
	{
		this(instance, method, new ExpressionList(arguments));
	}

	public MethodCall ( String methodname, String... arguments )
	{
		this(null, methodname, arguments);
	}

	public MethodCall ( String instancevar, String methodname,
			String... arguments )
	{
		this(instancevar != null ? new Expression(instancevar) : null,
				methodname, new ExpressionList(arguments));
	}

	public MethodCall ( Method method, String... arguments )
			throws MalformedCodeError
	{
		this(method, new ExpressionList(arguments));
	}

	public MethodCall ( String instancevar, Method method, String... arguments )
			throws MalformedCodeError
	{
		this(instancevar != null ? new Expression(instancevar) : null, method,
				new ExpressionList(arguments));
	}

	@Override
	public Class<?> getType ()
	{
		return returntype;
	}
}
