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

import org.jwatter.browser.WebAutomationFramework;
import org.jwatter.util.Callable;
import org.jwatter.util.Waiting.TimedOut;

/**
 * Base class for all frames.
 */
public abstract class FrameImpl
        extends WindowImpl
        implements Frame {

    protected Window parent = null;
    protected Window topLevelWindow = null;
    protected String address = null;

    /**
     * Creates a new frame. You must use {@link #setParent(Window)} to set the parent frame or
     * window of the new frame.
     * 
     * @param browserFramework
     *        the browser that will display the frame
     * @param name
     *        the name of the new frame
     * @param pages
     *        the pages that will be displayed in the frame
     */
    protected FrameImpl (WebAutomationFramework browserFramework, String name)
            throws Exception {
        this(browserFramework, name, (Window)null);
    }

    /**
     * Creates a new frame.
     * 
     * @param browserFramework
     *        the browser that will display the frame
     * @param name
     *        the name of the new frame
     * @param parent
     *        the parent frame or window of the new frame
     * @param pages
     *        the pages that will be displayed in the frame
     */
    protected FrameImpl (WebAutomationFramework browserFramework, String name, Window parent)
            throws Exception {
        super(browserFramework);
        this.setName(name);
        this.setParent(parent);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Frame#setName(java.lang.String)
     */
    public void setName (String name) {
        this.name = name;
        this.setAddress();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Frame#setName()
     */
    @Override
    public void setName () {
        throw new UnsupportedOperationException(
                                                "setName on a frame must be called with an argument");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jwatter.Frame#setParent(org.jwatter.Window)
     */
    public void setParent (Window parent) {
        if (this.parent != parent) {
            this.parent = parent;
            parent.addFrame(this);
            if (parent instanceof Frame) {
                this.topLevelWindow = ((Frame)parent).getWindow();
            }
            else {
                this.topLevelWindow = parent;
            }
            this.setAddress();
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Frame#getParent()
     */
    public Window getParent () {
        return this.parent;
    }

    private void setAddress () {
        if (this.parent == null)
            return;
        if (this.parent instanceof Frame) {
            String parentAddress = ((Frame)this.parent).getAddress();
            // a frame that is embedded in another frame has an address only if the parent frame has
            // an address
            this.address = parentAddress != null ? (parentAddress + "." + this.name) : null;
        }
        else {
            this.address = this.name;
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Frame#getWindow()
     */
    public Window getWindow () {
        return this.topLevelWindow;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Frame#getAddress()
     */
    public String getAddress () {
        return this.address;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Frame#setAsTarget()
     */
    @Override
    public void setAsTarget ()
            throws WindowException, Exception {
        if (this.address == null) {
            logger.severe("address of frame " + this.getClass().getSimpleName() + " is not known");
            throw new FrameException("address of frame " + this.getClass().getSimpleName()
                + " is not known");
        }
        if (this.topLevelWindow == null) {
            logger.severe("window for frame " + this.getClass().getSimpleName() + " is not set");
            throw new WindowException("window for frame " + this.getClass().getSimpleName()
                + " is not set");
        }
        // first, set target to the window that contains this frame
        this.topLevelWindow.setAsTarget();
        // then set target to this frame
        logger.info("Setting target to frame " + this.address);
        this.browserFramework.setTargetToFrameWithId(this.address);
        // tell the window that the target is on this frame
        this.topLevelWindow.setTargetFrameAddress(this.address);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Frame#isTarget()
     */
    @Override
    public boolean isTarget ()
            throws WindowException, Exception {
        if (this.address == null) {
            logger.severe("address of frame " + this.getClass().getSimpleName() + " is not known");
            throw new FrameException("address of frame " + this.getClass().getSimpleName()
                + " is not known");
        }
        if (this.topLevelWindow == null) {
            logger.severe("window for frame " + this.getClass().getSimpleName() + " is not set");
            throw new WindowException("window for frame " + this.getClass().getSimpleName()
                + " is not set");
        }
        return this.topLevelWindow.containsTarget()
            && this.address.equals(this.topLevelWindow.getTargetFrameAddress());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Frame#containsTarget()
     */
    @Override
    public boolean containsTarget () {
        logger.severe("unsupported operation called on frame " + this.getClass().getSimpleName());
        throw new UnsupportedOperationException("this method is not supported by frames");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Frame#synchronizeNewWindow(org.jwatter.util.Callable,
     * int)
     */
    @Override
    public void synchronizeNewWindow (final Callable method, int timeout)
            throws WindowException, TimedOut, Exception {
        if (this.topLevelWindow == null) {
            logger.severe("window for frame " + this.getClass().getSimpleName() + " is not set");
            throw new WindowException("window for frame " + this.getClass().getSimpleName()
                + " is not set");
        }
        this.topLevelWindow.synchronizeNewWindow(method, timeout);
    }
}
