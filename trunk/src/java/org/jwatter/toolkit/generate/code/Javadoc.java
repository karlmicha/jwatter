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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.jwatter.util.StringUtil;

public class Javadoc extends PrintWriter implements Formattable
{
	public Javadoc ()
	{
		super(new StringWriter());
	}

	public Javadoc ( String text )
	{
		this();
		print(text);
	}

	public boolean endsWithNewline ()
	{
		StringBuffer buffer = ((StringWriter) out).getBuffer();
		return buffer.length() > 0
				&& buffer.charAt(buffer.length() - 1) == '\n';
	}

	public void ensureNewline ()
	{
		if ( !endsWithNewline() )
		{
			println();
		}
	}

	public String format ()
	{
		return "\t/**\n\t * "
				+ ((StringWriter) out).toString().trim().replaceAll("\n",
						"\n\t * ") + "\n\t */\n";
	}

	public String getEncoding ()
	{
		return StringUtil.getEncoding(((StringWriter) out).toString());
	}

}
