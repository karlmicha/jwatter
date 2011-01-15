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

public class ExitCommand extends AbstractCommand implements Command
{
	protected Shell shell;

	public ExitCommand ( Shell shell )
	{
		super(new String[]
		{
				"q", "quit", "exit"
		}, "quit the shell");
		this.shell = shell;
	}

	public void execute ( String... args ) throws CommandExecutionException
	{
		shell.exit();
	}

}
