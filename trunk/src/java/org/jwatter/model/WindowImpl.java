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

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.jwatter.browser.WebAutomationFramework;
import org.jwatter.test.WebFunctionalTestCase;
import org.jwatter.util.AccessibleFieldIterator;
import org.jwatter.util.Callable;
import org.jwatter.util.Waiting;
import org.jwatter.util.Waiting.TimedOut;

/**
 * Base class for all windows.
 */
public abstract class WindowImpl
        implements Window {

    protected static final int DEFAULT_SYNC_WINDOW_TIMEOUT = 30;

    protected static final Logger logger = Logger.getLogger(WindowImpl.class.getName());

    /**
     * Default timeout for browser sync operations, in seconds.
     */
    protected static int syncWindowTimeout;

    static {

        String syncBrowserTimeoutProperty =
            WebFunctionalTestCase.getProperty("syncWindowTimeout", Window.class);
        syncWindowTimeout =
            syncBrowserTimeoutProperty != null ? Integer.parseInt(syncBrowserTimeoutProperty)
                                              : DEFAULT_SYNC_WINDOW_TIMEOUT;

    }
    protected WebAutomationFramework browserFramework;
    protected String name;
    protected Map<Class<? extends Page>, Field> pageFields;
    protected Map<Class<? extends Frame>, Field> frameFields;
    protected Map<Class<? extends Frame>, Frame> frames;
    protected Map<String, Frame> framesByName;

    /**
     * Contains the address of a frame embedded in this window if the frame is the current browser
     * target. A frame tells its window when it is set as target. If the window is set as target, it
     * sets {@link #targetFrameAddress} to null.
     */
    protected String targetFrameAddress;

    /**
     * Creates a new window. Creates pages for all accessible fields whose type implements
     * {@link Page} and is not abstract and assigns them to the fields.
     * 
     * @param browserFramework
     *        the browser that will display this window
     */
    protected WindowImpl (WebAutomationFramework browserFramework)
            throws Exception {
        this.browserFramework = browserFramework;
        this.name = null;

        // create an index of all accessible Page and Frame fields in this window
        this.pageFields = new HashMap<Class<? extends Page>, Field>();
        this.frameFields = new HashMap<Class<? extends Frame>, Field>();
        this.indexFields(Page.class, this.pageFields);
        this.indexFields(Frame.class, this.frameFields);

        // create pages
        this.createPages();

        this.frames = new HashMap<Class<? extends Frame>, Frame>();
        this.framesByName = new HashMap<String, Frame>();
        this.targetFrameAddress = null;
    }

    @SuppressWarnings("unchecked")
    protected <T> void indexFields (Class<? extends T> indexClass,
                                    Map<Class<? extends T>, Field> fieldIndex) {
        Class<? extends Window> windowClass = this.getClass();
        AccessibleFieldIterator fields = new AccessibleFieldIterator(windowClass);
        while (fields.hasNext()) {
            Field field = fields.next();
            Class<?> fieldClass = field.getType();
            if (indexClass.isAssignableFrom(fieldClass)) {
                fieldIndex.put((Class<? extends T>)fieldClass, field);
            }
        }
    }

    /**
     * Creates pages for all fields in this window whose type implements {@link Page} and is not
     * abstract. Assigns the pages to the corresponding fields. Sets the window of all pages to this
     * window.
     * 
     * @throws Exception
     *         if an error occurs
     */
    protected void createPages ()
            throws Exception {
        for (Entry<Class<? extends Page>, Field> e : this.pageFields.entrySet()) {
            Field pageField = e.getValue();
            Class<? extends Page> pageClass = e.getKey();
            if (!Modifier.isAbstract(pageClass.getModifiers())) {
                this.createPage(pageClass, pageField);
            }
        }
    }

    /**
     * Creates a new page of the specified class and assigns it to the specified field in this
     * window. Sets the page window to this window.
     * 
     * @param pageClass
     *        the requested page class
     * @param pageField
     *        the field to assign the page to
     * @throws Exception
     *         if the page cannot be created or the field has the wrong type or is not a field of
     *         this window
     */
    protected void createPage (Class<? extends Page> pageClass, Field pageField)
            throws Exception {
        Page page = PageFactory.newPage(pageClass, browserFramework);
        page.setWindow(this);
        pageField.set(this, page);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jwatter.Window#addFrame(org.jwatter.FrameImpl)
     */
    public void addFrame (Frame frame) {
        if (!(this.framesByName.containsKey(frame.getName()))) {
            this.frames.put(frame.getClass(), frame);
            this.framesByName.put(frame.getName(), frame);
            frame.setParent(this);
            if (this.frameFields.containsKey(frame.getClass())) {
                Field frameField = this.frameFields.get(frame.getClass());
                try {
                    frameField.set(this, frame);
                }
                catch (IllegalAccessException e) {}
            }
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#containsPage(java.lang.Class)
     */
    public boolean containsPage (Class<? extends Page> pageClass) {
        return this.pageFields.containsKey(pageClass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#containsFrame(java.lang.Class)
     */
    public boolean containsFrame (Class<? extends Frame> frameClass) {
        return this.frames.containsKey(frameClass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.model.Window#getPages()
     */
    public List<Page> getPages () {
        List<Page> pages = new ArrayList<Page>();
        for (Field pageField : pageFields.values()) {
            try {
                pages.add((Page)pageField.get(this));
            }
            catch (Exception e) {}
        }
        return pages;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.model.Window#getPageClasses()
     */
    public Collection<Class<? extends Page>> getPageClasses () {
        return this.pageFields.keySet();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#getPage(java.lang.Class)
     */
    @SuppressWarnings("unchecked")
    public <T extends Page> T getPage (Class<T> pageClass)
            throws NoSuchPageException {
        if (!this.containsPage(pageClass)) {
            logger.severe(this.getClass().getSimpleName() + " does not contain a "
                + pageClass.getSimpleName() + " page");
            throw new NoSuchPageException(pageClass, this.getClass());
        }
        try {
            return (T)this.pageFields.get(pageClass).get(this);
        }
        catch (IllegalAccessException e) {
            // rethrow as unchecked exception
            throw new RuntimeException(e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#getCurrentPage()
     */
    public Page getCurrentPage ()
            throws Exception {
        for (Page page : this.getPages()) {
            if (page.onPage()) {
                return page;
            }
        }
        logger.severe("Unable to determine current page");
        throw new Exception("Unable to determine current page");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#onPage(java.lang.Class)
     */
    public boolean onPage (Class<? extends Page> pageClass)
            throws NoSuchPageException, Exception {
        Page page = this.getPage(pageClass);
        if (page == null) {
            logger.severe(pageClass.getSimpleName() + " page does not exist in " + this.getClass());
            throw new NoSuchPageException(pageClass, this.getClass());
        }
        return page.onPage();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#getFrame(java.lang.Class)
     */
    public Frame getFrame (Class<? extends Frame> frameClass)
            throws NoSuchFrameException {
        if (!this.frames.containsKey(frameClass)) {
            throw new NoSuchFrameException(frameClass, this.getClass());
        }
        return this.frames.get(frameClass);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#getFrame(java.lang.String)
     */
    public Frame getFrame (String name)
            throws NoSuchFrameException {
        if (!this.framesByName.containsKey(name)) {
            throw new NoSuchFrameException(name, this.getClass());
        }
        return this.framesByName.get(name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#setName()
     */
    public void setName ()
            throws WindowException, Exception {
        this.name = this.browserFramework.getWindowName();
        if (this.name == null) {
            logger.severe("unable to determine browser window name, or no browser window open");
            throw new WindowException(
                                      "unable to determine browser window name, or no browser window open");
        }
        logger.info("window name: " + this.name);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#getName()
     */
    public String getName () {
        return this.name;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#setAsTarget()
     */
    public void setAsTarget ()
            throws WindowException, Exception {
        if (this.name == null) {
            logger.severe("unknown window name");
            throw new WindowException("unknown window name");
        }
        logger.info("Setting browser target to " + this.name);
        this.browserFramework.setTargetToWindow(this.name);
        this.targetFrameAddress = null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#isTarget()
     */
    public boolean isTarget ()
            throws WindowException, Exception {
        return this.containsTarget() && this.targetFrameAddress == null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#containsTarget()
     */
    public boolean containsTarget ()
            throws WindowException, Exception {
        if (this.name == null) {
            logger.severe("unknown window name");
            throw new WindowException("unknown window name");
        }
        return this.name.equals(this.browserFramework.getWindowName());
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#setTargetFrameAddress(java.lang.String)
     */
    public void setTargetFrameAddress (String address) {
        this.targetFrameAddress = address;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#getTargetFrameAddress()
     */
    public String getTargetFrameAddress () {
        return this.targetFrameAddress;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#enableJavascriptAlert()
     */
    public void enableJavascriptAlert ()
            throws WindowException, Exception {
        this.ensureIsTarget("cannot enable Javascript alert on non target window");
        this.browserFramework.enableJavascriptAlert();
        logger.info("Javascript alert enabled");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#disableJavascriptAlert()
     */
    public void disableJavascriptAlert ()
            throws WindowException, Exception {
        this.ensureIsTarget("cannot disable Javascript alert on non target window");
        this.browserFramework.disableJavascriptAlert();
        logger.info("Javascript alert disabled");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#enableJavascriptConfirm()
     */
    public void enableJavascriptConfirm ()
            throws WindowException, Exception {
        this.ensureIsTarget("cannot enable Javascript confirm on non target window");
        this.browserFramework.enableJavascriptConfirm();
        logger.info("Javascript confirm enabled");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.jwatter.Window#disableJavascriptConfirm(boolean)
     */
    public void disableJavascriptConfirm (boolean confirm)
            throws WindowException, Exception {
        this.ensureIsTarget("cannot disable Javascript confirm on non target window");
        this.browserFramework.disableJavascriptConfirm(confirm);
        logger.info("Javascript confirm disabled (will always return " + confirm + ")");
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jwatter.Window#synchronizeNewWindow(org.jwatter.util.Callable,
     * int)
     */
    public void synchronizeNewWindow (final Callable method, int timeout)
            throws WindowException, TimedOut, Exception {
        if (this.name == null) {
            logger.severe("current window name is not known");
            throw new WindowException("current window name is not known");
        }
        ensureIsTarget("trying to open a new window from non target window");
        if (method != null) {
            method.call();
        }
        final String windowname = this.name;
        new Waiting(timeout * 1000, "new window not open after " + timeout + " seconds") {

            @Override
            public boolean until ()
                    throws Exception {
                return !browserFramework.getMostRecentWindowName().equals(windowname);
            }
        }.waitUntil();
        String newWindowName = browserFramework.getMostRecentWindowName();
        logger.info("new window name: " + newWindowName);
        browserFramework.setTargetToWindow(newWindowName);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.jwatter.Window#synchronizeNewWindow(org.jwatter.util.Callable)
     */
    public void synchronizeNewWindow (final Callable method)
            throws WindowException, TimedOut, Exception {
        this.synchronizeNewWindow(method, syncWindowTimeout);
    }

    protected void ensureIsTarget (String message)
            throws WindowException, Exception {
        String currentTargetWindowName = this.browserFramework.getWindowName();
        if (!this.name.equals(currentTargetWindowName)) {
            logger.severe(message + " (method called on " + this.name + ", target window is "
                + currentTargetWindowName + ")");
            throw new WindowException(message + " (method called on " + this.name
                + ", target window is " + currentTargetWindowName + ")");
        }
    }
}
