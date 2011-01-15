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
/**
 * 
 */
package org.jwatter.model;

import java.io.IOException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.logging.Logger;

import org.jwatter.util.FunctionalTestProperties;
import org.jwatter.browser.WebAutomationFramework;
import org.jwatter.test.WebFunctionalTestCase;
import org.jwatter.util.Callable;
import org.jwatter.util.Waiting;
import org.jwatter.util.Waiting.TimedOut;

/**
 * Base class for pages in the web application.
 * 
 * @author kschneider
 * 
 */
public abstract class PageImpl implements Page {

    // property that specifies the properties file for page properties
    protected static final String PAGE_PROPERTIESFILE_PROPERTY =
        Page.class.getName() + ".propertiesfile";

    protected static final String SYNC_PAGE_TIMEOUT_PROPERTY =
        Page.class.getName() + "syncPageTimeout";
	protected static final int DEFAULT_SYNC_PAGE_TIMEOUT = 30;

	protected static Logger logger = Logger
			.getLogger(WebFunctionalTestCase.class.getPackage().getName());

	protected static String propertiesFilename;
	protected static FunctionalTestProperties properties;

	/**
	 * Default timeout for browser sync operations, in seconds.
	 */
	protected static int syncPageTimeout;
    
    static {
		propertiesFilename = WebFunctionalTestCase.getRequiredProperty(
		        PAGE_PROPERTIESFILE_PROPERTY);
		logger.info("Loading page properties from " + propertiesFilename);
		try {
			properties = new FunctionalTestProperties(propertiesFilename);
		} catch( IOException e ) {
			throw new RuntimeException(e);
		}

		String syncBrowserTimeoutProperty = 
                WebFunctionalTestCase.getProperty(SYNC_PAGE_TIMEOUT_PROPERTY);
		syncPageTimeout = syncBrowserTimeoutProperty != null
		        ? Integer.parseInt(syncBrowserTimeoutProperty)
		        : DEFAULT_SYNC_PAGE_TIMEOUT;
	}

	protected WebAutomationFramework browserFramework;

	// the window or frame in which this page lives
	protected Window pageWindow;

	protected String url;

	protected PageImpl (WebAutomationFramework browserFramework,
	        Class<? extends Page> pageClass) throws Exception {
		this.browserFramework = browserFramework;
		this.url = getRequiredProperty("url", pageClass);
	}

	public static String getRequiredProperty (String propertyName)
			throws MissingResourceException {
		return properties.getRequiredProperty(propertyName);
	}

	public static String getRequiredProperty (String propertyName,
			Class<? extends Page> cls) throws MissingResourceException {
		return properties.getRequiredProperty(propertyName, cls);
	}

	/* (non-Javadoc)
     * @see org.jwatter.PageI#setWindow(org.jwatter.Window)
     */
	public void setWindow (Window window) {
	    this.pageWindow = window;
	}

	/* (non-Javadoc)
     * @see org.jwatter.PageI#getWindow()
     */
	public Window getWindow () {
	    return this.pageWindow;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.PageI#getTopLevelWindow()
	 */
	public Window getTopLevelWindow () {
	    return this.pageWindow instanceof Frame
	            ? ((Frame)this.pageWindow).getWindow() : this.pageWindow;
	}

	/* (non-Javadoc)
     * @see org.jwatter.PageI#onPage()
     */
	public boolean onPage () throws Exception {
	    URL browserUrl = new URL(browserFramework.getUrl());
	    return this.url.equals(browserUrl.getPath());
	}

	/* (non-Javadoc)
     * @see org.jwatter.PageI#isLoaded()
     */
	public boolean isLoaded () throws Exception {
        return browserFramework.getElementsByName("body").size() > 0;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.PageI#open(org.jwatter.util.Callable, int)
	 */
	public void open (Callable opener, int timeout) throws Exception {
	    opener.call();
	    this.switchTo(timeout);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.PageI#open(org.jwatter.util.Callable)
	 */
	public void open (Callable opener) throws TimedOut, Exception {
	    this.open(opener, syncPageTimeout);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.model.Page#switchTo(int)
	 */
	public void switchTo (int timeout) throws TimedOut, Exception {
        this.getWindow().setAsTarget();
        this.waitUntilLoaded(timeout);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.model.Page#switchTo()
	 */
	public void switchTo () throws TimedOut, Exception {
	    this.switchTo(syncPageTimeout);
	}

	/* (non-Javadoc)
     * @see org.jwatter.PageI#waitUntilLoaded(int)
     */
	public void waitUntilLoaded (int timeout) throws TimedOut, Exception {
	    final Page thisPage = this;
	    new Waiting(timeout * 1000, this.getClass().getSimpleName() +
	                " page still not loaded after " + timeout + "seconds") {
	        @Override
	        public boolean until () throws Exception {
	            return thisPage.isLoaded();
	        }
	    }.waitUntil();
	}

	/* (non-Javadoc)
     * @see org.jwatter.PageI#waitUntilLoaded()
     */
	public void waitUntilLoaded () throws TimedOut, Exception {
	    this.waitUntilLoaded(syncPageTimeout);
	}

	/* (non-Javadoc)
     * @see org.jwatter.PageI#leavePage(int)
     */
	public void leavePage (int timeout) throws TimedOut, Exception {
	    final Page thisPage = this;
	    new Waiting(timeout * 1000, "browser still on page after " + timeout +
	                " seconds") {

            @Override
            public boolean until () throws Exception {
                return !thisPage.onPage();
            }
        }.waitUntil();
	}

	/* (non-Javadoc)
     * @see org.jwatter.PageI#leavePage()
     */
	public void leavePage () throws TimedOut, Exception {
	    this.leavePage(syncPageTimeout);
	}

    /**
     * Attempts to open a new window, waits until the new window is open and sets the browser target
     * to the new window.
     * 
     * @param method
     *        if not null, a callable object that is called to open the new window; if null, it is
     *        assumed that the window has already been opened
     * @throws WindowException
     *         if no window was set for this page, or the window for this page is not the current
     *         browser window, or its name is not known
     * @throws TimedOut
     *         if the new window was not opened after the specified time
     * @throws Exception
     *         if an error occurs
     */
	protected void synchronizeNewWindow (final Callable method) throws
	        WindowException, TimedOut, Exception {
	    if (this.pageWindow == null) {
	        logger.severe("no window set for " + this.getClass().getSimpleName() + " page");
	        throw new WindowException("window is not set for this page");
	    }
	    this.pageWindow.synchronizeNewWindow(method);
	}

    /**
     * Attempts to open a new window, waits until the new window is open and sets the browser target
     * to the new window.
     * 
     * @param method
     *        if not null, a callable object that is called to open the new window; if null, it is
     *        assumed that the window has already been opened
     * @param timeout
     *        the maximum number of seconds to wait for the new window to open, or 0 to wait forever
     * @throws WindowException
     *         if no window was set for this page, or the window for this page is not the current
     *         browser window, or its name is not known
     * @throws TimedOut
     *         if the new window was not opened after the specified time
     * @throws Exception
     *         if an error occurs
     */
	protected void synchronizeNewWindow(final Callable method, int timeout) throws
	        WindowException, TimedOut, Exception {
        if (this.pageWindow == null) {
            logger.severe("no window set for " + this.getClass().getSimpleName() + " page");
            throw new WindowException("window is not set for this page");
        }
	    this.pageWindow.synchronizeNewWindow(method, timeout);
	}
}
