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
package org.jwatter.browser;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

import org.jwatter.html.Element;
import org.jwatter.html.NoSuchOptionException;
import org.jwatter.toolkit.generate.HtmlAttribute;
import org.jwatter.toolkit.generate.HtmlElement;
import org.jwatter.toolkit.generate.HtmlElementContent;
import org.jwatter.toolkit.generate.HtmlElementIndex;

/**
 * The web automation framework interface defines methods to access a web
 * application through a web browser. The methods defined here are application
 * independent. They provide access to basic HTML elements such as input
 * elements, links, images, text, using attributes such as id and name. The
 * methods defined here are browser independent. Typically there are different
 * implementations of this interface for different browsers.
 * 
 * @author kschneider
 * 
 */
public interface WebAutomationFramework {

	/**
	 * Creates a new web browser and opens a browser window. The contents of the
	 * browser window is undefined (depends on the browser).
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	public void createBrowser () throws Exception;

	/**
	 * Creates a new web browser and opens a browser window. The contents of the
	 * browser window is undefined (depends on the browser). The browser uses the
	 * specified profile.
	 * 
	 * @param profileName
	 *             the name of the browser profile to use
	 * @throws Exception
	 *             if an error occurs
	 */
	public void createBrowser (String profileName) throws Exception;
	
	/**
	 * Closes the browser.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	public void closeBrowser () throws Exception;

	/**
	 * Closes the current window. If the current window is the only open window,
	 * also closes the browser.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	public void closeWindow () throws Exception;

	/**
	 * Checks whether a browser has been opened.
	 * 
	 * @return true if a browser is currently open
	 * @throws Exception
	 *             if an error occurs
	 */
	public boolean isBrowserOpen () throws Exception;

	/**
	 * Returns the browser version.
	 * 
	 * @return the version of the current browser
	 * @throws Exception
	 *             if an error occurs
	 */
	public String getBrowserVersion () throws Exception;

	/**
	 * Loads the specified URL in the current browser window.
	 * 
	 * @param url
	 *            the URL of the page to load
	 * @throws Exception
	 *             if an error occurs
	 */
	public void loadUrl (String url) throws Exception;

	/**
	 * Returns to the previous page. This should simulate a click on the back
	 * button of the browser.
	 * 
	 * @throws BrowserHistoryException
	 *             if the current browser window already contains the oldest
	 *             page in the history
	 * @throws Exception
	 *             if an error occurs
	 */
	public void back () throws BrowserHistoryException, Exception;

	/**
	 * Moves one page forward in the browser's history. This should simulate a
	 * click on the forward button of the browser.
	 * 
	 * @throws BrowserHistoryException
	 *             if the current browser window already contains the most
	 *             recent page from the history
	 * @throws Exception
	 *             if an error occurs
	 */
	public void forward () throws BrowserHistoryException, Exception;

	/**
	 * Disables Javascript alerts in the currently targeted window. Subsequent
	 * calls to <code>window.alert(message)</code> will have no effect.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	public void disableJavascriptAlert () throws Exception;

	/**
	 * Enables Javascript alerts in the currently targeted window, if they have
	 * been disabled previously.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	public void enableJavascriptAlert () throws Exception;

	/**
	 * Disables Javascript confirm dialogues in the currently targeted window.
	 * Subsequent calls to <code>window.confirm(message)</code> will return the
	 * value of the confirm parameter without displaying a pop-up window.
	 * 
	 * @param confirm
	 *             the value that should be returned by
	 *             <code>window.confirm(message)</code>
	 * @throws Exception
	 *             if an error occurs
	 */
	public void disableJavascriptConfirm (boolean confirm) throws Exception;

	/**
	 * Enables Javascript confirm dialogues in the currently targeted window,
	 * if they have been previously disabled.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	public void enableJavascriptConfirm () throws Exception;

	/**
	 * Set the target of subsequent browser operations to the specified frame.
	 * 
	 * @param frameIndex
	 *             the frame index, zero-based
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = {"frame", "iframe"})
	public void setTargetToFrame (@HtmlElementIndex int frameIndex) throws Exception;

	/**
	 * Set the target of subsequent browser operations to the specified frame.
	 * A frame address is specified in the form <i>name</i>[.<i>name</i>...]
	 * where <i>name</i> can be a frame id, frame name, or frame index
	 * (zero-based). Each <i>name</i> specifies a child frame of the frame with
	 * the previous name, or of the current window.
	 * 
	 * @param frameAddress
	 *             the frame address
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = {"frame", "iframe"})
	public void setTargetToFrameWithId (@HtmlAttribute("id") String frameAddress)
			throws Exception;

	/**
	 * Set the target of subsequent browser operations to the specified frame.
	 * A frame address is specified in the form <i>name</i>[.<i>name</i>...]
	 * where <i>name</i> can be a frame id, frame name, or frame index
	 * (zero-based). Each <i>name</i> specifies a child frame of the frame with
	 * the previous name, or of the current window.
	 * 
	 * @param frameAddress
	 *             the frame address
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = {"frame", "iframe"})
	public void setTargetToFrameWithName (@HtmlAttribute("name") String frameAddress)
			throws Exception;

	/**
	 * Set the target of subsequent browser operations to the window with the
	 * specified name, or to the default window.
	 * 
	 * @param windowName
	 *             the window name, or null to set the target to the default
	 *             window
	 * @throws Exception
	 *             if an error occurs
	 */
	public void setTargetToWindow (String windowName) throws Exception;

	/**
	 * Returns the name of the window that the browser currently operates on.
	 * If the browser currently operates on the default window, returns a unique
	 * handle for the default window since the default window has no name.
	 * Can be used as an argument for {@link #setTargetToWindow(String)}.
	 * 
	 * @return the name of the active window
	 * @throws Exception
	 *             if an error occurs
	 */
	public String getWindowName () throws Exception;

	/**
	 * Returns the names of all open windows. For windows that have no name or
	 * were not opened through {@link #setTargetToWindow(String)}, the list
	 * contains a unique handle.
	 * 
	 * @return the names of all open windows
	 * @throws Exception
	 *             if an error occurs
	 */
	public List<String> getWindowNames () throws Exception;

	/**
	 * Returns the name of the most recently opened window.
	 * <p>
	 * <i>Note: You should call one of the methods {@link #getWindowName()},
	 * {@link #getWindowNames()}, {@link #getMostRecentWindowName()}
	 * immediately before calling any method that opens a new window,
	 * otherwise this method may return the name of a window that is not the
	 * most recently opened window. There is no way for this framework to
	 * be notified automatically when a new window is opened, other than by
	 * forcing it to register all open windows, which you do by calling any
	 * of the mentioned methods.</i>
	 * 
	 * @return the name of the most recently opened window
	 * @throws Exception
	 *             if an error occurs
	 */
	public String getMostRecentWindowName () throws Exception;

	/**
	 * Returns the URL of the current page.
	 * 
	 * @return the URL of the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	public String getUrl () throws Exception;

	/**
	 * Returns the URL path of the current page.
	 * 
	 * @return the URL path of the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	public String getUrlPath () throws Exception;

	/**
	 * Returns the page title.
	 * 
	 * @return the title of the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	public String getTitle () throws Exception;

	/**
	 * Returns the page contents.
	 * 
	 * @return the contents of the current page as plain text
	 * @throws Exception
	 *             if an error occurs
	 */
	public String getContents () throws Exception;

	/**
	 * Checks if a page contains some text.
	 * 
	 * @param text
	 *            the text to look for
	 * @return true if the current page contains the specified text
	 * @throws Exception
	 *             if an error occurs
	 */
	public boolean containsText (String text) throws Exception;

	/**
	 * Checks if a page contains a text input element with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return true if the current page contains an element
	 *         <code>&lt;input type="text" id="id"/&gt;</code>
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public boolean hasTextInputWithId (@HtmlAttribute("id") String id) throws
			Exception;

	/**
	 * Checks if a page contains a text input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return true if the current page contains an element
	 *         <code>&lt;input type="text" name="name"/&gt;</code>
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public boolean hasTextInputWithName (@HtmlAttribute("name") String name)
			throws Exception;

	/**
	 * Checks if a page contains a password field with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return true if the current page contains an element
	 *         <code>&lt;input type="password" id="id"/&gt;</code>
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public boolean hasPasswordInputWithId (@HtmlAttribute("id") String id) throws
			Exception;

	/**
	 * Checks if a page contains a password field with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return true if the current page contains an element
	 *         <code>&lt;input type="password" name="name"/&gt;</code>
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public boolean hasPasswordInputWithName (@HtmlAttribute("name") String name)
			throws Exception;

	/**
	 * Checks if a page contains a multi-line text field with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return true if the current page contains an element
	 *         <code>&lt;textarea id="id"&gt;&lt;/textarea&gt;</code>
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "textarea")
	public boolean hasTextareaWithId (@HtmlAttribute("id") String id)
			throws Exception;

	/**
	 * Checks if a page contains a multi-line text field with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return true if the current page contains an element
	 *         <code>&lt;textarea name="name"&gt;&lt;/textarea&gt;</code>
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "textarea")
	public boolean hasTextareaWithName (@HtmlAttribute("name") String name)
			throws Exception;

	/**
	 * Checks if a page contains a radio button with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return true if the current page contains an element
	 *         <code>&lt;input type="radio" id="id"/&gt;</code>
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "radio")
	public boolean hasRadioButtonWithId (@HtmlAttribute("id") String id) throws
			Exception;

	/**
	 * Checks if a page contains a radio button with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return true if the current page contains an element
	 *         <code>&lt;input type="radio" name="name"/&gt;</code>
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "radio")
	public boolean hasRadioButtonWithName (@HtmlAttribute("name") String name)
			throws Exception;

	/**
	 * Checks if a page contains a checkbox with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return true if the current page contains an element
	 *         <code>&lt;input type="checkbox" id="id"/&gt;</code>
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "checkbox")
	public boolean hasCheckboxWithId (@HtmlAttribute("id") String id) throws
			Exception;

	/**
	 * Checks if a page contains a checkbox with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return true if the current page contains an element
	 *         <code>&lt;input type="checkbox" name="name"/&gt;</code>
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "checkbox")
	public boolean hasCheckboxWithName (@HtmlAttribute("name") String name)
			throws Exception;

	/**
	 * Checks if a page contains a select element (drop-down menu) with a
	 * specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return true if the current page contains an element
	 *         <code>&lt;select id="id"&gt;&lt;/select&gt;</code>
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "select")
	public boolean hasDropDownMenuWithId (@HtmlAttribute("id") String id)
			throws Exception;

	/**
	 * Checks if a page contains a select element (drop-down menu) with a
	 * specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return true if the current page contains an element
	 *         <code>&lt;select name="name"&gt;&lt;/select&gt;</code>
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "select")
	public boolean hasDropDownMenuWithName (@HtmlAttribute("name") String name)
			throws Exception;

	/**
	 * Checks if a drop down-menu contains an option with a specific text.
	 * 
	 * @param id
	 *            the id attribute value to identify the drop down menu
	 * @param option
	 *            the option text
	 * @return true if the drop-down menu with the specified id has an option
	 *            with the specified text
	 * @throws NoSuchElementException
	 *            if there is no drop-down menu with the specified id in the
	 *            current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "select")
	public boolean hasOptionInDropDownMenuWithId (@HtmlAttribute("id") String id,
			String option) throws NoSuchElementException, Exception;

	/**
	 * Checks if a drop down-menu contains an option with a specific text.
	 * 
	 * @param name
	 *            the name attribute value to identify the drop down menu
	 * @param option
	 *            the option text
	 * @return true if the drop-down menu with the specified name has an option
	 *            with the specified text
	 * @throws NoSuchElementException
	 *            if there is no drop-down menu with the specified name in the
	 *            current page
	 * @throws AmbiguousElementException
	 *            if there are multiple drop-down menus with the specified name
	 *            in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "select")
	public boolean hasOptionInDropDownMenuWithName (@HtmlAttribute("name") String name,
			String option) throws NoSuchElementException, AmbiguousElementException,
			Exception;

	/**
	 * Checks if a drop down-menu contains an option with a specific text.
	 * 
	 * @param name
	 *            the name attribute value to identify the drop down menu
	 * @param which
	 *            specifies which of the matching drop-down menus this method
	 *            should use (1-based)
	 * @param option
	 *            the option text
	 * @return true if the drop-down menu with the specified name has an option
	 *            with the specified text
	 * @throws NoSuchElementException
	 *            if there are less than the specified number of drop-down menus
	 *            with the specified name in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "select")
	public boolean hasOptionInDropDownMenuWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which, String option) throws NoSuchElementException,
			Exception;

	/**
	 * Returns all options in a drop down menu with a specific id.
	 * 
	 * @param id
	 *            the id attribute value to identify the drop-down menu
	 * @return an array containing the option text for each option
	 * @throws NoSuchElementException
	 *            if there is no drop-down menu with the specified id in the
	 *            current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "select")
	public String[] optionsInDropDownMenuWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Returns all options in a drop down menu with a specific name.
	 * 
	 * @param name
	 *            the name attribute value to identify the drop-down menu
	 * @return an array containing the option text for each option
	 * @throws NoSuchElementException
	 *            if there is no drop-down menu with the specified name in the
	 *            current page
	 * @throws AmbiguousElementException
	 *            if there are multiple drop-down menus with the specified name
	 *            in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "select")
	public String[] optionsInDropDownMenuWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Returns all options in a drop down menu with a specific name.
	 * 
	 * @param name
	 *            the name attribute value to identify the drop-down menu
	 * @param which
	 *            specifies which of the matching drop-down menus this method
	 *            should use (1-based)
	 * @return an array containing the option text for each option
	 * @throws NoSuchElementException
	 *            if there are less than the specified number of drop-down menus
	 *            in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "select")
	public String[] optionsInDropDownMenuWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Checks if a page contains a button with a specific id. The button can be
	 * a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return true if the current page contains a button with the specified id
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public boolean hasButtonWithId (@HtmlAttribute("id") String id) throws Exception;

	/**
	 * Checks if a page contains a button with a specific name. The button can be
	 * a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return true if the current page contains a button with the specified name
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public boolean hasButtonWithName (@HtmlAttribute("name") String name) throws
			Exception;

	/**
	 * Checks if a page contains a button with a specific button text. The
	 * button can be a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param text
	 *            the button text
	 * @return true if the current page contains a button with the specified
	 *         text
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public boolean hasButtonWithText (@HtmlAttribute("value") String text)
			throws Exception;

	/**
	 * Checks if a page contains a button with a specific title. The
	 * button can be a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param title
	 *            the value of the title attribute
	 * @return true if the current page contains a button with the specified
	 *         title
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public boolean hasButtonWithTitle (@HtmlAttribute("title") String title)
			throws Exception;

	/**
	 * Checks if a page contains a link with a specific link text.
	 * 
	 * @param text
	 *            the link text
	 * @return true if the current page contains a link with the specified text
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "a")
	public boolean hasLinkWithText (@HtmlElementContent String text)
			throws Exception;

	/**
	 * Checks if a page contains an image with a specific image URL.
	 * 
	 * @param imageurl
	 *            the image URL, either absolute or relative to the current page
	 *            URL
	 * @return true if the current page contains an image with the specified URL
	 * @throws URISyntaxException
	 *             if the specified URL is not wellformed
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "img")
	public boolean hasImageWithUrl (@HtmlAttribute("src") String imageurl) throws
			URISyntaxException, Exception;
	
	/**
	 * Checks if a page contains a span with a specific CSS class.
	 * 
	 * @param cssclass
	 *            the CSS class
	 * @return true if the current page contains a span with the specified class
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "span")
	public boolean hasSpanWithClass (@HtmlAttribute("class") String cssclass) throws
			Exception;

	/**
	 * Checks if the focus is on a text input element with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return true if the focus is on the text input element with the specified
	 *         id
	 * @throws NoSuchElementException
	 *             if there is no text input element with the specified id in
	 *             the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public boolean hasFocusOnTextInputWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Checks if the focus is on a text input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return true if the focus is on the text input element with the specified
	 *         name
	 * @throws NoSuchElementException
	 *             if there is no text input element with the specified name in
	 *             the current page
	 * @throws AmbiguousElementException
	 *             if there are multiple text input elements with the specified
	 *             name in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public boolean hasFocusOnTextInputWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Checks if the focus is on a text input element with a specific name. The
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching inputs this method should use
	 *            (1 based)
	 * @return true if the focus is on the specified text input element
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of text input
	 *             elements with the specified name in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public boolean hasFocusOnTextInputWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Checks if the focus is on a button with a specific id. The button can be
	 * a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return true if the focus is on a button with the specified id
	 * @throws NoSuchElementException
	 *             if there is no button with the specified id in the current
	 *             page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public boolean hasFocusOnButtonWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Checks if the focus is on a button with a specific name. The button can be
	 * a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return true if the focus is on a button with the specified name
	 * @throws NoSuchElementException
	 *             if there is no button with the specified name in the current
	 *             page
	 * @throws AmbiguousElementException
	 *             if there are multiple buttons with the specified name in the
	 *             current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public boolean hasFocusOnButtonWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, Exception;

	/**
	 * Checks if the focus is on a button with a specific name. The button can be
	 * a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching buttons this method should use
	 *            (1 based)
	 * @return true if the focus is on a button with the specified name
	 * @throws NoSuchElementException
	 *             if there is no button with the specified name in the current
	 *             page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public boolean hasFocusOnButtonWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Checks if the focus is on a button with a specific button text. The
	 * button can be a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param text
	 *            the button text
	 * @return true if the focus is on a button with the specified button text
	 * @throws NoSuchElementException
	 *             if there is no button with the specified button text in the
	 *             current page
	 * @throws AmbiguousElementException
	 *             if there are multiple buttons with the specified button text
	 *             in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public boolean hasFocusOnButtonWithText (@HtmlAttribute("value") String text)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Checks if the focus is on a button with a specific button text. The
	 * button can be a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param text
	 *            the button text
	 * @param which
	 *            specifies which of the matching buttons this method should use
	 *            (1 based)
	 * @return true if the focus is on the specified button with the specified
	 *         button text
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of buttons with
	 *             the specified button text in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public boolean hasFocusOnButtonWithText (@HtmlAttribute("value") String text,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Checks if the focus is on a link with the specified link text.
	 * 
	 * @param text
	 *            the link text
	 * @return true if the focus is on a link with the specified link text
	 * @throws NoSuchElementException
	 *             if there is no link with the specified link text in the
	 *             current page
	 * @throws AmbiguousElementException
	 *             if there are multiple links with the specified link text in
	 *             the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "a")
	public boolean hasFocusOnLinkWithText (@HtmlElementContent String text)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Checks if the focus is on a link with the specified link text.
	 * 
	 * @param text
	 *            the link text
	 * @param which
	 *            specifies which of the matching links this method should use
	 *            (1 based)
	 * @return true if the focus is on the specified link with the specified
	 *         link text
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of links with the
	 *             specified link text in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "a")
	public boolean hasFocusOnLinkWithText (@HtmlElementContent String text,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Returns the value of a text input element with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return the value of the text input element with the specified id
	 * @throws NoSuchElementException
	 *             if there is no text input element with the specified id in
	 *             the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public String valueOfTextInputWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Returns the value of a text input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return the value of the text input element with the specified name
	 * @throws NoSuchElementException
	 *             if there is no text input element with the specified name in
	 *             the current page
	 * @throws AmbiguousElementException
	 *             if there are multiple text input elements with the specified
	 *             name in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public String valueOfTextInputWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Returns the value of a text input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching inputs this method should use
	 *            (1 based)
	 * @return the value of the specified text input element with the specified
	 *         name
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of text input
	 *             elements in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public String valueOfTextInputWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Returns the value of a password input element with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return the value of the password input element with the specified id
	 * @throws NoSuchElementException
	 *             if there is no password input element with the specified id
	 *             in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public String valueOfPasswordInputWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Returns the value of a password input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return the value of the password input element with the specified name
	 * @throws NoSuchElementException
	 *             if there is no password input element with the specified name
	 *             in the current page
	 * @throws AmbiguousElementException
	 *             if there are multiple password input elements with the
	 *             specified name in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public String valueOfPasswordInputWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Returns the value of a password input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching inputs this method should use
	 *            (1 based)
	 * @return the value of the specified password input element with the
	 *         specified name
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of password input
	 *             elements in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public String valueOfPasswordInputWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Returns the value of a multi-line text field with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return the value of the multi-line text field with the specified id
	 * @throws NoSuchElementException
	 *             if there is no multi-line text field with the specified id in
	 *             the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "textarea")
	public String valueOfTextareaWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Returns the value of a multi-line text field with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return the value of the multi-line text field with the specified name
	 * @throws NoSuchElementException
	 *             if there is no multi-line text field with the specified name in
	 *             the current page
	 * @throws AmbiguousElementException
	 *             if there are multiple multi-line text fields with the specified
	 *             name in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "textarea")
	public String valueOfTextareaWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Returns the value of a multi-line text field with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching text fields this method should
	 *            use (1 based)
	 * @return the value of the specified multi-line text field with the specified
	 *         name
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of multi-line text
	 *             fields in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "textarea")
	public String valueOfTextareaWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Returns whether a checkbox with a specific id is checked.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return true if the checkbox with the specified id is checked, else false
	 * @throws NoSuchElementException
	 *            if there is no checkbox with the specified id in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "checkbox")
	public boolean isCheckedCheckboxWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Returns whether a checkbox with a specific name is checked.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return true if the checkbox with the specified name is checked, else false
	 * @throws NoSuchElementException
	 *            if there is no checkbox with the specified name in the current page
	 * @throws AmbiguousElementException
	 *            if there are multiple checkboxes with the specified name in the
	 *            current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "checkbox")
	public boolean isCheckedCheckboxWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Returns whether a checkbox with a specific name is checked.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching input elements should be used
	 *            (1-based)
	 * @return true if the specified checkbox is checked, else false
	 * @throws NoSuchElementException
	 *            if there is no checkbox with the specified name in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "checkbox")
	public boolean isCheckedCheckboxWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Returns whether a radio button with a specific id is checked.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @return true if the radio button with the specified id is checked, else false
	 * @throws NoSuchElementException
	 *            if there is no radio button with the specified id in the current
	 *            page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "radio")
	public boolean isCheckedRadioButtonWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Returns whether a radio button with a specific name is checked.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @return true if the radio button with the specified name is checked, else false
	 * @throws NoSuchElementException
	 *            if there is no radio button with the specified name in the current page
	 * @throws AmbiguousElementException
	 *            if there are multiple radio buttones with the specified name in the
	 *            current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "radio")
	public boolean isCheckedRadioButtonWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Returns whether a radio button with a specific name is checked.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching input elements should be used
	 *            (1-based)
	 * @return true if the specified radio button is checked, else false
	 * @throws NoSuchElementException
	 *            if there is no radio button with the specified name in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "radio")
	public boolean isCheckedRadioButtonWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Returns the currently selected option in a drop down-menu with a specific id.
	 * 
	 * @param id
	 *            the id attribute value to identify the drop down menu
	 * @return the text of the option that is currently selected
	 * @throws NoSuchElementException
	 *            if there is no drop-down menu with the specified id in the
	 *            current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "select")
	public String selectedOptionInDropDownMenuWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Returns the currently selected option in a drop down-menu with a specific name.
	 * 
	 * @param name
	 *            the name attribute value to identify the drop down menu
	 * @return the text of the option that is currently selected
	 * @throws NoSuchElementException
	 *            if there are less than the specified number of drop-down menus
	 *            with the specified name in the current page
	 * @throws AmbiguousElementException
	 *            if there are multiple drop-down menus with the specified name in
	 *            the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "select")
	public String selectedOptionInDropDownMenuWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Returns the currently selected option in a drop down-menu with a specific name.
	 * 
	 * @param name
	 *            the name attribute value to identify the drop down menu
	 * @param which
	 *            specifies which of the matching drop-down menus this method
	 *            should use (1-based)
	 * @return the text of the option that is currently selected
	 * @throws NoSuchElementException
	 *            if there are less than the specified number of drop-down menus
	 *            with the specified name in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "select")
	public String selectedOptionInDropDownMenuWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Returns the contents of a span element with a specific CSS class.
	 * 
	 * @param cssclass
	 *            the CSS class
	 * @return the contents of the span element with the specified CSS class
	 * @throws NoSuchElementException
	 *            if there is no span element with the specified class
	 * @throws AmbiguousElementException
	 *            if there is more than one span element with the specified class
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "span")
	public String contentsOfSpanWithClass (@HtmlAttribute("class") String cssclass)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Returns the contents of a span element with a specific CSS class.
	 * 
	 * @param cssclass
	 *            the CSS class
	 * @param which
	 *            specifies which of the matching elements this method should use
	 *            (1 based)
	 * @return the contents of the specified span element with the specified class
	 * @throws NoSuchElementException
	 *            if there are less than the specified number of span elements with
	 *            the specified class
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "span")
	public String contentsOfSpanWithClass (@HtmlAttribute("class") String cssclass,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;
	
	/**
	 * Returns the contents of all span elements with a specific CSS class.
	 * 
	 * @param cssclass
	 *            the CSS class
	 * @return an array containing the contents of all span elements with the specified
	 *            CSS class. Returns an array of length 0 if there are no such span
	 *            elements.
	 * @throws Exception
	 *            if an error occors
	 */
	@HtmlElement(name = "span")
	public String[] contentsOfSpansWithClass (@HtmlAttribute("class") String cssclass)
			throws Exception;
	
	/**
	 * Performs a click on a text input element with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @throws NoSuchElementException
	 *             if there is no text input element with the specified id in
	 *             the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public void clickTextInputWithId (@HtmlAttribute("id") String id) throws
			NoSuchElementException, Exception;

	/**
	 * Performs a click on a text input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @throws NoSuchElementException
	 *             if there is no text input element with the specified name in
	 *             the current page
	 * @throws AmbiguousElementException
	 *             if there are multiple text input elements with the specified
	 *             name in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public void clickTextInputWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Performs a click on a text input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching inputs this method should use
	 *            (1 based)
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of text input
	 *             elements in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public void clickTextInputWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Performs a click on a password input element with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @throws NoSuchElementException
	 *             if there is no password input element with the specified id
	 *             in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public void clickPasswordInputWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Performs a click on a password input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @throws NoSuchElementException
	 *             if there is no password input element with the specified name
	 *             in the current page
	 * @throws AmbiguousElementException
	 *             if there are multiple password input elements with the
	 *             specified name in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public void clickPasswordInputWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Performs a click on a password input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching inputs this method should use
	 *            (1 based)
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of password input
	 *             elements in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public void clickPasswordInputWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Performs a click on a radio button with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @throws NoSuchElementException
	 *             if there is no radio button with the specified id in the current
	 *             page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "radio")
	public void clickRadioButtonWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Performs a click on a radio button with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @throws NoSuchElementException
	 *             if there is no radio button with the specified name in the
	 *             current page
	 * @throws AmbiguousElementException
	 *             if there are multiple radio buttons with the specified name in
	 *             the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "radio")
	public void clickRadioButtonWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Performs a click on a radio button with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching inputs this method should use
	 *            (1 based)
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of radio buttons
	 *             in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "radio")
	public void clickRadioButtonWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Performs a click on a checkbox with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @throws NoSuchElementException
	 *             if there is no checkbox with the specified id in the current
	 *             page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "checkbox")
	public void clickCheckboxWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Performs a click on a checkbox with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @throws NoSuchElementException
	 *             if there is no checkbox with the specified name in the current
	 *             page
	 * @throws AmbiguousElementException
	 *             if there are multiple checkboxes with the specified name in the
	 *             current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "checkbox")
	public void clickCheckboxWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Performs a click on a checkbox with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching inputs this method should use
	 *            (1 based)
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of checkboxes in
	 *             the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "checkbox")
	public void clickCheckboxWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Selects an option from a drop-down menu with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @param option
	 *            the text of the option to select
	 * @throws NoSuchElementException
	 *            if there is no drop-down menu with the specified id in the
	 *            current page
	 * @throws NoSuchOptionException
	 *            if the drop-down menu does not have an option with the specified
	 *            text
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "select")
	public void selectOptionFromDropDownMenuWithId (@HtmlAttribute("id") String id,
			String option) throws NoSuchElementException, NoSuchOptionException,
			Exception;

	/**
	 * Selects an option from a drop-down menu with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param option
	 *            the text of the option to select
	 * @throws NoSuchElementException
	 *            if there is no drop-down menu with the specified name in the
	 *            current page
	 * @throws AmbiguousElementException
	 *            if there are multiple drop-down menus with the specified name
	 *            in the current page
	 * @throws NoSuchOptionException
	 *            if the drop-down menu does not have an option with the specified
	 *            text
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "select")
	public void selectOptionFromDropDownMenuWithName (@HtmlAttribute("name") String name,
			String option) throws NoSuchElementException, AmbiguousElementException,
			NoSuchOptionException, Exception;

	/**
	 * Selects an option from a drop-down menu with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching drop-down menus this method
	 *            should use (1-based)
	 * @param option
	 *            the text of the option to select
	 * @throws NoSuchElementException
	 *            if there are less than the specified number of drop-down menus
	 *            with the specified name in the current page
	 * @throws NoSuchOptionException
	 *            if the drop-down menu does not have an option with the specified
	 *            text
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "select")
	public void selectOptionFromDropDownMenuWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which, String option) throws NoSuchElementException,
			NoSuchOptionException, Exception;

	/**
	 * Performs a click on a button with a specific id. The button can be a
	 * <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @throws NoSuchElementException
	 *             if there is no button with the specified id in the current
	 *             page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public void clickButtonWithId (@HtmlAttribute("id") String id) throws
			NoSuchElementException, Exception;

	/**
	 * Performs a click on a button with a specific name. The button can
	 * be a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @throws NoSuchElementException
	 *             if there is no button with the specified name in the
	 *             current page
	 * @throws AmbiguousElementException
	 *             if there are multiple buttons with the specified name
	 *             in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public void clickButtonWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Performs a click on a button with a specific name. The button can
	 * be a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching buttons this method should use
	 *            (1 based)
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of buttons with
	 *             the specified name in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public void clickButtonWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Performs a click on a button with a specific button text. The button can
	 * be a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param text
	 *            the button text
	 * @throws NoSuchElementException
	 *             if there is no button with the specified button text in the
	 *             current page
	 * @throws AmbiguousElementException
	 *             if there are multiple buttons with the specified button text
	 *             in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public void clickButtonWithText (@HtmlAttribute("value") String text)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Performs a click on a button with a specific button text. The button can
	 * be a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param text
	 *            the button text
	 * @param which
	 *            specifies which of the matching buttons this method should use
	 *            (1 based)
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of buttons with
	 *             the specified button text in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public void clickButtonWithText (@HtmlAttribute("value") String text,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Performs a click on a button with a specific title. The button can
	 * be a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param title
	 *            the value of the title attribute
	 * @throws NoSuchElementException
	 *             if there is no button with the specified title in the
	 *             current page
	 * @throws AmbiguousElementException
	 *             if there are multiple buttons with the specified title
	 *             in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public void clickButtonWithTitle (@HtmlAttribute("title") String title)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Performs a click on a button with a specific title. The button can
	 * be a <code>&lt;input type="submit"/&gt;</code> element or a
	 * <code>&lt;button/&gt;</code> element.
	 * 
	 * @param title
	 *            the value of the title attribute
	 * @param which
	 *            specifies which of the matching buttons this method should use
	 *            (1 based)
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of buttons with
	 *             the specified title in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(
			name = {"input", "button"},
			attributeName = {"type", ""},
			attributeValue = {"submit", ""})
	public void clickButtonWithTitle (@HtmlAttribute("title") String title,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Performs a click on a link with a specific link text.
	 * 
	 * @param text
	 *            the link text
	 * @throws NoSuchElementException
	 *             if there is no link with the specified link text in the
	 *             current page
	 * @throws AmbiguousElementException
	 *             if there are multiple links with the specified link text in
	 *             the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "a")
	public void clickLinkWithText (@HtmlElementContent String text)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Performs a click on a link with a specific link text.
	 * 
	 * @param text
	 *            the link text
	 * @param which
	 *            specifies which of the matching links this method should use
	 *            (1 based)
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of links with the
	 *             specified link text in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "a")
	public void clickLinkWithText (@HtmlElementContent String text,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Performs a click on an image with a specific image URL.
	 * 
	 * @param imageurl
	 *            the image URL, either absolute or relative to the current page
	 *            URL
	 * @throws NoSuchElementException
	 *             if there is no image with the specified URL in the current
	 *             page
	 * @throws AmbiguousElementException
	 *             if there are multiple images with the specified URL in the
	 *             current page
	 * @throws URISyntaxException
	 *             if the specified URL is not wellformed
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "img")
	public void clickImageWithUrl (@HtmlAttribute("src") String imageurl)
			throws NoSuchElementException, AmbiguousElementException,
			URISyntaxException, Exception;

	/**
	 * Performs a click on an image with a specific image URL.
	 * 
	 * @param imageurl
	 *            the image URL, either absolute or relative to the current page
	 *            URL
	 * @param which
	 *            specifies which of the matching images this method should use
	 *            (1 based)
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of images with
	 *             the specified URL in the current page
	 * @throws URISyntaxException
	 *             if the specified URL is not wellformed
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "img")
	public void clickImageWithUrl (@HtmlAttribute("src") String imageurl,
			@HtmlElementIndex int which) throws NoSuchElementException,
			URISyntaxException, Exception;

	/**
	 * Sets the focus on a text input element with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @throws NoSuchElementException
	 *             if there is no input element with the specified id in the
	 *             current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public void focusOnTextInputWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Sets the focus on a text input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @throws NoSuchElementException
	 *             if there is no text input element with the specified name in
	 *             the current page
	 * @throws AmbiguousElementException
	 *             if there are multiple text input elements with the specified
	 *             name in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public void focusOnTextInputWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Sets the focus on a text input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching inputs this method should use
	 *            (1 based)
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of text input
	 *             elements in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public void focusOnTextInputWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Sets the value of a text input element with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @param value
	 *            the value to set
	 * @throws NoSuchElementException
	 *             if there is no input element with the specified id in the
	 *             current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public void setTextInputWithId (@HtmlAttribute("id") String id, String value)
			throws NoSuchElementException, Exception;

	/**
	 * Sets the value of a text input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param value
	 *            the value to set
	 * @throws NoSuchElementException
	 *             if there is no text input element with the specified name in
	 *             the current page
	 * @throws AmbiguousElementException
	 *             if there are multiple text input elements with the specified
	 *             name in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public void setTextInputWithName (@HtmlAttribute("name") String name, String
			value) throws NoSuchElementException, AmbiguousElementException,
			Exception;

	/**
	 * Sets the value of a text input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching inputs this method should use
	 *            (1 based)
	 * @param value
	 *            the value to set
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of text input
	 *             elements in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public void setTextInputWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which, String value)
			throws NoSuchElementException, Exception;

	/**
	 * Sets the value of a password input element with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @param value
	 *            the value to set
	 * @throws NoSuchElementException
	 *             if there is no input element with the specified id in the
	 *             current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public void setPasswordInputWithId (@HtmlAttribute("id") String id, String value)
			throws NoSuchElementException, Exception;

	/**
	 * Sets the value of a password input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param value
	 *            the value to set
	 * @throws NoSuchElementException
	 *             if there is no password input element with the specified name
	 *             in the current page
	 * @throws AmbiguousElementException
	 *             if there are multiple password input elements with the
	 *             specified name in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public void setPasswordInputWithName (@HtmlAttribute("name") String name, String
			value) throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Sets the value of a password input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching inputs this method should use
	 *            (1 based)
	 * @param value
	 *            the value to set
	 * @throws NoSuchElementException
	 *             if there are less than the specified number of password input
	 *             elements in the current page
	 * @throws Exception
	 *             if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public void setPasswordInputWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which, String value)
			throws NoSuchElementException, Exception;

	/**
	 * Sets the value of a multi-line text field with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @param value
	 *            the value to set
	 * @throws NoSuchElementException
	 *            if there is no multi-line text field with the specified id
	 *            in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "textarea")
	public void setTextareaWithId (@HtmlAttribute("id") String id, String value)
			throws NoSuchElementException, Exception;

	/**
	 * Sets the value of a multi-line text field with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param value
	 *            the value to set
	 * @throws NoSuchElementException
	 *            if there is no multi-line text field with the specified name
	 *            in the current page
	 * @throws AmbiguousElementException
	 *            if there are multiple multi-line text fields with the specified
	 *            name in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "textarea")
	public void setTextareaWithName (@HtmlAttribute("name") String name, String value)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Sets the value of a multi-line text field with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching text fields this method should
	 *            use (1-based)
	 * @param value
	 *            the value to set
	 * @throws NoSuchElementException
	 *            if there are less than the specified number of multi-line text
	 *            fields with the specified name in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "textarea")
	public void setTextareaWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which, String value)
			throws NoSuchElementException, Exception;

	/**
	 * Press the Enter key in the text input element with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @throws NoSuchElementException
	 *            if there is no text input element with the specified id in the
	 *            current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public void pressEnterInTextInputWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Press the Enter key in the text input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @throws NoSuchElementException
	 *            if there is no text input element with the specified name in the
	 *            current page
	 * @throws AmbiguousElementException
	 *            if there are multiple text input elements with the specified name
	 *            in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public void pressEnterInTextInputWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Press the Enter key in a text input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching input elements this method
	 *            should use (1-based)
	 * @throws NoSuchElementException
	 *            if there are less than the specified number of input elements
	 *            in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "text")
	public void pressEnterInTextInputWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Press the Enter key in the password input element with a specific id.
	 * 
	 * @param id
	 *            the value of the id attribute
	 * @throws NoSuchElementException
	 *            if there is no password input element with the specified id in
	 *            the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public void pressEnterInPasswordInputWithId (@HtmlAttribute("id") String id)
			throws NoSuchElementException, Exception;

	/**
	 * Press the Enter key in the password input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @throws NoSuchElementException
	 *            if there is no password input element with the specified name in
	 *            the current page
	 * @throws AmbiguousElementException
	 *            if there are multiple password input elements with the specified
	 *            name in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public void pressEnterInPasswordInputWithName (@HtmlAttribute("name") String name)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/**
	 * Press the Enter key in a password input element with a specific name.
	 * 
	 * @param name
	 *            the value of the name attribute
	 * @param which
	 *            specifies which of the matching input elements this method
	 *            should use (1-based)
	 * @throws NoSuchElementException
	 *            if there are less than the specified number of input elements
	 *            in the current page
	 * @throws Exception
	 *            if an error occurs
	 */
	@HtmlElement(name = "input", attributeName = "type", attributeValue = "password")
	public void pressEnterInPasswordInputWithName (@HtmlAttribute("name") String name,
			@HtmlElementIndex int which) throws NoSuchElementException, Exception;

	/**
	 * Find elements in the current page.
	 * 
	 * @param tagName
	 *            the tag name of the elements to look for
	 * @return all elements with the specified tag name
	 */
	public List<Element> getElementsByName (String tagName);

	/**
	 * Find elements in the current page.
	 * 
	 * @param tagName
	 *            the tag name of the elements to look for
	 * @param attName
	 *            the attribute name to restrict the search
	 * @param attValue
	 *            the attribute value to restrict the search
	 * @return all elements with the specified tag name having the specified
	 *            attribute with the specified value
	 */
	public List<Element> getElementsByName (String tagName, String attName,
			String attValue);

	/**
	 * Find elements in the current page.
	 * 
	 * @param tagName
	 *            the tag name of the elements to look for
	 * @param attributes
	 *            a mapping from attribute names to attribute values
	 * @return all elements with the specified tag name having all of the
	 *            specified attributes with their associated values
	 */
	public List<Element> getElementsByName (String tagName,
			Map<String, String> attributes);
}
