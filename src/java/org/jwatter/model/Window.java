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

import java.util.Collection;
import java.util.List;

import org.jwatter.util.Callable;
import org.jwatter.util.Waiting.TimedOut;

/**
 * A window represents a set of pages. A window has methods to open a specific page, check if a
 * specific page is currently displayed, and query the page that is currrently displayed.
 * 
 * Each window has a name, which is the window handle used by the web automation framework. Since
 * the browser window may not exist at the time when the window instance is created, there is a
 * method to tell the window instance to use the current window handle as its name. The window name
 * can be used to set the browser target to a window.
 */
public interface Window {

    /**
     * Adds the specified frame to this window and sets the parent of the specified frame to this
     * window. If this window has a field with the same class as the frame, it sets this field to
     * the given frame. If this window already contains the given frame, this method does nothing.
     * 
     * @param frame
     *        the frame to add to this window
     */
    public void addFrame (Frame frame);

    /**
     * Checks if this window contains a page of the given class.
     * 
     * @param pageClass
     *        the page class to look for
     * @return true if this window contains a page of the specified class, else false
     */
    public boolean containsPage (Class<? extends Page> pageClass);

    /**
     * Checks if this window contains a frame with the given class.
     * 
     * @param frameClass
     *        the frame class to look for
     * @return true if this window contains a child frame of the specified class, else false
     */
    public boolean containsFrame (Class<? extends Frame> frameClass);

    /**
     * Returns a list of all pages in this window.
     * 
     * @return a list of all pages in this window
     */
    public List<Page> getPages ();

    /**
     * Returns a collection of all page classes in this window.
     * 
     * @return a collection of all page classes in this window
     */
    public Collection<Class<? extends Page>> getPageClasses ();

    /**
     * Returns the page with the given class in this window.
     * 
     * @param pageClass
     *        the class of the page to look for
     * @return the page with the given class in this window
     * @throws NoSuchPageException
     *         if this window does not contain a page with the given class
     */
    public <T extends Page> T getPage (Class<T> pageClass)
            throws NoSuchPageException;

    /**
     * Returns the frame with the specified class in this window.
     * 
     * @param frameClass
     *        the class of the frame to look for
     * @return the frame with the given class in this window
     * @throws NoSuchFrameException
     *         if this window does not contain a frame with the given class
     */
    public Frame getFrame (Class<? extends Frame> frameClass)
            throws NoSuchFrameException;

    /**
     * Returns the frame with the specified name in this window.
     * 
     * @param name
     *        the name of the frame to look for
     * @return the frame with the given name in this window
     * @throws NoSuchFrameException
     *         if this window does not contain a frame with the given name
     */
    public Frame getFrame (String name)
            throws NoSuchFrameException;

    /**
     * Sets the name of this window to the name of the current browser window.
     * 
     * @throws WindowException
     *         if no browser window is open
     * @throws Exception
     *         if an error occurs
     */
    public void setName ()
            throws WindowException, Exception;

    /**
     * Returns the name of this window.
     * 
     * @return the name of this window
     */
    public String getName ();

    /**
     * Sets the browser target to this window.
     * 
     * @throws WindowException
     *         if the name of this window is not known
     * @throws Exception
     *         if an error occurs
     */
    public void setAsTarget ()
            throws WindowException, Exception;

    /**
     * Checks if this window is currently set as the browser target.
     * 
     * @return true if this window is the browser target, else false
     * @throws WindowException
     *         if the name of this window is not known
     * @throws Exception
     *         if an error occurs
     */
    public boolean isTarget ()
            throws WindowException, Exception;

    /**
     * Checks if this window or one of the frames in this window is set as the browser target.
     * 
     * @return true if this window or one of its frames is the browser target, else false
     * @throws WindowException
     *         if the name of this window is not known
     * @throws Exception
     *         if an error occurs
     */
    public boolean containsTarget ()
            throws WindowException, Exception;

    /**
     * Tells this window which of its frames is the browser target. This method should only be
     * called by a frame on its parent window if that frame is set as browser target.
     * 
     * @param address
     *        the address of the frame in this window that is the browser target, or null if no
     *        frame in this window is the browser target
     */
    public void setTargetFrameAddress (String address);

    /**
     * Returns the address of the frame in this window that is the browser target. This is not
     * meaningful if the browser target is on a different window.
     * 
     * This method is called by a frame on its parent window to determine whether the frame is
     * currently the browser target.
     * 
     * @return the address of the frame in this window that is the browser target, or null if no
     *         frame in this window is the browser target.
     */
    public String getTargetFrameAddress ();

    /**
     * Enables Javascript alerts in this window. This window must be the current browser target.
     * 
     * @throws WindowException
     *         if this window is not the current browser target
     * @throws Exception
     *         if an error occurs
     */
    public void enableJavascriptAlert ()
            throws WindowException, Exception;

    /**
     * Disables Javascript alerts in this window. This window must be the current browser target.
     * 
     * @throws WindowException
     *         if this window is not the current browser target
     * @throws Exception
     *         if an error occurs
     */
    public void disableJavascriptAlert ()
            throws WindowException, Exception;

    /**
     * Enables Javascript confirm dialogues in this window. This window must be the current browser
     * target.
     * 
     * @throws WindowException
     *         if this window is not the current browser target
     * @throws Exception
     *         if an error occurs
     */
    public void enableJavascriptConfirm ()
            throws WindowException, Exception;

    /**
     * Disables Javascript confirm dialogues in this window. This window must be the current browser
     * target.
     * 
     * @param confirm
     *        the value that should be returned by window.confirm(message)
     * @throws WindowException
     *         if this window is not the current browser target
     * @throws Exception
     *         if an error occurs
     */
    public void disableJavascriptConfirm (boolean confirm)
            throws WindowException, Exception;

    /**
     * Finds the page object representing the page that is currently displayed in the browser, if
     * that page object is a field in the calling class or a superclass.
     * 
     * @return the page object representing the page that is currently displayed in the browser
     * @throws Exception
     *         if the currently displayed page is not represented by any field of the current class
     *         or a superclass
     */
    public Page getCurrentPage ()
            throws Exception;

    /**
     * Checks if a page of the given class is currently displayed.
     * 
     * @param pageClass
     *        the page class
     * @return true if a page with the specified class exists in this window and is currently
     *         displayed, and false if a page with the specified class exists in this window but is
     *         not currently displayed
     * @throws NoSuchPageException
     *         if this window does not contain a page with the given class
     * @throws Exception
     *         if an error occurs while checking whether the page is currently displayed
     */
    public boolean onPage (Class<? extends Page> pageClass)
            throws NoSuchPageException, Exception;

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
     *         if this window is not the current browser window, or its name is not known
     * @throws TimedOut
     *         if the new window was not opened after the specified time
     * @throws Exception
     *         if an error occurs
     */
    public void synchronizeNewWindow (final Callable method, int timeout)
            throws WindowException, TimedOut, Exception;

    /**
     * Attempts to open a new window, waits until the new window is open and sets the browser target
     * to the new window. The timeout is set to the default timeout. This can be overridden in the
     * test.properties file.
     * 
     * @param method
     *        a callable object that opens a new window when called
     * @throws WindowException
     *         if this window is not the current browser window, or its name is not known
     * @throws TimedOut
     *         if the new window was not opened after the default time has passed
     * @throws Exception
     *         if an error occurs
     */
    public void synchronizeNewWindow (final Callable method)
            throws WindowException, TimedOut, Exception;

}
