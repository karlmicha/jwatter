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
package org.jwatter.toolkit.shell.commands;

import java.util.Map;

public abstract class AbstractCommand implements Command
{
	protected String[] names;
	protected String[] argNames;
	protected String[] optArgNames;
	protected String description;

	protected AbstractCommand ( String name, String description )
	{
		this(new String[]
		{
			name
		}, description);
		if ( name == null )
		{
			throw new IllegalArgumentException("name cannot be null");
		}
	}

	protected AbstractCommand ( String[] names, String description )
	{
		this(names, null, null, description);
	}

	protected AbstractCommand ( String[] names, String[] argumentNames,
			String[] optionalArgumentNames, String description )
	{
		if ( names == null )
		{
			throw new IllegalArgumentException("names cannot be null");
		}
		if ( names.length == 0 )
		{
			throw new IllegalArgumentException("names cannot be empty");
		}
		this.names = names;
		this.argNames = argumentNames == null ? new String[0] : argumentNames;
		this.optArgNames =
				optionalArgumentNames == null	? new String[0]
												: optionalArgumentNames;
		this.description = description;
	}

	public String[] getNames ()
	{
		return names;
	}

	public String[] getArgumentNames ()
	{
		return argNames;
	}

	public String[] getOptionalArgumentNames ()
	{
		return optArgNames;
	}

	public String getDescription ()
	{
		return description;
	}

	public void register ( Map<String, Command> commands )
	{
		for ( String name : names )
		{
			if ( commands.containsKey(name) )
			{
				throw new Error("Duplicate command name " + name + ": "
						+ commands.get(name).getClass().getSimpleName() + ", "
						+ this.getClass().getSimpleName());
			}
			commands.put(name, this);
		}
	}

}
