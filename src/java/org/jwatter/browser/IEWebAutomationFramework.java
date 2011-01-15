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
package org.jwatter.browser;

import java.net.URISyntaxException;

import watij.elements.Button;
import watij.elements.Checkbox;
import watij.elements.HtmlElement;
import watij.elements.HtmlElementCollections;
import watij.elements.Image;
import watij.elements.Link;
import watij.elements.Option;
import watij.elements.Radio;
import watij.elements.SelectList;
import watij.elements.TextArea;
import watij.elements.TextField;
import watij.finders.SymbolFactory;
import watij.runtime.ie.IE;

import org.jwatter.browser.finders.InnerTextFinder;
import org.jwatter.browser.finders.TextInputFinder;
import org.jwatter.browser.finders.UrlFinder;
import org.jwatter.html.NoSuchOptionException;
import org.jwatter.util.NotImplementedException;

/**
 * @author kschneider
 * 
 */
public class IEWebAutomationFramework extends BaseWebAutomationFramework
		implements WebAutomationFramework {

	protected IE browser;

	public IEWebAutomationFramework () {
		browser = null;
	}

	/**
	 * Creates a new Internet Explorer instance with a blank window.
	 * 
	 * @throws Exception
	 *             if an error occurs.
	 */
	@Override
	public void createBrowser () throws Exception {
		browser = new IE();
		browser.start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebFunctionalTestFramework#closeBrowser
	 * ()
	 */
	@Override
	public void closeBrowser () throws Exception {
		browser.close();
		browser = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebFunctionalTestFramework#getBrowserVersion
	 * ()
	 */
	@Override
	public String getBrowserVersion () {
		return "IE";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebFunctionalTestFramework#loadUrl(java
	 * .lang.String)
	 */
	@Override
	public void loadUrl (String url) throws Exception {
		browser.goTo(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.BaseWebAutomationFramework#back()
	 */
	@Override
	public void back () throws BrowserHistoryException, Exception {
		browser.back();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.BaseWebAutomationFramework#forward()
	 */
	@Override
	public void forward () throws BrowserHistoryException, Exception {
		browser.forward();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebFunctionalTestFramework#getUrl()
	 */
	@Override
	public String getUrl () throws Exception {
		return browser.url();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebFunctionalTestFramework#getTitle()
	 */
	@Override
	public String getTitle () throws Exception {
		return browser.title();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebFunctionalTestFramework#getContents()
	 */
	@Override
	public String getContents () throws Exception {
		return browser.text();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * hasFocusOnTextInputWithId(java.lang.String)
	 */
	@Override
	public boolean hasFocusOnTextInputWithId (String id)
			throws NoSuchElementException, Exception {
		return findTextInputWithId(id).equals(browser.active());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * hasFocusOnTextInputWithName(java.lang.String)
	 */
	@Override
	public boolean hasFocusOnTextInputWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return hasFocusOnTextInputWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * hasFocusOnTextInputWithName(java.lang.String, int)
	 */
	@Override
	public boolean hasFocusOnTextInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		return findTextInputWithName(name, which).equals(browser.active());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * hasFocusOnButtonWithId(java.lang.String)
	 */
	@Override
	public boolean hasFocusOnButtonWithId (String id)
			throws NoSuchElementException, Exception {
		return findButtonWithId(id).equals(browser.active());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * hasFocusOnButtonWithText(java.lang.String)
	 */
	@Override
	public boolean hasFocusOnButtonWithText (String text)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return hasFocusOnButtonWithText(text, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * hasFocusOnButtonWithText(java.lang.String, int)
	 */
	@Override
	public boolean hasFocusOnButtonWithText (String text, int which)
			throws NoSuchElementException, Exception {
		return findButtonWithText(text, which).equals(browser.active());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * hasFocusOnLinkWithText(java.lang.String)
	 */
	@Override
	public boolean hasFocusOnLinkWithText (String text)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return hasFocusOnLinkWithText(text, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * hasFocusOnLinkWithText(java.lang.String, int)
	 */
	@Override
	public boolean hasFocusOnLinkWithText (String text, int which)
			throws NoSuchElementException, Exception {
		return findLinkWithText(text, which).equals(browser.active());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * valueOfTextInputWithId(java.lang.String)
	 */
	@Override
	public String valueOfTextInputWithId (String id)
			throws NoSuchElementException, Exception {
		return findTextInputWithId(id).value();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * valueOfTextInputWithName(java.lang.String, int)
	 */
	@Override
	public String valueOfTextInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		return findTextInputWithName(name, which).value();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * valueOfPasswordInputWithId(java.lang.String)
	 */
	@Override
	public String valueOfPasswordInputWithId (String id)
			throws NoSuchElementException, Exception {
		return findPasswordInputWithId(id).value();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * valueOfPasswordInputWithName(java.lang.String, int)
	 */
	@Override
	public String valueOfPasswordInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		return findPasswordInputWithName(name, which).value();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickTextInputWithId
	 * (java.lang.String)
	 */
	@Override
	public void clickTextInputWithId (String id) throws NoSuchElementException,
			Exception {
		findTextInputWithId(id).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * clickTextInputWithName(java.lang.String, int)
	 */
	@Override
	public void clickTextInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		findTextInputWithName(name, which).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * clickPasswordInputWithId(java.lang.String)
	 */
	@Override
	public void clickPasswordInputWithId (String id)
			throws NoSuchElementException, Exception {
		findPasswordInputWithId(id).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * clickPasswordInputWithName(java.lang.String, int)
	 */
	@Override
	public void clickPasswordInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		findPasswordInputWithName(name, which).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickButtonWithId
	 * (java.lang.String)
	 */
	@Override
	public void clickButtonWithId (String id) throws NoSuchElementException,
			Exception {
		findButtonWithId(id).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickButtonWithText
	 * (java.lang.String, int)
	 */
	@Override
	public void clickButtonWithText (String text, int which)
			throws NoSuchElementException, Exception {
		findButtonWithText(text, which).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickLinkWithText
	 * (java.lang.String, int)
	 */
	@Override
	public void clickLinkWithText (String text, int which)
			throws NoSuchElementException, Exception {
		findLinkWithText(text, which).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickImage(java
	 * .lang.String, int)
	 */
	@Override
	public void clickImageWithUrl (String imageurl, int which)
			throws NoSuchElementException, URISyntaxException, Exception {
		findImageWithUrl(imageurl, which).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * focusOnTextInputWithId(java.lang.String)
	 */
	@Override
	public void focusOnTextInputWithId (String id)
			throws NoSuchElementException, Exception {
		findTextInputWithId(id).focus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * focusOnTextInputWithName(java.lang.String, int)
	 */
	@Override
	public void focusOnTextInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		findTextInputWithName(name, which).focus();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebFunctionalTestFramework#setTextInputById
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void setTextInputWithId (String id, String value)
			throws NoSuchElementException, Exception {
		findTextInputWithId(id).set(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#setTextInputWithName
	 * (java.lang.String, int, java.lang.String)
	 */
	@Override
	public void setTextInputWithName (String name, int which, String value)
			throws NoSuchElementException, Exception {
		findTextInputWithName(name, which).set(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * setPasswordInputWithId(java.lang.String, java.lang.String)
	 */
	@Override
	public void setPasswordInputWithId (String id, String value)
			throws NoSuchElementException, Exception {
		findPasswordInputWithId(id).set(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.WebAutomationFramework#
	 * setPasswordInputWithName(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void setPasswordInputWithName (String name, int which, String value)
			throws NoSuchElementException, Exception {
		findPasswordInputWithName(name, which).set(value);
	}

	@Override
	protected TextField findTextInputWithId (String id)
			throws NoSuchElementException, Exception {
		return (TextField) selectHtmlElement(browser.textFields(
				SymbolFactory.id, id).textFields(new TextInputFinder()), 0,
				"input[id=\"" + id + "\" type=\"text\"]");
	}

	@Override
	protected TextField findTextInputWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return (TextField) selectHtmlElement(browser.textFields(
				SymbolFactory.name, name).textFields(new TextInputFinder()),
				which, "input[name=\"" + name + "\" type=\"text\"]");
	}

	@Override
	protected TextField findPasswordInputWithId (String id)
			throws NoSuchElementException, Exception {
		return (TextField) selectHtmlElement(browser.textFields(
				SymbolFactory.id, id).textFields(
				new TextInputFinder("password")), 0, "input[id=\"" + id
				+ "\" type=\"password\"]");
	}

	@Override
	protected TextField findPasswordInputWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return (TextField) selectHtmlElement(browser.textFields(
				SymbolFactory.name, name).textFields(
				new TextInputFinder("password")), which, "input[name=\"" + name
				+ "\" type=\"password\"]");
	}

	@Override
	protected Checkbox findCheckboxWithId (String id)
			throws NoSuchElementException, Exception {
		return (Checkbox) selectHtmlElement(
				browser.checkboxes(SymbolFactory.id, id), 0,
				"input[id=\"" + id + "\" type=\"checkbox\"]");
	}

	@Override
	protected Checkbox findCheckboxWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return (Checkbox) selectHtmlElement(
				browser.checkboxes(SymbolFactory.name, name), which,
				"input[name=\"" + name + "\" type=\"checkbox\"]");
	}

	@Override
	protected Radio findRadioButtonWithId (String id)
			throws NoSuchElementException, Exception {
		return (Radio) selectHtmlElement(
				browser.radios(SymbolFactory.id, id), 0,
				"input[id=\"" + id + "\" type=\"radio\"]");
	}

	@Override
	protected Radio findRadioButtonWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return (Radio) selectHtmlElement(
				browser.radios(SymbolFactory.name, name), which,
				"input[name=\"" + name + "\" type=\"radio\"]");
	}

	@Override
	protected Button findButtonWithId (String id)
			throws NoSuchElementException, Exception {
		return (Button) selectHtmlElement(
				browser.buttons(SymbolFactory.id, id), 0, "button[id=\"" + id
						+ "\"]");
	}

	@Override
	protected Button findButtonWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return (Button) selectHtmlElement(
				browser.buttons(SymbolFactory.name, name), which,
				"button[name=\"" + name + "\"]");
	}

	@Override
	protected Button findButtonWithText (String text, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return (Button) selectHtmlElement(browser.buttons(new InnerTextFinder(
				text)), which, "button[text=\"" + text + "\"]");
	}

	@Override
	protected Button findButtonWithTitle (String title, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return (Button) selectHtmlElement(browser.buttons(SymbolFactory.title, title),
				which, "button[title=\"" + title + "\"]");
	}

	@Override
	protected TextArea findTextareaWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	@Override
	protected TextArea findTextareaWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		throw new NotImplementedException();
	}

	@Override
	protected SelectList findDropDownMenuWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	@Override
	protected SelectList findDropDownMenuWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		throw new NotImplementedException();
	}

	@Override
	protected Option findOptionInDropDownMenuWithId (String id, String option)
			throws NoSuchElementException, NoSuchOptionException, Exception {
		throw new NotImplementedException();
	}

	@Override
	protected Option findOptionInDropDownMenuWithName (String name, int which,
			String option) throws NoSuchElementException, NoSuchOptionException,
			Exception {
		throw new NotImplementedException();
	}

	@Override
	protected Link findLinkWithText (String text, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return (Link) selectHtmlElement(browser
				.links(new InnerTextFinder(text)), which, "link[text=\"" + text
				+ "\"]");
	}

	@Override
	protected Image findImageWithUrl (String url, int which)
			throws NoSuchElementException, AmbiguousElementException,
			URISyntaxException, Exception {
		return (Image) selectHtmlElement(browser.images(new UrlFinder("src",
				url, browser.url())), which, "img[src=\"" + url + "\"]");
	}

	@Override
	protected Object findSpanWithClass ( String cssclass, int which )
			throws NoSuchElementException, AmbiguousElementException, Exception
	{
		throw new NotImplementedException();
	}

	protected HtmlElement selectHtmlElement (
			HtmlElementCollections<? extends HtmlElement> elements, int which,
			String elementDescription) throws NoSuchElementException,
			AmbiguousElementException, Exception {
		if( elements.length() > 0 ) {
			if( which == 0 ) {
				if( elements.length() > 1 ) {
					throw new AmbiguousElementException("ambiguous element: "
							+ elementDescription);
				} else {
					return elements.get(0);
				}
			} else if( which > elements.length() ) {
				throw new NoSuchElementException(which, elements.length(),
						"no such element: " + elementDescription);
			} else {
				return elements.get(which - 1);
			}
		} else {
			throw new NoSuchElementException("no such element: "
					+ elementDescription);
		}
	}

}
