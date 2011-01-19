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
package org.jwatter.toolkit.control;

import org.jwatter.browser.WebAutomationFramework;
import org.jwatter.toolkit.control.request.BrowserControllerRequest;
import org.jwatter.toolkit.control.request.BrowserRequest;
import org.jwatter.toolkit.control.request.Request;
import org.jwatter.toolkit.control.request.StatusChangeListener;

public class BrowserController implements Runnable
{
	protected WebAutomationFramework browser;
	protected RequestBuffer requestBuffer;
	protected Thread browserControllerThread;
	protected boolean running;

	public BrowserController ( WebAutomationFramework browser,
			RequestBuffer requestBuffer )
	{
		this.browser = browser;
		this.requestBuffer = requestBuffer;
		browserControllerThread = null;
		running = false;
	}

	public void run ()
	{
		browserControllerThread = Thread.currentThread();
		synchronized ( this )
		{
			// notify other threads that wait for initialization to complete
			this.notifyAll();
		}
		System.out.println("Browser controller is running");
		running = true;
		while ( running )
		{
			Request request = null;
			try
			{
				request = requestBuffer.retrieve();
				if ( request instanceof BrowserControllerRequest )
				{
					((BrowserControllerRequest) request).handle(this);
					request.complete();
				}
				else if ( request instanceof BrowserRequest )
				{
					if ( !browser.isBrowserOpen() )
					{
						throw new BrowserControllerException(request.getClass()
								.getSimpleName()
								+ " failed: No browser running");
					}
					((BrowserRequest) request).handle(browser);
					request.complete();
				}
				else
				{
					throw new BrowserControllerException(
							"Unknown request type (can only handle browser requests and browser controller requests");
				}
			}
			catch ( InterruptedException e )
			{
				running = false;
				System.out.println("["
						+ BrowserController.class.getSimpleName()
						+ "] Interrupted");
			}
			catch ( BrowserControllerException e )
			{
				System.out.println(e.toString());
			}
			catch ( Exception e )
			{
				System.out.println(e.toString());
			}
			finally
			{
				if ( request != null )
				{
					synchronized ( request )
					{
						request.notifyAll();
					}
				}
			}
		}
		System.out.println("Browser controller terminated");
	}

	public synchronized void waitForInitializationToComplete ()
	{
		while ( browserControllerThread == null )
		{
			try
			{
				this.wait();
			}
			catch ( InterruptedException e )
			{
			}
		}
	}

	public void stop () throws BrowserControllerException
	{
		running = false;
		try
		{
			if ( browser.isBrowserOpen() )
			{
				closeBrowser();
			}
		}
		catch ( Exception e )
		{
			throw new BrowserControllerException(e);
		}
	}

	public void createBrowser ( String profileName )
			throws BrowserControllerException
	{
		try
		{
			if ( browser.isBrowserOpen() )
			{
				throw new BrowserControllerException("Browser is already open");
			}
			if ( profileName != null && !profileName.equals(""))
			{
				System.out.print("Creating browser with " + profileName
						+ " profile...");
				browser.createBrowser(profileName);
			}
			else
			{
				System.out.print("Creating browser...");
				browser.createBrowser();
			}
			System.out.println();
		}
		catch ( BrowserControllerException e )
		{
			throw e;
		}
		catch ( Exception e )
		{
			throw new BrowserControllerException(e);
		}
	}

	public void closeBrowser () throws BrowserControllerException
	{
		try
		{
			if ( !browser.isBrowserOpen() )
			{
				throw new BrowserControllerException("No browser open");
			}
			System.out.print("Closing browser...");
			browser.closeBrowser();
			System.out.println();
		}
		catch ( BrowserControllerException e )
		{
			throw e;
		}
		catch ( Exception e )
		{
			throw new BrowserControllerException(e);
		}
	}

	public void send ( Request request ) throws BrowserControllerException
	{
		send(request, null);
	}

	public void send ( Request request, StatusChangeListener changeListener )
			throws BrowserControllerException
	{
		send(request, 0, changeListener);
	}

	public void send ( Request request, final long timeout )
			throws BrowserControllerException
	{
		send(request, timeout, null);
	}

	public void send ( Request request, final long timeout,
			StatusChangeListener changeListener )
			throws BrowserControllerException
	{
		if ( browserControllerThread == null )
		{
			throw new BrowserControllerException(
					"Browser controller thread has not been created");
		}
		if ( !browserControllerThread.isAlive() )
		{
			throw new BrowserControllerException(
					"Browser controller thread is not running");
		}
		request.setChangeListener(changeListener);
		synchronized ( request )
		{
			final Thread requestingThread = Thread.currentThread();
			Thread timeoutGuard = null;
			if ( timeout > 0 )
			{
				timeoutGuard = new Thread()
				{
					public void run ()
					{
						try
						{
							Thread.sleep(timeout);
							requestingThread.interrupt();
						}
						catch ( InterruptedException e )
						{
						}
					}
				};
				timeoutGuard.start();
			}
			try
			{
				requestBuffer.submit(request);
				request.wait();
			}
			catch ( InterruptedException e )
			{
				throw new BrowserControllerException("Command timed out");
			}
			finally
			{
				if ( timeoutGuard != null )
				{
					timeoutGuard.interrupt();
				}
			}
		}
	}
}
