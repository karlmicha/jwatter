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

import org.jwatter.html.Element;
import org.jwatter.html.WebDriverElement;
import org.jwatter.toolkit.control.BrowserController;
import org.jwatter.toolkit.control.BrowserControllerException;
import org.jwatter.toolkit.control.request.GetHtmlElementsRequest;
import org.jwatter.toolkit.shell.CommandExecutionException;

public class InspectElementCommand extends AbstractCommand implements Command
{
	protected BrowserController browserController;
	protected Element element;

	public InspectElementCommand ( BrowserController browserController )
	{
		super(
				new String[]
				{
						"i", "inspect"
				},
				new String[0],
				new String[]
				{
						"element_name", "index"
				},
				"display attributes and children of an HTML element; "
						+ "if ELEMENT_NAME is '>', display the specified child of the current element; "
						+ "if ELEMENT_NAME is '<', display the parent of the current element; "
						+ "if no element is specified, display the current element");
		this.browserController = browserController;
		this.element = null;
	}

	public void execute ( String... args ) throws CommandExecutionException
	{
		if ( args.length == 0 )
		{
			if ( element == null )
			{
				throw new CommandExecutionException("No element selected");
			}
			element.inspect(System.out);
		}
		else if ( args[0].equals("<") )
		{
			Element parent = element.getParent();
			if ( parent == null )
			{
				throw new CommandExecutionException("At root");
			}
			element = parent;
			element.inspect(System.out);
		}
		else if ( args[0].equals(">") )
		{
			if ( args.length < 2 )
			{
				throw new CommandExecutionException(
						"Usage: inspect > CHILD_INDEX");
			}
			int childindex = Integer.parseInt(args[1]);
			if ( childindex < 1 )
			{
				throw new CommandExecutionException("Invalid child index: "
						+ args[1]);
			}
			Element child = element.getChild(childindex);
			if ( child == null )
			{
				throw new CommandExecutionException("No such child element: "
						+ childindex);
			}
			element = child;
			element.inspect(System.out);
		}
		else
		{
			String elementname = args[0];
			int elementindex = 0;
			if ( args.length > 1 )
			{
				try
				{
					elementindex = Integer.parseInt(args[1]);
				}
				catch ( NumberFormatException e )
				{
					throw new CommandExecutionException(
							"Invalid element index: " + args[1]);
				}
				if ( elementindex < 1 )
				{
					throw new CommandExecutionException(
							"Invalid element index: " + args[1]);
				}
			}
			List<Element> elements = new ArrayList<Element>();
			try
			{
				browserController.send(new GetHtmlElementsRequest(elementname,
						elements));
			}
			catch ( BrowserControllerException e )
			{
				throw new CommandExecutionException(e);
			}
			if ( elements.size() == 0 )
			{
				throw new CommandExecutionException("No such element: "
						+ elementname);
			}
			if ( elements.size() < elementindex )
			{
				throw new CommandExecutionException("No such element: "
						+ elementname + "[" + elementindex + "]");
			}
			if ( elements.size() > 1 && elementindex == 0 )
			{
				System.out.println(elementname + " is ambiguous:");
				int i = 0;
				for ( Element e : elements )
				{
					System.out.println(++i
							+ ": "
							+ e.toString()
							+ " \""
							+ e.getText(WebDriverElement.TRUNCATE_ELEMENT_TEXT,
									true) + "\"");
				}
			}
			else
			{
				element = elements.get(elementindex > 0 ? elementindex - 1 : 0);
				element.inspect(System.out);
			}
		}
	}
}
