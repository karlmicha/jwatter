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
package org.jwatter.toolkit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jwatter.util.FunctionalTestProperties;
import org.jwatter.browser.FirefoxWebAutomationFramework;
import org.jwatter.browser.WebAutomationFramework;
import org.jwatter.toolkit.control.BrowserController;
import org.jwatter.toolkit.control.BrowserControllerException;
import org.jwatter.toolkit.control.RequestBuffer;
import org.jwatter.toolkit.control.request.BrowserInfo;
import org.jwatter.toolkit.control.request.CreateBrowserRequest;
import org.jwatter.toolkit.control.request.ExitRequest;
import org.jwatter.toolkit.control.request.OpenUrlRequest;
import org.jwatter.toolkit.generate.CodeGenerator;
import org.jwatter.toolkit.generate.code.PropertiesFile;
import org.jwatter.toolkit.shell.CommandShell;
import org.jwatter.toolkit.shell.Shell;
import org.jwatter.toolkit.shell.commands.BrowserHistoryBackCommand;
import org.jwatter.toolkit.shell.commands.BrowserHistoryForwardCommand;
import org.jwatter.toolkit.shell.commands.BrowserTargetCommand;
import org.jwatter.toolkit.shell.commands.BrowserWindowCommand;
import org.jwatter.toolkit.shell.commands.CloseBrowserCommand;
import org.jwatter.toolkit.shell.commands.CreateBrowserCommand;
import org.jwatter.toolkit.shell.commands.ExitCommand;
import org.jwatter.toolkit.shell.commands.GenerateCodeCommand;
import org.jwatter.toolkit.shell.commands.HelpCommand;
import org.jwatter.toolkit.shell.commands.InspectElementCommand;
import org.jwatter.toolkit.shell.commands.ListCommand;
import org.jwatter.toolkit.shell.commands.OpenUrlCommand;

public class Toolkit
{
	protected static final String TOOLKIT_NAME = "Web Test Automation Toolkit";
	protected static final String TOOLKIT_VERSION = "0.1";

	protected static final String usageMessage =
			"Usage: " + Toolkit.class.getSimpleName()
					+ " PROPERTIES_FILE [START_URL]";

	protected static String toolkitPropertiesFilename;
	protected static String startUrl = null;

	protected Toolkit ( BrowserController browserController,
			String browserProfileName, Shell commandShell, String startUrl )
			throws BrowserControllerException, InterruptedException
	{
		Thread browserControllerThread = new Thread(browserController);
		browserControllerThread.start();
		browserController.waitForInitializationToComplete();
		if ( !browserControllerThread.isAlive() )
		{
			System.out.println("Failed to start browser controller thread");
			return;
		}
		System.out.println("Browser controller thread started");

		if ( startUrl != null )
		{
			browserController
					.send(browserProfileName != null ? new CreateBrowserRequest(
															browserProfileName)
													: new CreateBrowserRequest());
			browserController.send(new OpenUrlRequest(startUrl));
		}

		commandShell.commandLoop();

		browserController.send(new ExitRequest());
		System.out
				.println("Waiting for browser controller thread to terminate");
		browserControllerThread.join();
	}

	public static void launchToolkit (
			FunctionalTestProperties toolkitProperties, String startUrl )
			throws IOException, BrowserControllerException,
			InterruptedException
	{
		String actorPropertiesFilename =
				toolkitProperties.getRequiredProperty("actorPropertiesFile",
						Toolkit.class);
		String outputDir =
				toolkitProperties.getRequiredProperty("outputDir",
						Toolkit.class);
		String basePackageName =
				toolkitProperties.getRequiredProperty("basePackageName",
						Toolkit.class);
		String browserProfileName =
				toolkitProperties.getProperty("browserProfileName",
						Toolkit.class);
		if ( startUrl == null )
		{
			startUrl =
					toolkitProperties.getProperty("startupUrl", Toolkit.class);
		}

		// Create object graph
		WebAutomationFramework browser = new FirefoxWebAutomationFramework();
		BrowserController browserController =
				new BrowserController(browser, new RequestBuffer());

		CommandShell commandShell = new CommandShell();

		CodeGenerator codeGenerator =
				new CodeGenerator(browserController, commandShell,
						new BrowserInfo(), new PropertiesFile(
								actorPropertiesFilename), outputDir,
						basePackageName);

		commandShell.addCommand(new CreateBrowserCommand(browserController));
		commandShell.addCommand(new CloseBrowserCommand(browserController));
		commandShell.addCommand(new OpenUrlCommand(browserController));
		commandShell
				.addCommand(new BrowserHistoryBackCommand(browserController));
		commandShell.addCommand(new BrowserHistoryForwardCommand(
				browserController));
		commandShell.addCommand(new BrowserTargetCommand(browserController));
		commandShell.addCommand(new BrowserWindowCommand(browserController));
		commandShell.addCommand(new ListCommand(browserController));
		commandShell.addCommand(new InspectElementCommand(browserController));
		commandShell.addCommand(new GenerateCodeCommand(codeGenerator));
		commandShell.addCommand(new ExitCommand(commandShell));
		commandShell.addCommand(new HelpCommand((CommandShell) commandShell));

		new Toolkit(browserController, browserProfileName, commandShell,
				startUrl);
	}

	public static void main ( String[] args )
	{
		try
		{
			parseCommandline(args);

			System.out.println(TOOLKIT_NAME + " Version " + TOOLKIT_VERSION);
			System.out.println();

			launchToolkit(new FunctionalTestProperties(
					toolkitPropertiesFilename), startUrl);
		}
		catch ( RuntimeException e )
		{
			e.printStackTrace();
			System.exit(1);
		}
		catch ( UsageException e )
		{
			System.err.println(e.getMessage());
			System.exit(2);
		}
		catch ( Exception e )
		{
			System.err.println(e.getMessage() != null ? e.getMessage() : e
					.toString());
			System.exit(1);
		}
	}

	protected static void parseCommandline ( String[] args )
			throws UsageException
	{
		List<String> arguments = new ArrayList<String>();
		int argp = 0;
		while ( argp < args.length )
		{
			String arg = args[argp++];
			if ( arg.equals("-h") || arg.equals("-help")
					|| arg.equals("--help") )
			{
				throw new UsageException();
			}
			else if ( arg.equals("-V") || arg.equals("-version")
					|| arg.equals("--version") )
			{
	            System.out.println(TOOLKIT_NAME + " Version " + TOOLKIT_VERSION);
				System.exit(0);
			}
			else if ( arg.startsWith("-") )
			{
				throw new UsageException("unknown option: " + arg);
			}
			else
			{
				arguments.add(arg);
			}
		}
		if ( arguments.size() < 1 )
		{
			throw new UsageException("no properties file specified");
		}
		if ( arguments.size() > 2 )
		{
			throw new UsageException("too many arguments");
		}
		toolkitPropertiesFilename = arguments.get(0);
		if ( arguments.size() > 1 )
		{
			startUrl = arguments.get(1);
		}
	}
}
