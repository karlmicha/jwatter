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
package org.jwatter.model;

import org.jwatter.util.Callable;
import org.jwatter.util.Waiting.TimedOut;

/**
 * A page knows which elements it contains and provides methods to fill in forms, click buttons,
 * follow links, inspect text, etc. It is not the responsibility of a page to load itself in a
 * browser, this is done by the tests. A page only provides methods to access the elements contained
 * in it.
 * 
 * @author kschneider
 * 
 */
public interface Page {

    /**
     * Tells this page in which window or frame it lives. If this page is displayed in a frame, this
     * method must be called with the {@link Frame} instance that contains this page, not the
     * {@link Window} instance.
     * 
     * @param window
     *        the window or frame in which this page lives
     */
    public void setWindow (Window window);

    /**
     * Returns the window or frame in which this page lives.
     * 
     * @return the window or frame in which this page lives
     */
    public Window getWindow ();

    /**
     * Returns the window in which this page lives. If this page lives in a frame, returns the
     * window that contains the frame.
     * 
     * @return the window in which this page lives
     */
    public Window getTopLevelWindow ();

    /**
     * Checks if the browser is on the page represented by this page object.
     * 
     * @return true if the browser is on the page represented by this page object, else false
     * @throws Exception
     *         if an error occurs
     */
    public boolean onPage ()
            throws Exception;

    /**
     * Checks if the page represented by this page object has finished loading. By default, this
     * method checks whether the body element exists in the DOM tree. A subclass can override this
     * method to define more specific loading conditions.
     * 
     * @return true if the page has finished loading
     * @throws Exception
     *         if an error occurs
     */
    public boolean isLoaded ()
            throws Exception;

    /**
     * Opens the page represented by this page object, using the specified opener, sets the browser
     * target to the frame or window that displays the page and waits until the page is loaded.
     * 
     * @param opener
     *        used to open the page
     * @param timeout
     *        the maximum number of seconds to wait for the page to load, or 0 to wait forever
     * @param TimedOut
     *        if the page has not finished loading after the specified time
     * @throws Exception
     *         if an error occurs
     */
    public void open (Callable opener, int timeout)
            throws TimedOut, Exception;

    /**
     * Opens the page represented by this page object, using the specified opener, sets the browser
     * target to the frame or window that displays the page and waits until the page is loaded. The
     * timeout is set to the default timeout. This can be overridden in the test.properties file.
     * 
     * @param opener
     *        used to open the page
     * @param TimedOut
     *        if the page has not finished loading after the specified time
     * @throws Exception
     *         if an error occurs
     */
    public void open (Callable opener)
            throws Exception;

    /**
     * Waits until the page represented by this page object has finished loading.
     * 
     * @param timeout
     *        the maximum number of seconds to wait, or 0 to wait forever
     * @throws TimedOut
     *         if the page has not finished loading after the specified time
     * @throws Exception
     *         if an error occurs
     */
    public void waitUntilLoaded (int timeout)
            throws TimedOut, Exception;

    /**
     * Waits until the page represented by this page object has finished loading. The timeout is set
     * to the default timeout. This can be overridden in the test.properties file.
     * 
     * @throws TimedOut
     *         if the page has not finished loading after the specified time
     * @throws Exception
     *         if an error occurs
     */
    public void waitUntilLoaded ()
            throws TimedOut, Exception;

    /**
     * Switches the browser target to the window that contains the page represented by this page
     * object and waits until the page has finished loading.
     * 
     * @param timeout
     *        the maximum number of seconds to wait, or 0 to wait forever
     * @throws TimedOut
     *         if the page has not finished loading after the specified time
     * @throws Exception
     *         if an error occurs
     */
    public void switchTo (int timeout)
            throws TimedOut, Exception;

    /**
     * Switches the browser target to the window that contains the page represented by this page
     * object and waits until the page has finished loading. The timeout is set to the default
     * timeout. This can be overridden in the test.properties file.
     * 
     * @throws TimedOut
     *         if the page has not finished loading after the specified time
     * @throws Exception
     *         if an error occurs
     */
    public void switchTo ()
            throws TimedOut, Exception;

    /**
     * Wait until the browser has left the page represented by this page object.
     * 
     * @param timeout
     *        the maximum number of seconds to wait, or 0 to wait forever
     * @throws TimedOut
     *         if the browser has not left the current page after the specified time
     * @throws Exception
     *         if an error occurs
     */
    public void leavePage (int timeout)
            throws TimedOut, Exception;

    /**
     * Wait until the browser has left the current page. The timeout is set to the default timeout.
     * This can be overridden in the test.properties file.
     * 
     * @throws TimedOut
     *         if the browser has not left the current page after the default time has passed
     * @throws Exception
     *         if an error occurs
     */
    public void leavePage ()
            throws TimedOut, Exception;

}
