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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import org.jwatter.browser.finders.By;
import org.jwatter.browser.finders.ByUrlFilter;
import org.jwatter.html.Element;
import org.jwatter.html.NoSuchOptionException;
import org.jwatter.html.WebDriverElement;

/**
 * An implementation of the web automation framework interface for WebDriver.
 * 
 * @author kschneider
 * 
 */
public abstract class WebDriverWebAutomationFramework
		extends BaseWebAutomationFramework
		implements WebAutomationFramework {

	protected static Logger logger = Logger
			.getLogger(WebDriverWebAutomationFramework.class.getPackage()
					.getName());

	protected WebDriver browser;
	protected LinkedHashMap<String, String> windowNames;
	protected String defaultWindowHandle;

	/**
	 * Creates a new instance.
	 */
	public WebDriverWebAutomationFramework () {
		browser = null;
		windowNames = new LinkedHashMap<String, String>();
		defaultWindowHandle = null;
	}
	
	protected void initBrowser () {
		defaultWindowHandle = browser.getWindowHandle();
		windowNames.put(defaultWindowHandle, defaultWindowHandle);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#closeBrowser()
	 */
	@Override
	public void closeBrowser () throws Exception {
		browser.quit();
		browser = null;
		windowNames.clear();
		defaultWindowHandle = null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#closeWindow()
	 */
	@Override
	public void closeWindow () throws Exception {
		String windowname = browser.getWindowHandle();
		windowNames.remove(windowname);
		if( windowname.equals(defaultWindowHandle) ) {
			defaultWindowHandle = null;
		}
		browser.close();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.BaseWebAutomationFramework#isBrowserOpen()
	 */
	@Override
	public boolean isBrowserOpen () {
		return browser != null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#loadUrl(java.
	 * lang.String)
	 */
	@Override
	public void loadUrl (String url) throws Exception {
		browser.get(url);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.BaseWebAutomationFramework#back()
	 */
	@Override
	public void back () throws Exception {
		browser.navigate().back();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.BaseWebAutomationFramework#forward()
	 */
	@Override
	public void forward () throws Exception {
		browser.navigate().forward();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#disableJavascriptAlert()
	 */
	@Override
	public void disableJavascriptAlert () throws Exception {
		((JavascriptExecutor) browser).executeScript(
				"if(typeof(window.__webdriver_savedalert)==undefined||window.__webdriver_savedalert==undefined){window.__webdriver_savedalert=window.alert;window.alert=function(m){};};");
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#enableJavascriptAlert()
	 */
	@Override
	public void enableJavascriptAlert () throws Exception {
		((JavascriptExecutor) browser).executeScript(
				"if(typeof(window.__webdriver_savedalert)!=undefined&&window.__webdriver_savedalert!=undefined){window.alert=window.__webdriver_savedalert;window.__webdriver_savedalert=undefined;};");
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#disableJavascriptConfirm(boolean)
	 */
	@Override
	public void disableJavascriptConfirm (boolean confirm) throws Exception {
		((JavascriptExecutor) browser).executeScript(
				"if(typeof(window.__webdriver__savedconfirm)==undefined||window.__webdriver_savedconfirm==undefined){window.__webdriver_savedconfirm=window.confirm;window.confirm=function(m){return "+confirm+";};};");
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#enableJavascriptConfirm()
	 */
	@Override
	public void enableJavascriptConfirm () throws Exception {
		((JavascriptExecutor) browser).executeScript(
				"if(typeof(window.__webdriver__savedconfirm)!=undefined&&window.__webdriver_savedconfirm!=undefined){window.confirm=window.__webdriver_savedconfirm;window.__webdriver_savedconfirm=undefined;};");
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#setTargetToFrame(int)
	 */
	@Override
	public void setTargetToFrame (int frameIndex) throws Exception {
		browser.switchTo().frame(frameIndex);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#setTargetToFrameWithId(java.lang.String)
	 */
	@Override
	public void setTargetToFrameWithId (String frameAddress) throws Exception {
		browser.switchTo().frame(frameAddress);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#setTargetToFrameWithName(java.lang.String)
	 */
	@Override
	public void setTargetToFrameWithName (String frameAddress) throws Exception {
		browser.switchTo().frame(frameAddress);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#setTargetToWindow(java.lang.String)
	 */
	@Override
	public void setTargetToWindow (String windowName) throws Exception {
		syncWindowNames();
		if (windowName != null) {
			browser.switchTo().window(windowName);
			windowNames.put(browser.getWindowHandle(), windowName);
		}
		else {
			browser.switchTo().window(defaultWindowHandle);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#getWindowName()
	 */
	@Override
	public String getWindowName () throws Exception {
		syncWindowNames();
		return windowNames.get(browser.getWindowHandle());
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#getWindowNames()
	 */
	@Override
	public List<String> getWindowNames () throws Exception {
		syncWindowNames();
		List<String> names = new ArrayList<String>();
		names.addAll(windowNames.values());
		return names;
	}

	@Override
	public String getMostRecentWindowName () throws Exception {
		List<String> windownames = getWindowNames();
		return windownames.get(windownames.size() - 1);
	}
	
	protected void syncWindowNames () throws Exception {
		Set<String> windowhandles = browser.getWindowHandles();

		// remove window names that are no longer open
		windowNames.keySet().retainAll(windowhandles);

		// add window handles as names for windows not in the list
		for( String handle : windowhandles ) {
			if( !windowNames.containsKey(handle) ) {
				windowNames.put(handle, handle);
			}
		}		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#getUrl()
	 */
	@Override
	public String getUrl () throws Exception {
		return browser.getCurrentUrl();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#getTitle()
	 */
	@Override
	public String getTitle () throws Exception {
		return browser.getTitle();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#getContents()
	 */
	@Override
	public String getContents () throws Exception {
		return browser.findElement(By.xpath("//body")).getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickButtonWithId
	 * (java.lang.String)
	 */
	@Override
	public void clickButtonWithId (String id) throws NoSuchElementException {
		findButtonWithId(id).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.BaseWebAutomationFramework#clickButtonWithName(java.lang.String, int)
	 */
	@Override
	public void clickButtonWithName (String name, int which)
			throws NoSuchElementException, Exception {
		findButtonWithName(name, which).click();
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
	 * org.jwatter.WebAutomationFramework#clickButtonWithTitle
	 * (java.lang.String, int)
	 */
	@Override
	public void clickButtonWithTitle (String title, int which)
			throws NoSuchElementException, Exception {
		findButtonWithTitle(title, which).click();
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
	 * org.jwatter.WebAutomationFramework#clickTextInputWithId
	 * (java.lang.String)
	 */
	@Override
	public void clickTextInputWithId (String id) throws NoSuchElementException {
		findTextInputWithId(id).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
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
	 * @see org.jwatter.WebAutomationFramework#
	 * clickPasswordInputWithId(java.lang.String)
	 */
	@Override
	public void clickPasswordInputWithId (String id)
			throws NoSuchElementException {
		findPasswordInputWithId(id).click();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * clickPasswordInputWithName(java.lang.String, int)
	 */
	@Override
	public void clickPasswordInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		findPasswordInputWithName(name, which).click();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#clickRadioButtonWithId(java.lang.String)
	 */
	@Override
	public void clickRadioButtonWithId (String id)
			throws NoSuchElementException {
		findRadioButtonWithId(id).click();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#clickRadioButtonWithName(java.lang.String, int)
	 */
	@Override
	public void clickRadioButtonWithName (String name, int which)
			throws NoSuchElementException, Exception {
		findRadioButtonWithName(name, which).click();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#clickCheckboxWithId(java.lang.String)
	 */
	@Override
	public void clickCheckboxWithId (String id)
			throws NoSuchElementException {
		findCheckboxWithId(id).click();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#clickCheckboxWithName(java.lang.String, int)
	 */
	@Override
	public void clickCheckboxWithName (String name, int which)
			throws NoSuchElementException, Exception {
		findCheckboxWithName(name, which).click();
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
			throws NoSuchElementException {
		WebElement input = findTextInputWithId(id);
		input.clear();
		input.sendKeys(value);
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
		WebElement input = findTextInputWithName(name, which);
		input.clear();
		input.sendKeys(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * setPasswordInputWithId(java.lang.String, java.lang.String)
	 */
	@Override
	public void setPasswordInputWithId (String id, String value)
			throws NoSuchElementException {
		WebElement input = findPasswordInputWithId(id);
		input.clear();
		input.sendKeys(value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * setPasswordInputWithName(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void setPasswordInputWithName (String name, int which, String value)
			throws NoSuchElementException, Exception {
		WebElement input = findPasswordInputWithName(name, which);
		input.clear();
		input.sendKeys(value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#setTextareaWithId(java.lang.String, java.lang.String)
	 */
	@Override
	public void setTextareaWithId (String id, String value)
			throws NoSuchElementException {
		WebElement textarea = findTextareaWithId(id);
		textarea.clear();
		textarea.sendKeys(value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#setTextareaWithName(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void setTextareaWithName (String name, int which, String value)
			throws NoSuchElementException, Exception {
		WebElement textarea = findTextareaWithName(name, which);
		textarea.clear();
		textarea.sendKeys(value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#optionsInDropDownMenuWithId(java.lang.String)
	 */
	@Override
	public String[] optionsInDropDownMenuWithId (String id) throws
			NoSuchElementException {
		List<WebElement> options = findDropDownMenuWithId(id)
				.findElements(By.tagName("option"));
		String[] optiontexts = new String[options.size()];
		int i = 0;
		for( WebElement option : options ) {
			optiontexts[i++] = option.getText();
		}
		return optiontexts;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#optionsInDropDownMenuWithName(java.lang.String, int)
	 */
	@Override
	public String[] optionsInDropDownMenuWithName (String name, int which) throws
			NoSuchElementException, Exception {
		List<WebElement> options = findDropDownMenuWithName(name, which)
				.findElements(By.tagName("option"));
		String[] optiontexts = new String[options.size()];
		int i = 0;
		for( WebElement option : options ) {
			optiontexts[i++] = option.getText();
		}
		return optiontexts;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#selectOptionFromDropDownMenuWithId(java.lang.String, java.lang.String)
	 */
	@Override
	public void selectOptionFromDropDownMenuWithId (String id, String option)
			throws NoSuchElementException, NoSuchOptionException {
		findOptionInDropDownMenuWithId(id, option).setSelected();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#selectOptionFromDropDownMenuWithName(java.lang.String, int, java.lang.String)
	 */
	@Override
	public void selectOptionFromDropDownMenuWithName (String name, int which,
			String option) throws NoSuchElementException, NoSuchOptionException,
			Exception {
		findOptionInDropDownMenuWithName(name, which, option).setSelected();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#pressEnterInTextInputWithId(java.lang.String)
	 */
	@Override
	public void pressEnterInTextInputWithId (String id)
			throws NoSuchElementException {
		findTextInputWithId(id).sendKeys(Keys.ENTER);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#pressEnterInTextInputWithName(java.lang.String, int)
	 */
	@Override
	public void pressEnterInTextInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		findTextInputWithName(name, which).sendKeys(Keys.ENTER);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#pressEnterInPasswordInputWithId(java.lang.String)
	 */
	@Override
	public void pressEnterInPasswordInputWithId (String id)
			throws NoSuchElementException {
		findPasswordInputWithId(id).sendKeys(Keys.ENTER);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#pressEnterInPasswordInputWithName(java.lang.String, int)
	 */
	@Override
	public void pressEnterInPasswordInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		findPasswordInputWithName(name, which).sendKeys(Keys.ENTER);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * valueOfTextInputWithId(java.lang.String)
	 */
	@Override
	public String valueOfTextInputWithId (String id)
			throws NoSuchElementException {
		return findTextInputWithId(id).getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * valueOfTextInputWithName(java.lang.String, int)
	 */
	@Override
	public String valueOfTextInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		return findTextInputWithName(name, which).getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * valueOfPasswordInputWithId(java.lang.String)
	 */
	@Override
	public String valueOfPasswordInputWithId (String id)
			throws NoSuchElementException {
		return findPasswordInputWithId(id).getValue();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * valueOfPasswordInputWithName(java.lang.String, int)
	 */
	@Override
	public String valueOfPasswordInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		return findPasswordInputWithName(name, which).getValue();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#valueOfTextareaWithId(java.lang.String)
	 */
	@Override
	public String valueOfTextareaWithId (String id)
			throws NoSuchElementException {
		return findTextareaWithId(id).getText();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#valueOfTextareaWithName(java.lang.String, int)
	 */
	@Override
	public String valueOfTextareaWithName (String name, int which)
			throws NoSuchElementException, Exception {
		return findTextareaWithName(name, which).getText();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * isCheckedCheckboxWithId(java.lang.String)
	 */
	@Override
	public boolean isCheckedCheckboxWithId (String id)
			throws NoSuchElementException {
		return findCheckboxWithId(id).isSelected();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * isCheckedCheckboxWithName(java.lang.String, int)
	 */
	@Override
	public boolean isCheckedCheckboxWithName (String name, int which)
			throws NoSuchElementException, Exception {
		return findCheckboxWithName(name, which).isSelected();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * isCheckedRadioButtonWithId(java.lang.String)
	 */
	@Override
	public boolean isCheckedRadioButtonWithId (String id)
			throws NoSuchElementException {
		return findRadioButtonWithId(id).isSelected();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * isCheckedRadioButtonWithName(java.lang.String, int)
	 */
	@Override
	public boolean isCheckedRadioButtonWithName (String name, int which)
			throws NoSuchElementException, Exception {
		return findRadioButtonWithName(name, which).isSelected();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#selectedOptionInDropDownMenuWithId(java.lang.String)
	 */
	@Override
	public String selectedOptionInDropDownMenuWithId (String id) throws
			NoSuchElementException {
		WebElement select = findDropDownMenuWithId(id);
		List<WebElement> options = select.findElements(By.tagName("option"));
		for( WebElement option : options ) {
			if( option.isSelected() ) {
				return option.getText();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#selectedOptionInDropDownMenuWithName(java.lang.String, int)
	 */
	@Override
	public String selectedOptionInDropDownMenuWithName (String name, int which)
			throws NoSuchElementException, Exception {
		WebElement select = findDropDownMenuWithName(name, which);
		List<WebElement> options = select.findElements(By.tagName("option"));
		for( WebElement option : options ) {
			if( option.isSelected() ) {
				return option.getText();
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#contentsOfSpanWithClass(java.lang.String, int)
	 */
	@Override
	public String contentsOfSpanWithClass (String cssclass, int which) throws
			NoSuchElementException, Exception {
		return findSpanWithClass(cssclass, which).getText();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.BaseWebAutomationFramework#contentsOfSpansWithClass(java.lang.String)
	 */
	@Override
	public String[] contentsOfSpansWithClass (String cssclass) throws Exception {
		List<WebElement> spans = findSpansWithClass(cssclass);
		String[] contents = new String[spans.size()];
		for (int i = 0; i < spans.size(); i++) {
			contents[i] = spans.get(i).getText();
		}
		return contents;
	}

	@Override
	protected WebElement findTextInputWithId (String id)
			throws NoSuchElementException {
		WebElement element = findElementWithId(id);
		if( !isTextInputElement(element) ) {
			throw new NoSuchElementException("input", "id", id, "type", "text");
		}
		return element;
	}

	@Override
	protected WebElement findPasswordInputWithId (String id)
			throws NoSuchElementException {
		WebElement element = findElementWithId(id);
		if( !isPasswordInputElement(element) ) {
			throw new NoSuchElementException("input", "id", id, "type",
					"password");
		}
		return element;
	}

	@Override
	protected WebElement findTextareaWithId (String id)
			throws NoSuchElementException {
		WebElement element = findElementWithId(id);
		if( !"textarea".equals(element.getTagName()) ) {
			throw new NoSuchElementException("textarea", "id", id);
		}
		return element;
	}

	@Override
	protected WebElement findCheckboxWithId (String id)
			throws NoSuchElementException {
		WebElement element = findElementWithId(id);
		if( !isCheckboxElement(element) ) {
			throw new NoSuchElementException("input", "id", id, "type", "checkbox");
		}
		return element;
	}

	@Override
	protected WebElement findRadioButtonWithId (String id)
			throws NoSuchElementException {
		WebElement element = findElementWithId(id);
		if( !isRadioButtonElement(element) ) {
			throw new NoSuchElementException("input", "id", id, "type", "radio");
		}
		return element;
	}

	@Override
	protected WebElement findButtonWithId (String id)
			throws NoSuchElementException {
		WebElement element = findElementWithId(id);
		if( !isButtonElement(element) ) {
			throw new NoSuchElementException("button", "id", id);
		}
		return element;
	}

	@Override
	protected WebElement findDropDownMenuWithId (String id)
			throws NoSuchElementException {
		WebElement element = findElementWithId(id);
		if( !"select".equals(element.getTagName()) ) {
			throw new NoSuchElementException("select", "id", id);
		}
		return element;
	}

	protected WebElement findElementWithId (String id)
			throws NoSuchElementException {
		try {
			return browser.findElement(By.id(id));
		} catch( org.openqa.selenium.NoSuchElementException e ) {
			throw new NoSuchElementException("id", id);
		}
	}

	@Override
	protected WebElement findTextInputWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException {
		return findElementWithXPath("//input[@type='text' and @name='" + name
				+ "']", which);
	}

	@Override
	protected WebElement findPasswordInputWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException {
		return findElementWithXPath("//input[@type='password' and @name='"
				+ name + "']", which);
	}

	@Override
	protected WebElement findTextareaWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException {
		return new ElementSelector<WebElement>() {
			@Override
			public boolean eval ( WebElement element ) {
				return "textarea".equals(element.getTagName());
			}
		}.filter(browser.findElements(By.name(name)), which, "textarea",
				"name", name);
	}

	@Override
	protected WebElement findCheckboxWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException {
		return findElementWithXPath("//input[@type='checkbox' and @name='"
				+ name + "']", which);
	}

	@Override
	protected WebElement findRadioButtonWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException {
		return findElementWithXPath("//input[@type='radio' and @name='"
				+ name + "']", which);
	}

	@Override
	protected WebElement findButtonWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException {
		return findElementWithXPath(
				"//*[(((name()='input' or name()='INPUT') and @type='submit') or name()='button' or name()='BUTTON') and @name='"
						+ name + "']", which);
	}

	@Override
	protected WebElement findButtonWithText (String text, int which)
			throws NoSuchElementException, AmbiguousElementException {
		return findElementWithXPath(
				"//*[(((name()='input' or name()='INPUT') and @type='submit') or name()='button' or name()='BUTTON') and .=\""
						+ text + "\"]", which);
	}

	@Override
	protected WebElement findButtonWithTitle (String title, int which)
			throws NoSuchElementException, AmbiguousElementException {
		return findElementWithXPath(
				"//*[(((name()='input' or name()='INPUT') and @type='submit') or name()='button' or name()='BUTTON') and @title='"
						+ title + "']", which);
	}

	@Override
	protected WebElement findDropDownMenuWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException {
		return new ElementSelector<WebElement>() {
			@Override
			public boolean eval ( WebElement element ) {
				return "select".equals(element.getTagName());
			}
		}.filter(browser.findElements(By.name(name)), which, "select",
				"name", name);
	}

	@Override
	protected WebElement findLinkWithText (String text, int which)
			throws NoSuchElementException, AmbiguousElementException {
		return new ElementSelector<WebElement> () {
			@Override
			public boolean eval ( WebElement element ) {
				return true;
			}
		}.filter(browser.findElements(By.linkText(text)), which,
				"a \"" + text + "\"");
	}

	@Override
	protected WebElement findImageWithUrl (String url, int which)
			throws NoSuchElementException, AmbiguousElementException,
			URISyntaxException {
		return new ElementSelector<WebElement> () {
			@Override
			public boolean eval ( WebElement element ) {
				return true;
			}
		}.filter(browser.findElements(new ByUrlFilter(
				By.tag("img"), "src", url, browser.getCurrentUrl())), which,
				"img", "src", url);
	}
	
	@Override
	protected WebElement findSpanWithClass (String cssclass, int which)
			throws NoSuchElementException, AmbiguousElementException {
		return new ElementSelector<WebElement> () {
			@Override
			public boolean eval ( WebElement element ) {
				return "span".equals(element.getTagName());
			}
		}.filter(browser.findElements(By.className(cssclass)),
				which, "span", "class", cssclass);
	}

	protected List<WebElement> findSpansWithClass (String cssclass) {
		return new ElementSelector<WebElement> () {
			@Override
			public boolean eval ( WebElement element ) {
				return "span".equals(element.getTagName());
			}
		}.filter(browser.findElements(By.className(cssclass)));
	}

	protected WebElement findElementWithXPath (String xpathExpression, int which)
			throws NoSuchElementException, AmbiguousElementException {
		try {
			return new ElementSelector<WebElement> () {
				@Override
				public boolean eval ( WebElement element ) {
					return true;
				}
			}.filter(browser
					.findElements(By.xpath(xpathExpression)), which,
					xpathExpression);
		} catch( RuntimeException e ) {
			logger.info("xpath=\"" + xpathExpression + "\"");
			throw e;
		}
	}

	@Override
	protected WebElement findOptionInDropDownMenuWithId (String id, String option)
			throws NoSuchElementException, NoSuchOptionException {
		WebElement select = findDropDownMenuWithId(id);
		List<WebElement> options = select.findElements(By.tagName("option"));
		for( WebElement optionel : options ) {
			if( option.equals(optionel.getText()) ) {
				return optionel;
			}
		}
		throw new NoSuchOptionException("select", "id", id, option);
	}

	@Override
	protected WebElement findOptionInDropDownMenuWithName (String name, int which,
			String option) throws NoSuchElementException, NoSuchOptionException,
			Exception {
		WebElement select = findDropDownMenuWithName(name, which);
		List<WebElement> options = select.findElements(By.tagName("option"));
		for( WebElement optionel : options ) {
			if( option.equals(optionel.getText()) ) {
				return optionel;
			}
		}
		throw new NoSuchOptionException("select", "name", name, option);
	}

	protected static boolean isTextInputElement (WebElement element) {
		return "input".equals(element.getTagName())
				&& "text".equals(element.getAttribute("type"));
	}

	protected static boolean isPasswordInputElement (WebElement element) {
		return "input".equals(element.getTagName())
				&& "password".equals(element.getAttribute("type"));
	}

	protected static boolean isCheckboxElement (WebElement element) {
		return "input".equals(element.getTagName())
				&& "checkbox".equals(element.getAttribute("type"));
	}

	protected static boolean isRadioButtonElement (WebElement element) {
		return "input".equals(element.getTagName())
				&& "radio".equals(element.getAttribute("type"));
	}

	protected static boolean isButtonElement (WebElement element) {
		return ("input".equals(element.getTagName()) && "submit"
				.equals(element.getAttribute("type")))
				|| "button".equals(element.getTagName());
	}

	@Override
	public List<Element> getElementsByName (String tagName) {
		return getElementsByName(tagName, null, null);
	}
	
	@Override
	public List<Element> getElementsByName (String tagName, String attName,
			String attValue) {
		List<Element> elements = new ArrayList<Element>();
		for (WebElement element : browser.findElements(By.tagName(tagName))) {
			if (attName == null || attValue.equals(element.getAttribute(attName))) {
				elements.add(new WebDriverElement(element));
			}
		}
		return elements;
	}

	@Override
	public List<Element> getElementsByName (String tagName,
			Map<String, String> attributes) {
		List<Element> elements = new ArrayList<Element>();
		for (WebElement element : browser.findElements(By.tagName(tagName))) {
			boolean addElement = true;
			if (attributes != null) {
				for (Entry<String, String> pair : attributes.entrySet()) {
					if (!pair.getValue().equals(element.getAttribute(pair.getKey()))) {
						addElement = false;
						break;
					}
				}
			}
			if (addElement) {
				elements.add(new WebDriverElement(element));
			}
		}
		return elements;
	}
}
