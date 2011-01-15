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

import org.jwatter.util.StringUtil;

public class FieldDeclaration implements Formattable
{
	protected int modifiers;
	protected String classname;
	protected String fieldname;
	protected Expression initializer;

	FieldDeclaration ( int modifiers, Class<?> type, String fieldname )
	{
		this(modifiers, type, fieldname, (Expression) null);
	}

	FieldDeclaration ( int modifiers, String classname, String fieldname )
	{
		this(modifiers, classname, fieldname, (Expression) null);
	}

	FieldDeclaration ( int modifiers, Class<?> type, String fieldname,
			Expression initializer )
	{
		this(modifiers, type.getSimpleName(), fieldname, initializer);
	}

	FieldDeclaration ( int modifiers, String classname, String fieldname,
			Expression initializer )
	{
		this.modifiers = modifiers;
		this.classname = classname;
		this.fieldname = fieldname;
		this.initializer = initializer;
	}

	FieldDeclaration ( int modifiers, Class<?> type, String fieldname,
			String initializer )
	{
		this(modifiers, type, fieldname, new Expression(initializer));
	}

	FieldDeclaration ( int modifiers, String classname, String fieldname,
			String initializer )
	{
		this(modifiers, classname, fieldname, new Expression(initializer));
	}

	public String getName ()
	{
		return fieldname;
	}

	public String format ()
	{
		StringBuilder formatted = new StringBuilder();
		if ( modifiers != 0 )
		{
			formatted.append(Modifier.toString(modifiers)).append(" ");
		}
		formatted.append(classname).append(" ");
		formatted.append(fieldname);
		if ( initializer != null )
		{
			formatted.append(" = ").append(initializer.format());
		}
		formatted.append(";");
		return formatted.toString();
	}

	public String getEncoding ()
	{
		if ( StringUtil.containsNonLatin1Characters(classname) ) return "UTF-8";
		if ( StringUtil.containsNonLatin1Characters(fieldname) ) return "UTF-8";
		return initializer.getEncoding();
	}

}
