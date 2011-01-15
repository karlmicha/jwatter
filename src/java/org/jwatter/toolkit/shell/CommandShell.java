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
package org.jwatter.toolkit.shell;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jwatter.toolkit.shell.commands.Command;

public class CommandShell implements Shell, UserCommunicationManager
{
	protected Map<String, Command> commands;
	protected BufferedReader inputReader;
	protected boolean isRunning;

	public CommandShell ()
	{
		commands = new HashMap<String, Command>();
		inputReader = new BufferedReader(new InputStreamReader(System.in));
		isRunning = false;
	}

	public void addCommand ( Command command )
	{
		command.register(commands);
	}

	public Collection<Command> getCommands ()
	{
		return new HashSet<Command>(commands.values());
	}

	public Command getCommand ( String name )
	{
		return commands.get(name);
	}

	public void commandLoop ()
	{
		isRunning = true;
		while ( isRunning )
		{
			String commandLine;
			try
			{
				commandLine = getLine();
				if ( commandLine == null )
				{
					isRunning = false;
					continue;
				}
				if ( commandLine.equals("") ) continue;

				String[] tokens = commandLine.split("\\s+");
				String commandname = tokens[0].toLowerCase();
				Command command = commands.get(commandname);
				if ( command == null )
				{
					throw new Exception("No such command: " + commandname);
				}
				if ( tokens.length - 1 < command.getArgumentNames().length )
				{
					throw new Exception("Not enough arguments (see help "
							+ commandname + ")");
				}
				if ( tokens.length - 1 > command.getArgumentNames().length
						+ command.getOptionalArgumentNames().length )
				{
					throw new Exception("Too many arguments (see help "
							+ commandname + ")");
				}

				String[] arguments = new String[tokens.length - 1];
				System.arraycopy(tokens, 1, arguments, 0, arguments.length);
				command.execute(arguments);
			}
			catch ( CommandExecutionException e )
			{
				System.out.println(e.getMessage());
			}
			catch ( RuntimeException e )
			{
				e.printStackTrace();
			}
			catch ( Exception e )
			{
				e.printStackTrace(System.out);
			}
		}
	}

	public void exit ()
	{
		isRunning = false;
	}

	public boolean confirm ( String message ) throws IOException
	{
		System.out.print(message + " ");
		String reply = inputReader.readLine();
		if ( reply == null ) return false;
		reply = reply.trim().toLowerCase();
		return reply.equals("y") || reply.equals("yes");
	}

	protected String getLine () throws IOException
	{
		System.out.print("> ");
		String line = inputReader.readLine();
		if ( line == null ) return null;
		return line.trim();
	}
}
