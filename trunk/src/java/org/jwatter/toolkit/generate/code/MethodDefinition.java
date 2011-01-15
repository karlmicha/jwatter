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
import java.util.ArrayList;
import java.util.List;

import org.jwatter.util.StringUtil;

public class MethodDefinition implements Formattable
{
	protected int modifiers;
	protected Class<?> returntype;
	protected String methodname;
	protected Class<?>[] parametertypes;
	protected String[] parameternames;
	protected Class<?>[] exceptiontypes;
	protected List<Statement> body;
	protected List<Comment> comments;
	protected MethodJavadoc javadoc;

	MethodDefinition ( int modifiers, Class<?> returntype, String methodname,
			Class<?>[] parametertypes, String[] parameternames,
			Class<?>[] exceptiontypes ) throws MalformedCodeError
	{
		if ( parametertypes == null )
		{
			parametertypes = new Class<?>[0];
		}
		if ( parameternames == null )
		{
			parameternames = new String[0];
		}
		if ( exceptiontypes == null )
		{
			exceptiontypes = new Class<?>[0];
		}
		if ( parametertypes.length != parameternames.length )
		{
			throw new MalformedCodeError(
					"Number of parameter types does not match number of parameter names");
		}
		this.modifiers = modifiers;
		this.returntype = returntype;
		this.methodname = methodname;
		this.parametertypes = parametertypes;
		this.parameternames = parameternames;
		this.exceptiontypes = exceptiontypes;
		this.body = new ArrayList<Statement>();
		this.comments = new ArrayList<Comment>();
		this.javadoc = null;
	}

	public void addToBody ( Statement... statements )
	{
		for ( Statement statement : statements )
		{
			body.add(statement);
		}
	}

	public void addComment ( String comment )
	{
		comments.add(new Comment(comment));
	}

	public void addJavadoc ( String description, String[] parameterdoc,
			String returndoc, String[] exceptiondoc )
	{
		javadoc = new MethodJavadoc();
		if ( description != null )
		{
			javadoc.addDescription(description);
		}
		if ( parameterdoc != null )
		{
			for ( int i = 0 ; i < parameterdoc.length ; i++ )
			{
				if ( i >= parameternames.length ) break;
				javadoc.addParam(parameternames[i], parameterdoc[i]);
			}
		}
		if ( returndoc != null )
		{
			javadoc.addReturn(returndoc);
		}
		if ( exceptiondoc != null )
		{
			for ( int i = 0 ; i < exceptiondoc.length ; i++ )
			{
				if ( i >= exceptiontypes.length ) break;
				javadoc.addThrows(exceptiontypes[i], exceptiondoc[i]);
			}
		}
	}

	public String getName ()
	{
		return methodname;
	}

	public void setName ( String name )
	{
		methodname = name;
	}

	public String getSignature ( boolean longTypeNames )
	{
		StringBuilder sig = new StringBuilder();
		sig.append(methodname).append("(");
		if ( parametertypes.length > 0 )
		{
			sig.append(longTypeNames ? parametertypes[0].getName()
									: parametertypes[0].getSimpleName());
			for ( int i = 1 ; i < parametertypes.length ; i++ )
			{
				sig.append(",").append(
						longTypeNames	? parametertypes[i].getName()
										: parametertypes[i].getSimpleName());
			}
		}
		sig.append(")");
		return sig.toString();
	}

	public String format ()
	{
		StringBuilder formatted = new StringBuilder();
		format(formatted);
		return formatted.toString();
	}

	protected void format ( StringBuilder formatted )
	{
		if ( javadoc != null )
		{
			formatJavadoc(formatted);
		}
		formatComments(formatted);
		formatted.append("\t");
		formatName(formatted);
		formatParameterList(formatted);
		formatThrowsClause(formatted);
		formatted.append("\n\t{\n");
		formatBody(formatted);
		formatted.append("\t}\n");
	}

	protected void formatJavadoc ( StringBuilder formatted )
	{
		formatted.append(javadoc.format());
	}

	protected void formatComments ( StringBuilder formatted )
	{
		for ( Comment comment : comments )
		{
			formatted.append(comment.format()).append("\n");
		}
	}

	protected void formatName ( StringBuilder formatted )
	{
		if ( modifiers != 0 )
		{
			formatted.append(Modifier.toString(modifiers)).append(" ");
		}
		formatted.append(returntype.getSimpleName()).append(" ");
		formatted.append(methodname);
	}

	protected void formatParameterList ( StringBuilder formatted )
	{
		formatted.append("(");
		if ( parametertypes.length > 0 )
		{
			formatted.append(parametertypes[0].getSimpleName()).append(" ")
					.append(parameternames[0]);
			for ( int i = 1 ; i < parametertypes.length ; i++ )
			{
				formatted.append(", ")
						.append(parametertypes[i].getSimpleName()).append(" ")
						.append(parameternames[i]);
			}
		}
		formatted.append(")");
	}

	protected void formatThrowsClause ( StringBuilder formatted )
	{
		if ( exceptiontypes.length == 0 ) return;
		formatted.append(" throws ").append(exceptiontypes[0].getSimpleName());
		for ( int i = 1 ; i < exceptiontypes.length ; i++ )
		{
			formatted.append(", ").append(exceptiontypes[i].getSimpleName());
		}
	}

	protected void formatBody ( StringBuilder formatted )
	{
		for ( Statement statement : body )
		{
			formatted.append("\t\t").append(statement.format()).append("\n");
		}
	}

	public String getEncoding ()
	{
		String enc = "UTF-8";
		if ( StringUtil.containsNonLatin1Characters(methodname) ) return enc;
		for ( String name : parameternames )
		{
			if ( StringUtil.containsNonLatin1Characters(name) ) return enc;
		}
		for ( Comment comment : comments )
		{
			enc = comment.getEncoding();
			if ( enc.equals("UTF-8") ) return enc;
		}
		enc = javadoc.getEncoding();
		if ( enc.equals("UTF-8") ) return enc;
		for ( Statement statement : body )
		{
			enc = statement.getEncoding();
			if ( enc.equals("UTF-8") ) return enc;
		}
		return "ISO-8859-1";
	}
}
