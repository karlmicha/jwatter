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

public class ExpressionList implements Formattable
{
	protected Expression[] expressions;

	public ExpressionList ()
	{
		expressions = new Expression[0];
	}

	public ExpressionList ( Expression... expressions )
	{
		this.expressions = expressions;
	}

	public ExpressionList ( String... expressions )
	{
		this.expressions = new Expression[expressions.length];
		for ( int i = 0 ; i < expressions.length ; i++ )
		{
			this.expressions[i] = new Expression(expressions[i]);
		}
	}

	public String format ()
	{
		StringBuilder formatted = new StringBuilder();
		if ( expressions.length > 0 )
		{
			formatted.append(expressions[0].format());
			for ( int i = 1 ; i < expressions.length ; i++ )
			{
				formatted.append(", ").append(expressions[i].format());
			}
		}
		return formatted.toString();
	}

	public int length ()
	{
		return expressions.length;
	}

	public String getEncoding ()
	{
		for ( Expression expression : expressions )
		{
			String enc = expression.getEncoding();
			if ( enc.equals("UTF-8") ) return enc;
		}
		return "ISO-8859-1";
	}
}
