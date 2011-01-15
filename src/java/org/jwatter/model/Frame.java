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
 * A frame is similar to a {@link Window} in that it represents a collection of pages that are
 * displayed in the same HTML frame. It differs from a {@link Window} in that pages in a frame are
 * not displayed in their own browser window, but embedded within other pages, in an HTML frame or
 * iframe.
 * 
 * A frame has a parent {@link Window} which can be a {@link Frame}.
 * 
 * A frame has a window which is the top-level {@link Window} in the parent chain.
 * 
 * A frame has a name, which is usually the value of the name attribute in the HTML frame or iframe.
 * In contrast to a window name, which is a GUID assigned by the browser when the window is opened
 * in the browser, a frame name is specified in the HTML and thus can be provided when the frame
 * instance is created.
 * 
 * A frame has an address, which is the concatenation of the frame's parent's names and the frame's
 * name, separated by dots, e.g. frame1.frame2.frame3 (frame2 is embedded in frame1 and frame3 is
 * embedded in frame2). The frame address is used to set the browser target to a frame.
 */
public interface Frame
        extends Window {

    /**
     * Sets the name of this frame.
     * 
     * @param name
     *        the name of this frame
     */
    public void setName (String name);

    /**
     * This method is not implemented on frames, use {@link #setName(String)} instead.
     * 
     * @throws UnsupportedOperationException
     */
    public void setName ();

    /**
     * Sets the parent of this frame and adds this frame to the given parent as a child frame. It
     * also sets the window of this frame to the window of the parent if the parent is a frame, or
     * to the parent if the parent is not a frame. If the given parent is the parent of this frame
     * already, this method does nothing.
     * 
     * @param parent
     *        the new parent frame or window
     */
    public void setParent (Window parent);

    /**
     * Returns the parent of this frame.
     * 
     * @return the parent of this frame, or null if it has not been set.
     */
    public Window getParent ();

    /**
     * Returns the window that contains this frame.
     * 
     * @return the window of this frame
     */
    public Window getWindow ();

    /**
     * Returns the address of this frame.
     * 
     * @return the address of this frame
     */
    public String getAddress ();

    /**
     * Sets the browser target to this frame.
     * 
     * @throws FrameException
     *         if the name of this frame is not known
     * @throws WindowException
     *         if the window for this frame is not set
     * @throws Exception
     *         if an error occurs
     */
    public void setAsTarget ()
            throws FrameException, WindowException, Exception;

    /**
     * Checks if this frame is the current browser target. A return value of true implies that the
     * window which contains this frame is the current target window.
     * 
     * @return true if this frame is the browser target, else false
     * @throws FrameException
     *         if the address of this frame has not been set
     * @throws WindowException
     *         if the window for this frame has not been set
     * @throws Exception
     *         if an error occurs
     */
    public boolean isTarget ()
            throws FrameException, WindowException, Exception;

    /**
     * This method is not implemented for frames.
     * 
     * @return nothing
     * @throws UnsupportedOperationException
     */
    public boolean containsTarget ();

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jwatter.model.Window#synchronizeNewWindow(org.jwatter.util.Callable
     * , int)
     */
    public void synchronizeNewWindow (final Callable method, int timeout)
            throws WindowException, TimedOut, Exception;

}
