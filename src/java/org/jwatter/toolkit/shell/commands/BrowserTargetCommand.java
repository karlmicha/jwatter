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
import org.jwatter.toolkit.control.request.BrowserTargetRequest;
import org.jwatter.toolkit.control.request.Request;
import org.jwatter.toolkit.shell.CommandExecutionException;

public class BrowserTargetCommand extends AbstractCommand implements Command
{
	protected BrowserController browserController;

	public BrowserTargetCommand ( BrowserController browserController )
	{
		super(
				new String[]
				{
						"t", "target"
				},
				new String[]
				{
					"type"
				},
				new String[]
				{
					"address"
				},
				"set target of browser control; valid arguments are FRAME {0|1|2|...}, FRAME name[.name...], WINDOW name, DEFAULT");
		this.browserController = browserController;
	}

	public void execute ( String... args ) throws CommandExecutionException
	{
		Request request;
		if ( args[0].toLowerCase().equals("frame") )
		{
			if ( args.length < 2 )
			{
				throw new CommandExecutionException(
						"no frame index or address specified");
			}
			try
			{
				request = new BrowserTargetRequest(Integer.parseInt(args[1]));
			}
			catch ( NumberFormatException e )
			{
				request =
						new BrowserTargetRequest(BrowserTargetRequest.FRAME,
								args[1]);
			}
		}
		else if ( args[0].toLowerCase().equals("window") )
		{
			if ( args.length < 2 )
			{
				throw new CommandExecutionException("no window name specified");
			}
			request =
					new BrowserTargetRequest(BrowserTargetRequest.WINDOW,
							args[1]);
		}
		else if ( args[0].toLowerCase().equals("default") )
		{
			if ( args.length > 1 )
			{
				throw new CommandExecutionException(
						"default target takes no arguments");
			}
			request =
					new BrowserTargetRequest(BrowserTargetRequest.WINDOW, null);
		}
		else
		{
			throw new CommandExecutionException("unknown browser target type "
					+ args[0]);
		}
		try
		{
			browserController.send(request);
		}
		catch ( BrowserControllerException e )
		{
			throw new CommandExecutionException(e);
		}
	}

}
