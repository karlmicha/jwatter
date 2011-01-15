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

import java.util.ArrayList;
import java.util.List;

import org.jwatter.toolkit.control.BrowserController;
import org.jwatter.toolkit.control.BrowserControllerException;
import org.jwatter.toolkit.control.request.BrowserTargetRequest;
import org.jwatter.toolkit.control.request.GetWindowNamesRequest;
import org.jwatter.toolkit.shell.CommandExecutionException;

public class BrowserWindowCommand extends AbstractCommand implements Command
{
	protected BrowserController browserController;

	public BrowserWindowCommand ( BrowserController browserController )
	{
		super(new String[]
		{
				"w", "window"
		}, null, new String[]
		{
			"window_name"
		}, "list window names or set target to a window");
		this.browserController = browserController;
	}

	public void execute ( String... args ) throws CommandExecutionException
	{
		try
		{
			if ( args.length == 0 )
			{
				List<String> windownames = getWindowNames();
				for ( int i = 1 ; i < windownames.size() ; i++ )
				{
					System.out.println((windownames.get(i).equals(
							windownames.get(0)) ? "*" : "")
							+ i + ": " + windownames.get(i));
				}
			}
			else
			{
				String windowname;
				try
				{
					int windowindex = Integer.parseInt(args[0]);
					try
					{
						windowname = getWindowNames().get(windowindex);
					}
					catch ( IndexOutOfBoundsException e )
					{
						throw new CommandExecutionException(
								"Illegal window index: " + windowindex);
					}
				}
				catch ( NumberFormatException e )
				{
					windowname = args[0];
				}
				browserController.send(new BrowserTargetRequest(
						BrowserTargetRequest.WINDOW, windowname));
			}
		}
		catch ( BrowserControllerException e )
		{
			throw new CommandExecutionException(e);
		}
	}

	protected List<String> getWindowNames () throws CommandExecutionException
	{
		try
		{
			List<String> windownames = new ArrayList<String>();
			browserController.send(new GetWindowNamesRequest(windownames));
			if ( windownames.size() == 0 )
			{
				throw new CommandExecutionException(
						"Could not find and windows");
			}
			return windownames;
		}
		catch ( BrowserControllerException e )
		{
			throw new CommandExecutionException(e);
		}
	}

}
