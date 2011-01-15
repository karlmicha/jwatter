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

import org.jwatter.toolkit.shell.CommandExecutionException;
import org.jwatter.toolkit.shell.Shell;

public class HelpCommand extends AbstractCommand implements Command
{
	protected Shell shell;

	public HelpCommand ( Shell shell )
	{
		super(new String[]
		{
				"h", "help"
		}, new String[0], new String[]
		{
			"command"
		}, "print command usage information");
		this.shell = shell;
	}

	public void execute ( String... args ) throws CommandExecutionException
	{
		if ( args.length > 0 )
		{
			Command command = shell.getCommand(args[0]);
			if ( command == null )
			{
				throw new CommandExecutionException("no such command: "
						+ args[0]);
			}
			printHelp(command);
		}
		else
		{
			for ( Command command : shell.getCommands() )
			{
				printHelp(command);
			}
		}
	}

	protected void printHelp ( Command command )
	{
		String[] names = command.getNames();
		System.out.print(names[0]);
		for ( int i = 1 ; i < names.length ; i++ )
		{
			System.out.print(", " + names[i]);
		}
		for ( String arg : command.getArgumentNames() )
		{
			System.out.print(" " + arg.toUpperCase());
		}
		for ( String optarg : command.getOptionalArgumentNames() )
		{
			System.out.print(" [" + optarg.toUpperCase());
		}
		for ( int i = 0 ; i < command.getOptionalArgumentNames().length ; i++ )
		{
			System.out.print("] ");
		}
		if ( command.getOptionalArgumentNames().length == 0 )
		{
			System.out.print(" ");
		}
		System.out.println("- " + command.getDescription());
	}
}
