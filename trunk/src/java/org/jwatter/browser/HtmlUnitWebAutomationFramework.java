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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

import org.jwatter.html.NoSuchOptionException;
import org.jwatter.util.ListFilter;
import org.jwatter.util.NotImplementedException;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ElementNotFoundException;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlImage;
import com.gargoylesoftware.htmlunit.html.HtmlInput;
import com.gargoylesoftware.htmlunit.html.HtmlOption;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlPasswordInput;
import com.gargoylesoftware.htmlunit.html.HtmlRadioButtonInput;
import com.gargoylesoftware.htmlunit.html.HtmlSelect;
import com.gargoylesoftware.htmlunit.html.HtmlTextArea;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

/**
 * @author kschneider
 * 
 */
public class HtmlUnitWebAutomationFramework extends BaseWebAutomationFramework
		implements WebAutomationFramework {

	protected static Logger logger = Logger
			.getLogger(HtmlUnitWebAutomationFramework.class.getPackage()
					.getName());

	protected static BrowserVersion DEFAULT_BROWSER_VERSION = BrowserVersion.INTERNET_EXPLORER_7;

	protected WebClientWithHistory browser;

	protected BrowserVersion browserVersion;

	protected HtmlPage page;

	public HtmlUnitWebAutomationFramework () {
		this(DEFAULT_BROWSER_VERSION);
	}

	public HtmlUnitWebAutomationFramework (BrowserVersion browserVersion) {
		browser = null;
		this.browserVersion = browserVersion;
	}

	/**
	 * Creates a new HtmlUnit web client.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Override
	public void createBrowser () throws Exception {
		browser = new WebClientWithHistory(browserVersion);
		browser.setUseInsecureSSL(true);
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
		browser.closeAllWindows();
	}

	/**
	 * Enables/disables JavaScript support. Enabled by default.
	 * 
	 * @param enabled
	 *            if true, JavaScript support is enabled
	 */
	public void setJavaScriptEnabled (boolean enabled) {
		browser.setJavaScriptEnabled(enabled);
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
		return browserVersion.getApplicationName() + " "
				+ browserVersion.getApplicationVersion();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebFunctionalTestFramework#openUrl(java
	 * .lang.String)
	 */
	@Override
	public void loadUrl (String url) throws Exception {
		page = browser.getPage(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.BaseWebAutomationFramework#back()
	 */
	@Override
	public void back () throws BrowserHistoryException, Exception {
		page = browser.back();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.BaseWebAutomationFramework#forward()
	 */
	@Override
	public void forward () throws BrowserHistoryException, Exception {
		page = browser.forward();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebFunctionalTestFramework#getContents()
	 */
	@Override
	public String getContents () throws Exception {
		return page.asText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebFunctionalTestFramework#getTitle()
	 */
	@Override
	public String getTitle () throws Exception {
		return page.getTitleText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#setTextInputWithId
	 * (java.lang.String, java.lang.String)
	 */
	@Override
	public void setTextInputWithId (String id, String value)
			throws NoSuchElementException, Exception {
		findTextInputWithId(id).setValueAttribute(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebFunctionalTestFramework#getUrl()
	 */
	@Override
	public String getUrl () throws Exception {
		String url = page.getWebResponse().getRequestUrl().toString();
		if( url.startsWith("file:/") && !url.startsWith("file:///") ) {
			return "file:///" + url.substring(6);
		} else {
			return url;
		}
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
		findTextInputWithName(name, which).setValueAttribute(value);
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
		findPasswordInputWithId(id).setValueAttribute(value);
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
		findPasswordInputWithName(name, which).setValueAttribute(value);
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
		page = findButtonWithId(id).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.BaseWebAutomationFramework#
	 * clickButtonWithText(java.lang.String, int)
	 */
	@Override
	public void clickButtonWithText (String text, int which)
			throws NoSuchElementException, Exception {
		page = findButtonWithText(text, which).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.BaseWebAutomationFramework#
	 * clickTextInputWithId(java.lang.String)
	 */
	@Override
	public void clickTextInputWithId (String id) throws NoSuchElementException,
			Exception {
		page = findTextInputWithId(id).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.BaseWebAutomationFramework#
	 * clickTextInputWithName(java.lang.String, int)
	 */
	@Override
	public void clickTextInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		page = findTextInputWithName(name, which).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.BaseWebAutomationFramework#
	 * clickPasswordInputWithId(java.lang.String)
	 */
	@Override
	public void clickPasswordInputWithId (String id)
			throws NoSuchElementException, Exception {
		page = findPasswordInputWithId(id).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.BaseWebAutomationFramework#
	 * clickPasswordInputWithName(java.lang.String, int)
	 */
	@Override
	public void clickPasswordInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		page = findPasswordInputWithName(name, which).click();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#clickCheckboxWithId(java.lang.String)
	 */
	@Override
	public void clickCheckboxWithId (String id) throws NoSuchElementException,
			Exception {
		page = findCheckboxWithId(id).click();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#clickCheckboxWithName(java.lang.String, int)
	 */
	@Override
	public void clickCheckboxWithName (String name, int which) throws
			NoSuchElementException, Exception {
		page = findCheckboxWithName(name, which).click();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#clickRadioButtonWithId(java.lang.String)
	 */
	@Override
	public void clickRadioButtonWithId (String id) throws NoSuchElementException,
			Exception {
		page = findRadioButtonWithId(id).click();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#clickRadioButtonWithName(java.lang.String, int)
	 */
	@Override
	public void clickRadioButtonWithName (String name, int which) throws
			NoSuchElementException, Exception {
		page = findRadioButtonWithName(name, which).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.BaseWebAutomationFramework#clickImageWithUrl
	 * (java.lang.String, int)
	 */
	@Override
	public void clickImageWithUrl (String imageurl, int which)
			throws NoSuchElementException, URISyntaxException, Exception {
		page = ((HtmlElement) findImageWithUrl(imageurl, which)).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.BaseWebAutomationFramework#clickLinkWithText
	 * (java.lang.String, int)
	 */
	@Override
	public void clickLinkWithText (String text, int which)
			throws NoSuchElementException, Exception {
		page = findLinkWithText(text, which).click();
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
		return findTextInputWithId(id).getValueAttribute();
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
		return findTextInputWithName(name, which).getValueAttribute();
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
		return findPasswordInputWithId(id).getValueAttribute();
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
		return findPasswordInputWithName(name, which).getValueAttribute();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.jwatter.BaseWebAutomationFramework#
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
	 * @seeorg.jwatter.BaseWebAutomationFramework#
	 * focusOnTextInputWithName(java.lang.String, int)
	 */
	@Override
	public void focusOnTextInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		findTextInputWithName(name, which).focus();
	}

	protected HtmlElement findElementWithId (String tagname, String id)
			throws Exception {
		HtmlElement el;
		try {
			el = page.getHtmlElementById(id);
		} catch( ElementNotFoundException e ) {
			throw new NoSuchElementException("id", id);
		}
		if( tagname.equals(el.getTagName()) ) {
			return el;
		} else {
			throw new NoSuchElementException(tagname, "id", id);
		}
	}

	protected HtmlElement findHtmlElementWithXPath (String xpathExpr, int which)
			throws Exception {
		return selectHtmlElement(page.getByXPath(xpathExpr), which);
	}

	protected HtmlInput findInputElementWithId (String id, String type)
			throws Exception {
		HtmlInput input = (HtmlInput) findElementWithId("input", id);
		if( type.equals(input.getTypeAttribute()) ) {
			return input;
		} else {
			throw new NoSuchElementException("input[id=" + id + ", type="
					+ type + "]: no such element");
		}
	}

	protected HtmlInput findInputElementWithName (String name, String type,
			int which) throws Exception {
		return (HtmlInput) findHtmlElementWithXPath("//input[@name='" + name
				+ "' and @type='" + type + "']", which);
	}

	@Override
	protected HtmlTextInput findTextInputWithId (String id) throws Exception {
		return (HtmlTextInput) findInputElementWithId(id, "text");
	}

	@Override
	protected HtmlTextInput findTextInputWithName (String name, int which)
			throws Exception {
		return (HtmlTextInput) findInputElementWithName(name, "text", which);
	}

	@Override
	protected HtmlPasswordInput findPasswordInputWithId (String id)
			throws Exception {
		return (HtmlPasswordInput) findInputElementWithId(id, "password");
	}

	@Override
	protected HtmlPasswordInput findPasswordInputWithName (String name,
			int which) throws Exception {
		return (HtmlPasswordInput) findInputElementWithName(name, "password",
				which);
	}

	@Override
	protected HtmlCheckBoxInput findCheckboxWithId (String id) throws Exception {
		return (HtmlCheckBoxInput) findInputElementWithId(id, "checkbox");
	}

	@Override
	protected HtmlCheckBoxInput findCheckboxWithName (String name, int which)
			throws Exception {
		return (HtmlCheckBoxInput) findInputElementWithName(name, "checkbox",
				which);
	}

	@Override
	protected HtmlRadioButtonInput findRadioButtonWithId (String id) throws
			Exception {
		return (HtmlRadioButtonInput) findInputElementWithId(id, "radio");
	}

	@Override
	protected HtmlRadioButtonInput findRadioButtonWithName (String name,
			int which) throws Exception {
		return (HtmlRadioButtonInput) findInputElementWithName(name, "radio", which);
	}

	@Override
	protected HtmlTextArea findTextareaWithId (String id) throws Exception {
		return (HtmlTextArea) findElementWithId("textarea", id);
	}

	@Override
	protected HtmlTextArea findTextareaWithName (String name, int which)
			throws Exception {
		throw new NotImplementedException();
	}

	@Override
	protected HtmlElement findButtonWithId (String id)
			throws NoSuchElementException {
		HtmlElement element;
		try {
			element = page.getHtmlElementById(id);
		} catch( ElementNotFoundException e ) {
			throw new NoSuchElementException("id", id);
		}
		if( isButton(element) ) {
			return element;
		}
		throw new NoSuchElementException("button", "id", id);
	}

	@Override
	protected HtmlElement findButtonWithName (String name, int which)
			throws Exception {
		return selectHtmlElement(page
						.getByXPath("//*[(name()='button' or (name()='input' and @type='submit')) and @name='"
								+ name + "']"), which);
	}

	@Override
	protected HtmlElement findButtonWithText (String text, int which)
			throws Exception {
		return selectHtmlElement(page
						.getByXPath("//*[(name()='button' or (name()='input' and @type='submit')) and .=\""
								+ text + "\"]"), which);
	}

	@Override
	protected HtmlElement findButtonWithTitle (String title, int which)
			throws Exception {
		return selectHtmlElement(page
						.getByXPath("//*[(name()='button' or (name()='input' and @type='submit')) and @title='"
								+ title + "']"), which);
	}

	@Override
	protected HtmlSelect findDropDownMenuWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	@Override
	protected HtmlSelect findDropDownMenuWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		throw new NotImplementedException();
	}

	@Override
	protected HtmlOption findOptionInDropDownMenuWithId (String id, String option)
			throws NoSuchElementException, NoSuchOptionException, Exception {
		throw new NotImplementedException();
	}

	@Override
	protected HtmlOption findOptionInDropDownMenuWithName (String name, int which,
			String option) throws NoSuchElementException, NoSuchOptionException,
			Exception {
		throw new NotImplementedException();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected HtmlImage findImageWithUrl (final String imageurl, int which)
			throws NoSuchElementException, AmbiguousElementException,
			URISyntaxException, Exception {
		List<HtmlImage> images = (List<HtmlImage>) page
				.getByXPath("//img[@src]");
		final URI baseURI = new URI(getUrl());
		final URI imageURI = baseURI.resolve(imageurl);
		ListFilter<HtmlImage> urlFilter = new ListFilter<HtmlImage>() {
			@Override
			public boolean eval (HtmlImage element) {
				URI uri = baseURI.resolve(element.getSrcAttribute());
				return imageURI.equals(uri);
			}
		};
		return (HtmlImage) selectHtmlElement(urlFilter.filter(images), which);
	}

	@Override
	protected HtmlAnchor findLinkWithText (final String text, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return (HtmlAnchor) findHtmlElementWithXPath("//a[.=\"" + text + "\"]",
				which);
	}

	@Override
	protected Object findSpanWithClass ( String cssclass, int which )
			throws NoSuchElementException, AmbiguousElementException, Exception
	{
		throw new NotImplementedException();
	}

	protected HtmlElement selectHtmlElement (List<?> elements, int which)
			throws Exception {
		if( elements.size() > 0 ) {
			if( which == 0 ) {
				if( elements.size() > 1 ) {
					throw new AmbiguousElementException();
				} else {
					return (HtmlElement) elements.get(0);
				}
			} else if( which > elements.size() ) {
				throw new NoSuchElementException();
			} else {
				return (HtmlElement) elements.get(which - 1);
			}
		} else {
			throw new NoSuchElementException();
		}
	}

	protected boolean isButton (HtmlElement element) {
		return "button".equals(element.getTagName())
				|| ("input".equals(element.getTagName()) && "submit"
						.equals(element.getAttribute("type")));
	}

}
