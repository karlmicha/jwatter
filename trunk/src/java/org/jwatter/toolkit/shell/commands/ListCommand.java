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
import org.jwatter.toolkit.control.BrowserController;
import org.jwatter.toolkit.control.BrowserControllerException;
import org.jwatter.toolkit.control.request.GetHtmlElementsRequest;
import org.jwatter.toolkit.shell.CommandExecutionException;

public class ListCommand extends AbstractCommand implements Command
{
	public static final int TRUNCATE_ELEMENT_TEXT = 60;

	protected BrowserController controller;

	public ListCommand ( BrowserController controller )
	{
		super(new String[]
		{
				"l", "list"
		}, new String[]
		{
			"element_name"
		}, new String[0], "list HTML elements");
		this.controller = controller;
	}

	public void execute ( String... args ) throws CommandExecutionException
	{
		try
		{
			List<Element> elements = new ArrayList<Element>();
			controller.send(new GetHtmlElementsRequest(args[0], elements));
			for ( Element element : elements )
			{
				System.out.println(element.toString() + " \""
						+ element.getText(TRUNCATE_ELEMENT_TEXT, true) + "\"");
			}
		}
		catch ( BrowserControllerException e )
		{
			throw new CommandExecutionException(e);
		}
	}
}
