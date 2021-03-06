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

import org.jwatter.toolkit.control.BrowserController;
import org.jwatter.toolkit.control.BrowserControllerException;
import org.jwatter.toolkit.control.request.CloseBrowserRequest;
import org.jwatter.toolkit.shell.CommandExecutionException;

public class CloseBrowserCommand extends AbstractCommand implements Command
{
	protected BrowserController controller;

	public CloseBrowserCommand ( BrowserController controller )
	{
		super("close", "close the current browser");
		this.controller = controller;
	}

	public void execute ( String... args ) throws CommandExecutionException
	{
		try
		{
			controller.send(new CloseBrowserRequest());
		}
		catch ( BrowserControllerException e )
		{
			throw new CommandExecutionException(e);
		}
	}
}
