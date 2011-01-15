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
import java.net.URL;
import java.util.List;
import java.util.Map;

import org.jwatter.html.Element;
import org.jwatter.html.NoSuchOptionException;
import org.jwatter.util.NotImplementedException;

/**
 * Base class for implementations of the web automation framework interface. An
 * implementation of the web automation framework must implement the protected
 * methods to find certain HTML elements. This class contains implementations of
 * some of the methods defined in the interface. All other methods throw a
 * {@link NotImplementedException}.
 * 
 * @author kschneider
 * 
 */
public abstract class BaseWebAutomationFramework implements
		WebAutomationFramework {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#createBrowser()
	 */
	public void createBrowser () throws Exception {
		throw new NotImplementedException();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#createBrowser(java.lang.String)
	 */
	public void createBrowser (String profileName) throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#closeBrowser()
	 */
	public void closeBrowser () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#closeWindow()
	 */
	public void closeWindow () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#isBrowserOpen()
	 */
	public boolean isBrowserOpen () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#getBrowserVersion
	 * ()
	 */
	public String getBrowserVersion () {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#loadUrl(java.
	 * lang.String)
	 */
	public void loadUrl (String url) throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#back()
	 */
	public void back () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#forward()
	 */
	public void forward () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#disableJavascriptAlert()
	 */
	public void disableJavascriptAlert () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#enableJavascriptAlert()
	 */
	public void enableJavascriptAlert () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#disableJavascriptConfirm(boolean)
	 */
	public void disableJavascriptConfirm (boolean confirm) throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#enableJavascriptConfirm()
	 */
	public void enableJavascriptConfirm () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#setTargetToFrame(int)
	 */
	public void setTargetToFrame (int frameIndex) throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#setTargetToFrameWithId(java.lang.String)
	 */
	public void setTargetToFrameWithId (String frameAddress) throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#setTargetToFrameWithName(java.lang.String)
	 */
	public void setTargetToFrameWithName (String frameAddress) throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#setTargetToWindow(java.lang.String)
	 */
	public void setTargetToWindow (String windowName) throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#getWindowName()
	 */
	public String getWindowName () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#getWindowNames()
	 */
	public List<String> getWindowNames () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#getMostRecentWindowName()
	 */
	public String getMostRecentWindowName () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#getUrl()
	 */
	public String getUrl () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#getUrlPath()
	 */
    public String getUrlPath () throws Exception {
        return new URL(this.getUrl()).getPath();
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#getTitle()
	 */
	public String getTitle () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#getContents()
	 */
	public String getContents () throws Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebFunctionalTestFramework#containsText
	 * (java.lang.String)
	 */
	public boolean containsText (String text) throws Exception {
		String contents = getContents();
		if( null != contents ) {
			return getContents().contains(text);
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#hasButtonWithId
	 * (java.lang.String)
	 */
	public boolean hasButtonWithId (String id) throws Exception {
		try {
			findButtonWithId(id);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#hasButtonWithName
	 * (java.lang.String)
	 */
	public boolean hasButtonWithName (String name) throws Exception {
		try {
			findButtonWithName(name, 1);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#hasButtonWithText
	 * (java.lang.String)
	 */
	public boolean hasButtonWithText (String text) throws Exception {
		try {
			findButtonWithText(text, 1);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#hasButtonWithTitle
	 * (java.lang.String)
	 */
	public boolean hasButtonWithTitle (String title) throws Exception {
		try {
			findButtonWithTitle(title, 1);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#hasCheckboxWithId(java.lang.String)
	 */
	public boolean hasCheckboxWithId (String id) throws Exception {
		try {
			findCheckboxWithId(id);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#hasCheckboxWithName(java.lang.String)
	 */
	public boolean hasCheckboxWithName (String name) throws Exception {
		try {
			findCheckboxWithName(name, 1);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#hasDropDownMenuWithId(java.lang.String)
	 */
	public boolean hasDropDownMenuWithId (String id) throws Exception {
		try {
			findDropDownMenuWithId(id);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#hasDropDownMenuWithName(java.lang.String)
	 */
	public boolean hasDropDownMenuWithName (String name) throws Exception {
		try {
			findDropDownMenuWithName(name, 1);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#hasOptionInDropDownMenuWithId(java.lang.String, java.lang.String)
	 */
	public boolean hasOptionInDropDownMenuWithId (String id, String option) throws
			NoSuchElementException, Exception {
		try {
			findOptionInDropDownMenuWithId(id, option);
		} catch( NoSuchOptionException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#hasOptionInDropDownMenuWithName(java.lang.String, java.lang.String)
	 */
	public boolean hasOptionInDropDownMenuWithName (String name, String option) throws
			NoSuchElementException, AmbiguousElementException, Exception {
		return hasOptionInDropDownMenuWithName(name, 0, option);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#hasOptionInDropDownMenuWithName(java.lang.String, int, java.lang.String)
	 */
	public boolean hasOptionInDropDownMenuWithName (String name, int which, String option)
			throws NoSuchElementException, Exception {
		try {
			findOptionInDropDownMenuWithName(name, which, option);
		} catch( NoSuchOptionException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#optionsInDropDownMenuWithId(java.lang.String)
	 */
	public String[] optionsInDropDownMenuWithId (String id) throws
			NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#optionsInDropDownMenuWithName(java.lang.String)
	 */
	public String[] optionsInDropDownMenuWithName (String name) throws
			NoSuchElementException, AmbiguousElementException, Exception {
		return optionsInDropDownMenuWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#optionsInDropDownMenuWithName(java.lang.String, int)
	 */
	public String[] optionsInDropDownMenuWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#hasImage(java
	 * .lang.String)
	 */
	public boolean hasImageWithUrl (String imageurl) throws URISyntaxException,
			Exception {
		try {
			findImageWithUrl(imageurl, 1);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#hasLinkWithText
	 * (java.lang.String)
	 */
	public boolean hasLinkWithText (String text) throws Exception {
		try {
			findLinkWithText(text, 1);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * hasPasswordInputWithId(java.lang.String)
	 */
	public boolean hasPasswordInputWithId (String id) throws Exception {
		try {
			findPasswordInputWithId(id);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * hasPasswordInputWithName(java.lang.String)
	 */
	public boolean hasPasswordInputWithName (String name) throws Exception {
		try {
			findPasswordInputWithName(name, 1);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#hasRadioButtonWithId(java.lang.String)
	 */
	public boolean hasRadioButtonWithId (String id) throws Exception {
		try {
			findRadioButtonWithId(id);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#hasRadioButtonWithName(java.lang.String)
	 */
	public boolean hasRadioButtonWithName (String name)	throws Exception {
		try {
			findRadioButtonWithName(name, 1);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#hasTextareaWithId(java.lang.String)
	 */
	public boolean hasTextareaWithId (String id) throws Exception {
		try {
			findTextareaWithId(id);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#hasTextareaWithName(java.lang.String)
	 */
	public boolean hasTextareaWithName (String name) throws Exception {
		try {
			findTextareaWithName(name, 1);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#hasTextInputWithId
	 * (java.lang.String)
	 */
	public boolean hasTextInputWithId (String id) throws Exception {
		try {
			findTextInputWithId(id);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#hasTextInputWithName
	 * (java.lang.String)
	 */
	public boolean hasTextInputWithName (String name) throws Exception {
		try {
			findTextInputWithName(name, 1);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#hasSpanWithClass(java.lang.String)
	 */
	public boolean hasSpanWithClass (String cssclass) throws Exception {
		try {
			findSpanWithClass(cssclass, 1);
		} catch( NoSuchElementException e ) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickButtonWithId
	 * (java.lang.String)
	 */
	public void clickButtonWithId (String id) throws NoSuchElementException,
			Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickButtonWithName
	 * (java.lang.String)
	 */
	public void clickButtonWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		clickButtonWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickButtonWithName
	 * (java.lang.String, int)
	 */
	public void clickButtonWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickButtonWithText
	 * (java.lang.String)
	 */
	public void clickButtonWithText (String text)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		clickButtonWithText(text, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickButtonWithText
	 * (java.lang.String, int)
	 */
	public void clickButtonWithText (String text, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickButtonWithTitle
	 * (java.lang.String)
	 */
	public void clickButtonWithTitle (String title)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		clickButtonWithTitle(title, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickButtonWithTitle
	 * (java.lang.String, int)
	 */
	public void clickButtonWithTitle (String title, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#clickCheckboxWithId(java.lang.String)
	 */
	public void clickCheckboxWithId (String id) throws NoSuchElementException,
			Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#clickCheckboxWithName(java.lang.String)
	 */
	public void clickCheckboxWithName (String name) throws NoSuchElementException,
			AmbiguousElementException, Exception {
		clickCheckboxWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#clickCheckboxWithName(java.lang.String, int)
	 */
	public void clickCheckboxWithName (String name, int which) throws
			NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#selectOptionFromDropDownMenuWithId(java.lang.String, java.lang.String)
	 */
	public void selectOptionFromDropDownMenuWithId (String id, String option) throws
			NoSuchElementException, NoSuchOptionException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#selectOptionFromDropDownMenuWithName(java.lang.String, java.lang.String)
	 */
	public void selectOptionFromDropDownMenuWithName (String name, String option) throws
			NoSuchElementException, AmbiguousElementException, NoSuchOptionException,
			Exception {
		selectOptionFromDropDownMenuWithName(name, 0, option);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#selectOptionFromDropDownMenuWithName(java.lang.String, int, java.lang.String)
	 */
	public void selectOptionFromDropDownMenuWithName (String name, int which,
			String option) throws NoSuchElementException, NoSuchOptionException,
			Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickImage(java
	 * .lang.String)
	 */
	public void clickImageWithUrl (String imageurl)
			throws NoSuchElementException, AmbiguousElementException,
			URISyntaxException, Exception {
		clickImageWithUrl(imageurl, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickImage(java
	 * .lang.String, int)
	 */
	public void clickImageWithUrl (String imageurl, int which)
			throws NoSuchElementException, URISyntaxException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickLinkWithText
	 * (java.lang.String)
	 */
	public void clickLinkWithText (String text) throws NoSuchElementException,
			AmbiguousElementException, Exception {
		clickLinkWithText(text, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickLinkWithText
	 * (java.lang.String, int)
	 */
	public void clickLinkWithText (String text, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#clickTextInputWithId
	 * (java.lang.String)
	 */
	public void clickTextInputWithId (String id) throws NoSuchElementException,
			Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * clickTextInputWithName(java.lang.String)
	 */
	public void clickTextInputWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		clickTextInputWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * clickTextInputWithName(java.lang.String, int)
	 */
	public void clickTextInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * clickPasswordInputWithId(java.lang.String)
	 */
	public void clickPasswordInputWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * clickPasswordInputWithName(java.lang.String)
	 */
	public void clickPasswordInputWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		clickPasswordInputWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * clickPasswordInputWithName(java.lang.String, int)
	 */
	public void clickPasswordInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#clickRadioButtonWithId(java.lang.String)
	 */
	public void clickRadioButtonWithId (String id) throws NoSuchElementException,
			Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#clickRadioButtonWithName(java.lang.String)
	 */
	public void clickRadioButtonWithName (String name) throws NoSuchElementException,
			AmbiguousElementException, Exception {
		clickRadioButtonWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#clickRadioButtonWithName(java.lang.String, int)
	 */
	public void clickRadioButtonWithName (String name, int which) throws
			NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * focusOnTextInputWithId(java.lang.String)
	 */
	public void focusOnTextInputWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * focusOnTextInputWithName(java.lang.String)
	 */
	public void focusOnTextInputWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		focusOnTextInputWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * focusOnTextInputWithName(java.lang.String, int)
	 */
	public void focusOnTextInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * hasFocusOnButtonWithId(java.lang.String)
	 */
	public boolean hasFocusOnButtonWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * hasFocusOnButtonWithName(java.lang.String)
	 */
	public boolean hasFocusOnButtonWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return hasFocusOnButtonWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * hasFocusOnButtonWithName(java.lang.String, int)
	 */
	public boolean hasFocusOnButtonWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * hasFocusOnButtonWithText(java.lang.String)
	 */
	public boolean hasFocusOnButtonWithText (String text)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return hasFocusOnButtonWithText(text, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * hasFocusOnButtonWithText(java.lang.String, int)
	 */
	public boolean hasFocusOnButtonWithText (String text, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * hasFocusOnLinkWithText(java.lang.String)
	 */
	public boolean hasFocusOnLinkWithText (String text)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return hasFocusOnLinkWithText(text, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * hasFocusOnLinkWithText(java.lang.String, int)
	 */
	public boolean hasFocusOnLinkWithText (String text, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * hasFocusOnTextInputWithId(java.lang.String)
	 */
	public boolean hasFocusOnTextInputWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * hasFocusOnTextInputWithName(java.lang.String)
	 */
	public boolean hasFocusOnTextInputWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return hasFocusOnTextInputWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * hasFocusOnTextInputWithName(java.lang.String, int)
	 */
	public boolean hasFocusOnTextInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#setTextInputWithId
	 * (java.lang.String, java.lang.String)
	 */
	public void setTextInputWithId (String id, String value)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#setTextInputWithName
	 * (java.lang.String, java.lang.String)
	 */
	public void setTextInputWithName (String name, String value)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		setTextInputWithName(name, 0, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.jwatter.WebAutomationFramework#setTextInputWithName
	 * (java.lang.String, int, java.lang.String)
	 */
	public void setTextInputWithName (String name, int which, String value)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * setPasswordInputWithId(java.lang.String, java.lang.String)
	 */
	public void setPasswordInputWithId (String id, String value)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * setPasswordInputWithName(java.lang.String, java.lang.String)
	 */
	public void setPasswordInputWithName (String name, String value)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		setPasswordInputWithName(name, 0, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * setPasswordInputWithName(java.lang.String, int, java.lang.String)
	 */
	public void setPasswordInputWithName (String name, int which, String value)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#setTextareaWithId(java.lang.String, java.lang.String)
	 */
	public void setTextareaWithId (String id, String value)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#setTextareaWithName(java.lang.String, java.lang.String)
	 */
	public void setTextareaWithName (String name, String value)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		setTextareaWithName(name, 0, value);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#setTextareaWithName(java.lang.String, int, java.lang.String)
	 */
	public void setTextareaWithName (String name, int which, String value)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#pressEnterInTextInputWithId(java.lang.String)
	 */
	public void pressEnterInTextInputWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#pressEnterInTextInputWithName(java.lang.String)
	 */
	public void pressEnterInTextInputWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		pressEnterInTextInputWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#pressEnterInTextInputWithName(java.lang.String, int)
	 */
	public void pressEnterInTextInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#pressEnterInPasswordInputWithId(java.lang.String)
	 */
	public void pressEnterInPasswordInputWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#pressEnterInPasswordInputWithName(java.lang.String)
	 */
	public void pressEnterInPasswordInputWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		pressEnterInPasswordInputWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#pressEnterInPasswordInputWithName(java.lang.String, int)
	 */
	public void pressEnterInPasswordInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * valueOfTextInputWithId(java.lang.String)
	 */
	public String valueOfTextInputWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * valueOfTextInputWithName(java.lang.String)
	 */
	public String valueOfTextInputWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return valueOfTextInputWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * valueOfTextInputWithName(java.lang.String, int)
	 */
	public String valueOfTextInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * valueOfPasswordInputWithId(java.lang.String)
	 */
	public String valueOfPasswordInputWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * valueOfPasswordInputWithName(java.lang.String)
	 */
	public String valueOfPasswordInputWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return valueOfPasswordInputWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * valueOfPasswordInputWithName(java.lang.String, int)
	 */
	public String valueOfPasswordInputWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#valueOfTextareaWithId(java.lang.String)
	 */
	public String valueOfTextareaWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#valueOfTextareaWithName(java.lang.String)
	 */
	public String valueOfTextareaWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return valueOfTextareaWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#valueOfTextareaWithName(java.lang.String, int)
	 */
	public String valueOfTextareaWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * isCheckedCheckboxWithId(java.lang.String)
	 */
	public boolean isCheckedCheckboxWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * isCheckedCheckboxWithName(java.lang.String)
	 */
	public boolean isCheckedCheckboxWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return isCheckedCheckboxWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * isCheckedCheckboxWithName(java.lang.String, int)
	 */
	public boolean isCheckedCheckboxWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * isCheckedRadioButtonWithId(java.lang.String)
	 */
	public boolean isCheckedRadioButtonWithId (String id)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * isCheckedRadioButtonWithName(java.lang.String)
	 */
	public boolean isCheckedRadioButtonWithName (String name)
			throws NoSuchElementException, AmbiguousElementException, Exception {
		return isCheckedRadioButtonWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * isCheckedRadioButtonWithName(java.lang.String, int)
	 */
	public boolean isCheckedRadioButtonWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#selectedOptionInDropDownMenuWithId(java.lang.String)
	 */
	public String selectedOptionInDropDownMenuWithId (String id) throws
			NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#selectedOptionInDropDownMenuWithName(java.lang.String)
	 */
	public String selectedOptionInDropDownMenuWithName (String name) throws
			NoSuchElementException, AmbiguousElementException, Exception {
		return selectedOptionInDropDownMenuWithName(name, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#selectedOptionInDropDownMenuWithName(java.lang.String, int)
	 */
	public String selectedOptionInDropDownMenuWithName (String name, int which)
			throws NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#contentsOfSpanWithClass(java.lang.String)
	 */
	public String contentsOfSpanWithClass (String cssclass) throws
			NoSuchElementException, AmbiguousElementException, Exception {
		return contentsOfSpanWithClass(cssclass, 0);
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#contentsOfSpanWithClass(java.lang.String, int)
	 */
	public String contentsOfSpanWithClass (String cssclass, int which) throws
			NoSuchElementException, Exception {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * @see org.jwatter.WebAutomationFramework#contentsOfSpansWithClass(java.lang.String)
	 */
	public String[] contentsOfSpansWithClass (String cssclass) throws Exception {
		throw new NotImplementedException();
	}

	protected abstract Object findButtonWithId (String id)
			throws NoSuchElementException, Exception;

	protected abstract Object findButtonWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	protected abstract Object findButtonWithText (String text, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	protected abstract Object findButtonWithTitle (String title, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	protected abstract Object findCheckboxWithId (String id)
			throws NoSuchElementException, Exception;

	protected abstract Object findCheckboxWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	protected abstract Object findDropDownMenuWithId (String id)
			throws NoSuchElementException, Exception;

	protected abstract Object findDropDownMenuWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	protected abstract Object findOptionInDropDownMenuWithId (String id, String option)
			throws NoSuchElementException, NoSuchOptionException, Exception;

	protected abstract Object findOptionInDropDownMenuWithName (String name, int which,
			String option) throws NoSuchElementException, NoSuchOptionException,
			Exception;

	protected abstract Object findImageWithUrl (String imageurl, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	protected abstract Object findTextInputWithId (String id)
			throws NoSuchElementException, Exception;

	protected abstract Object findTextInputWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	protected abstract Object findPasswordInputWithId (String id)
			throws NoSuchElementException, Exception;

	protected abstract Object findPasswordInputWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	protected abstract Object findTextareaWithId (String id)
			throws NoSuchElementException, Exception;

	protected abstract Object findTextareaWithName (String name, int which)
			throws NoSuchElementException, Exception;

	protected abstract Object findRadioButtonWithId (String id)
			throws NoSuchElementException, Exception;

	protected abstract Object findRadioButtonWithName (String name, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	protected abstract Object findLinkWithText (String text, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception;
	
	protected abstract Object findSpanWithClass (String cssclass, int which)
			throws NoSuchElementException, AmbiguousElementException, Exception;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * getElementsByName(java.lang.String)
	 */
	public List<Element> getElementsByName (String tagName) {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * getElementsByName(java.lang.String, java.lang.String, java.lang.String)
	 */
	public List<Element> getElementsByName (String tagname, String attName,
			String attValue) {
		throw new NotImplementedException();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jwatter.WebAutomationFramework#
	 * getElementsByName(java.lang.String, java.util.Map)
	 */
	public List<Element> getElementsByName (String tagname,
			Map<String, String> attributes) {
		throw new NotImplementedException();
	}
}
