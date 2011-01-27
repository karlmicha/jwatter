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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.jwatter.util.Require.requireEquals;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.jwatter.util.FunctionalTestProperties;

public class WebAutomationFrameworkTest {

	private static final Class<WebAutomationFrameworkTest> myclass =
			WebAutomationFrameworkTest.class;
	protected static Logger logger = Logger
			.getLogger(myclass.getPackage().getName());

	protected static final String propertiesFilename;
	protected static final FunctionalTestProperties properties;
	protected static final String browserFrameworkClassName;
	protected static final String loginPageUrl;
	protected static final String indexPageUrl;
	protected static final String forgotPasswordPageUrl;
	protected static final String homePageUrl;
	protected static final String loginPageTitle;
	protected static final String emailFieldId;
	protected static final String emailFieldName;
	protected static final String emailFieldValue;
	protected static final String passwordFieldId;
	protected static final String passwordFieldName;
	protected static final String passwordFieldValue;
	protected static final String signInButtonId;
	protected static final String signInButtonName;
	protected static final String signInButtonText;
	protected static final String cantSignInLinkText;
	protected static final String cantSignInLinkSpanClass;
	protected static final String logoImageUrl;
	protected static final String loginPageContentsSubstring;
	protected static final String loginPageNotContentsSubstring;
	protected static final String searchLinkText;
	protected static final String returnToLoginPageLinkText;

	static {
	    propertiesFilename = System.getProperty("tests.properties");
	    if (propertiesFilename == null) {
	        logger.severe("tests.properties is null, use -Dtests.properties=tests/resources/tests.properties");
	        throw new RuntimeException("tests.properties is null, use -Dtests.properties=tests/resources/tests.properties");
	    }
		try {
			properties = new FunctionalTestProperties(propertiesFilename);
		} catch( IOException e ) {
			logger.severe(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		browserFrameworkClassName = properties.getRequiredProperty(
				"browserFrameworkClassName", myclass);
		loginPageUrl = properties.getRequiredProperty("loginPageUrl", myclass);
		indexPageUrl = properties.getRequiredProperty("indexPageUrl", myclass);
		forgotPasswordPageUrl = properties.getRequiredProperty(
				"forgotPasswordPageUrl", myclass);
		homePageUrl = properties.getRequiredProperty("homePageUrl", myclass);
		loginPageTitle = properties.getRequiredProperty("loginPageTitle", myclass);
		emailFieldId = properties.getRequiredProperty("emailFieldId", myclass);
		emailFieldName = properties.getRequiredProperty("emailFieldName", myclass);
		emailFieldValue = properties.getRequiredProperty("emailFieldValue", myclass);
		passwordFieldId = properties.getRequiredProperty("passwordFieldId", myclass);
		passwordFieldName = properties.getRequiredProperty("passwordFieldName", myclass);
		passwordFieldValue = properties.getRequiredProperty("passwordFieldValue", myclass);
		signInButtonId = properties.getRequiredProperty("signInButtonId", myclass);
		signInButtonName = properties.getRequiredProperty("signInButtonName", myclass);
		signInButtonText = properties.getRequiredProperty("signInButtonText", myclass);
		cantSignInLinkText = properties.getRequiredProperty("cantSignInLinkText", myclass);
		cantSignInLinkSpanClass = properties.getRequiredProperty(
		        "cantSignInLinkSpanClass", myclass);
		logoImageUrl = properties.getRequiredProperty("logoImageUrl", myclass);
		loginPageContentsSubstring = properties.getRequiredProperty(
				"loginPageContentsSubstring", myclass);
		loginPageNotContentsSubstring = properties.getRequiredProperty(
				"loginPageNotContentsSubstring", myclass);
		searchLinkText = properties.getRequiredProperty("searchLinkText", myclass);
		returnToLoginPageLinkText = properties.getRequiredProperty(
				"returnToLoginPageLinkText", myclass);
	}

	protected static WebAutomationFramework browser;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUp () throws Exception {
		logger.info("Using browser framework " + browserFrameworkClassName);
		Class<? extends Object> browserFrameworkClass;
		try {
			browserFrameworkClass = Class.forName(browserFrameworkClassName);
		} catch( Exception e ) {
			logger.severe("Could not determine class for "
					+ browserFrameworkClassName);
			e.printStackTrace();
			throw e;
		}
		if( !WebAutomationFramework.class
				.isAssignableFrom(browserFrameworkClass) ) {
			logger.severe(browserFrameworkClassName
					+ " is not compatible with "
					+ WebAutomationFramework.class.getName());
			throw new Exception(browserFrameworkClassName
					+ " is not compatible with "
					+ WebAutomationFramework.class.getName());
		}
		Constructor<? extends WebAutomationFramework> browserFrameworkConstructor;
		try {
			browserFrameworkConstructor = ((Class<? extends WebAutomationFramework>) browserFrameworkClass)
					.getConstructor();
		} catch( Exception e ) {
			logger.severe("Could not locate constructor for "
					+ browserFrameworkClassName);
			e.printStackTrace();
			throw e;
		}
		try {
			browser = (WebAutomationFramework) browserFrameworkConstructor
					.newInstance();
		} catch( Exception e ) {
			logger.severe("Could not instantiate " + browserFrameworkClassName);
			e.printStackTrace();
			throw e;
		}

		logger.info("Starting browser");
		browser.createBrowser();
		if( browser instanceof HtmlUnitWebAutomationFramework ) {
			((HtmlUnitWebAutomationFramework) browser)
					.setJavaScriptEnabled(false);
		}
		logger.info("Using test page " + loginPageUrl);
		browser.loadUrl(loginPageUrl);
	}

	@Before
	public void setUpPage () throws Exception {
		if( !loginPageUrl.equals(browser.getUrl()) ) {
			browser.loadUrl(loginPageUrl);
		}
		requireEquals(loginPageUrl, browser.getUrl());
	}

	@AfterClass
	public static void tearDown () throws Exception {
	    logger.info("Shutting down browser");
		browser.closeBrowser();
	}

	@Test
	public void testGetTitle () throws Exception {
		assertEquals(loginPageTitle, browser.getTitle());
	}

	@Test
	public void testGetContents () throws Exception {
		assertTrue(browser.getContents().contains(loginPageContentsSubstring));
		assertFalse(browser.getContents().contains(
				loginPageNotContentsSubstring));
	}

	@Test
	public void testGetUrl () throws Exception {
		assertEquals(loginPageUrl, browser.getUrl());
	}

	@Test
	public void testContainsText () throws Exception {
		assertTrue(browser.containsText(loginPageContentsSubstring));
		assertFalse(browser.containsText(loginPageNotContentsSubstring));
	}

	@Test
	public void testHasButtonWithId () throws Exception {
		assertTrue(browser.hasButtonWithId(signInButtonId));
		assertFalse(browser.hasButtonWithId("X" + signInButtonId));
	}

	@Test
	public void testHasButtonWithText () throws Exception {
		assertTrue(browser.hasButtonWithText(signInButtonText));
		assertFalse(browser.hasButtonWithText("X" + signInButtonText));
	}

	@Test
	public void testHasButtonWithName () throws Exception {
		assertTrue(browser.hasButtonWithName(signInButtonName));
		assertFalse(browser.hasButtonWithName("X" + signInButtonName));
	}

	@Test
	public void testHasImageWithUrl () throws Exception {
		assertTrue(browser.hasImageWithUrl(logoImageUrl));
		assertFalse(browser.hasImageWithUrl("X" + logoImageUrl));
	}

	@Test
	public void testHasLinkWithText () throws Exception {
		assertTrue(browser.hasLinkWithText(cantSignInLinkText));
		assertFalse(browser.hasLinkWithText("X" + cantSignInLinkText));
	}
	
	@Test
	public void testHasSpanWithClass () throws Exception {
		assertTrue(browser.hasSpanWithClass(cantSignInLinkSpanClass));
		assertFalse(browser.hasSpanWithClass("X" + cantSignInLinkSpanClass));
	}

	@Test
	public void testHasPasswordInputWithId () throws Exception {
		assertTrue(browser.hasPasswordInputWithId(passwordFieldId));
		assertFalse(browser.hasPasswordInputWithId(passwordFieldName));
		assertFalse(browser.hasPasswordInputWithId(emailFieldId));
		assertFalse(browser.hasPasswordInputWithId("X" + passwordFieldId));
	}

	@Test
	public void testHasPasswordInputWithName () throws Exception {
		assertTrue(browser.hasPasswordInputWithName(passwordFieldName));
		assertFalse(browser.hasPasswordInputWithName(passwordFieldId));
		assertFalse(browser.hasPasswordInputWithName(emailFieldName));
		assertFalse(browser.hasPasswordInputWithName("X" + passwordFieldName));
	}

	@Test
	public void testHasTextInputWithId () throws Exception {
		assertTrue(browser.hasTextInputWithId(emailFieldId));
		assertFalse(browser.hasTextInputWithId(emailFieldName));
		assertFalse(browser.hasTextInputWithId(passwordFieldId));
		assertFalse(browser.hasTextInputWithId("X" + emailFieldId));
	}

	@Test
	public void testHasTextInputWithName () throws Exception {
		assertTrue(browser.hasTextInputWithName(emailFieldName));
		assertFalse(browser.hasTextInputWithName(emailFieldId));
		assertFalse(browser.hasTextInputWithName(passwordFieldName));
		assertFalse(browser.hasTextInputWithName("X" + emailFieldName));
	}

	@Test
	public void testSetTextInputWithId () throws Exception {
		browser.setTextInputWithId(emailFieldId, emailFieldValue);
		assertEquals(emailFieldValue, browser
				.valueOfTextInputWithId(emailFieldId));
		browser.setTextInputWithId(emailFieldId, "");
		assertEquals("", browser.valueOfTextInputWithId(emailFieldId));
	}

	@Test(expected = NoSuchElementException.class)
	public void testSetTextInputWithIdNoSuchElement () throws Exception {
		browser.setTextInputWithId("X" + emailFieldId, "");
	}

	@Test
	public void testSetTextInputWithNameStringString () throws Exception {
		browser.setTextInputWithName(emailFieldName, emailFieldValue);
		assertEquals(emailFieldValue, browser
				.valueOfTextInputWithName(emailFieldName));
		browser.setTextInputWithName(emailFieldName, "");
		assertEquals("", browser.valueOfTextInputWithName(emailFieldName));
	}

	@Test(expected = NoSuchElementException.class)
	public void testSetTextInputWithNameStringStringNoSuchElement ()
			throws Exception {
		browser.setTextInputWithName("X" + emailFieldName, "");
	}

	@Test
	public void testSetTextInputWithNameStringIntString () throws Exception {
		browser.setTextInputWithName(emailFieldName, 1, emailFieldValue);
		assertEquals(emailFieldValue, browser.valueOfTextInputWithName(
				emailFieldName, 1));
		browser.setTextInputWithName(emailFieldName, 1, "");
		assertEquals("", browser.valueOfTextInputWithName(emailFieldName, 1));
	}

	@Test(expected = NoSuchElementException.class)
	public void testSetTextInputWithNameStringIntStringNoSuchElement ()
			throws Exception {
		browser.setTextInputWithName(emailFieldName, 2, "");
	}

	@Test
	public void testSetPasswordInputWithId () throws Exception {
		browser.setPasswordInputWithId(passwordFieldId, passwordFieldValue);
		assertEquals(passwordFieldValue, browser
				.valueOfPasswordInputWithId(passwordFieldId));
		browser.setPasswordInputWithId(passwordFieldId, "");
		assertEquals("", browser.valueOfPasswordInputWithId(passwordFieldId));
	}

	@Test(expected = NoSuchElementException.class)
	public void testSetPasswordInputWithIdNoSuchElement () throws Exception {
		browser.setPasswordInputWithId("X" + passwordFieldId, "");
	}

	@Test
	public void testSetPasswordInputWithNameStringString () throws Exception {
		browser.setPasswordInputWithName(passwordFieldName, passwordFieldValue);
		assertEquals(passwordFieldValue, browser
				.valueOfPasswordInputWithName(passwordFieldName));
		browser.setPasswordInputWithName(passwordFieldName, "");
		assertEquals("", browser
				.valueOfPasswordInputWithName(passwordFieldName));
	}

	@Test(expected = NoSuchElementException.class)
	public void testSetPasswordInputWithNameStringStringNoSuchElement ()
			throws Exception {
		browser.setPasswordInputWithName("X" + passwordFieldName, "");
	}

	@Test
	public void testSetPasswordInputWithNameStringIntString () throws Exception {
		browser.setPasswordInputWithName(passwordFieldName, 1,
				passwordFieldValue);
		assertEquals(passwordFieldValue, browser.valueOfPasswordInputWithName(
				passwordFieldName, 1));
		browser.setPasswordInputWithName(passwordFieldName, 1, "");
		assertEquals("", browser.valueOfPasswordInputWithName(
				passwordFieldName, 1));
	}

	@Test(expected = NoSuchElementException.class)
	public void testSetPasswordInputWithNameStringIntStringNoSuchElement ()
			throws Exception {
		browser.setPasswordInputWithName(passwordFieldName, 2, "");
	}

	@Test
	public void testClickButtonWithId () throws Exception {
		browser.setTextInputWithId(emailFieldId, emailFieldValue);
		browser.setPasswordInputWithId(passwordFieldId, passwordFieldValue);
		browser.clickButtonWithId(signInButtonId);
		assertEquals(indexPageUrl, browser.getUrl());
		browser.back();
	}

	@Test(expected = NoSuchElementException.class)
	public void testClickButtonWithIdNoSuchElement () throws Exception {
		browser.clickButtonWithId("X" + signInButtonId);
	}

	@Test
	public void testClickButtonWithNameString () throws Exception {
		browser.setTextInputWithId(emailFieldId, emailFieldValue);
		browser.setPasswordInputWithId(passwordFieldId, passwordFieldValue);
		browser.clickButtonWithName(signInButtonName);
		assertEquals(indexPageUrl, browser.getUrl());
		browser.back();		
	}

	@Test(expected = NoSuchElementException.class)
	public void testClickButtonWithNameStringNoSuchElement () throws Exception {
		browser.clickButtonWithName("X" + signInButtonName);
	}

	@Test
	public void testClickButtonWithNameStringInt () throws Exception {
		browser.setTextInputWithId(emailFieldId, emailFieldValue);
		browser.setPasswordInputWithId(passwordFieldId, passwordFieldValue);
		browser.clickButtonWithName(signInButtonName, 1);
		assertEquals(indexPageUrl, browser.getUrl());
		browser.back();		
	}

	@Test(expected = NoSuchElementException.class)
	public void testClickButtonWithNameStringIntNoSuchElement () throws Exception {
		browser.clickButtonWithName(signInButtonName, 2);
	}

	@Test
	public void testClickButtonWithTextString () throws Exception {
		browser.setTextInputWithId(emailFieldId, emailFieldValue);
		browser.setPasswordInputWithId(passwordFieldId, passwordFieldValue);
		browser.clickButtonWithText(signInButtonText);
		assertEquals(indexPageUrl, browser.getUrl());
		browser.back();
	}

	@Test(expected = NoSuchElementException.class)
	public void testClickButtonWithTextStringNoSuchElement () throws Exception {
		browser.clickButtonWithText("X" + signInButtonText);
	}

	@Test
	public void testClickButtonWithTextStringInt () throws Exception {
		browser.setTextInputWithId(emailFieldId, emailFieldValue);
		browser.setPasswordInputWithId(passwordFieldId, passwordFieldValue);
		browser.clickButtonWithText(signInButtonText, 1);
		assertEquals(indexPageUrl, browser.getUrl());
		browser.back();
	}

	@Test(expected = NoSuchElementException.class)
	public void testClickButtonWithTextStringIntNoSuchElement ()
			throws Exception {
		browser.clickButtonWithText(signInButtonText, 2);
	}

	@Test
	public void testPressEnterInTextInputWithId () throws Exception {
		browser.setTextInputWithId(emailFieldId, emailFieldValue);
		browser.setPasswordInputWithId(passwordFieldId, passwordFieldValue);
		browser.pressEnterInTextInputWithId(emailFieldId);
		assertEquals(indexPageUrl, browser.getUrl());
		browser.back();
	}

	@Test(expected = NoSuchElementException.class)
	public void testPressEnterInTextInputWithIdNoSuchElement () throws Exception {
		browser.pressEnterInTextInputWithId("X" + emailFieldId);
	}

	@Test
	public void testPressEnterInTextInputWithNameString () throws Exception {
		browser.setTextInputWithName(emailFieldName, emailFieldValue);
		browser.setPasswordInputWithName(passwordFieldName, passwordFieldValue);
		browser.pressEnterInTextInputWithName(emailFieldName);
		assertEquals(indexPageUrl, browser.getUrl());
		browser.back();
	}

	@Test(expected = NoSuchElementException.class)
	public void testPressEnterInTextInputWithNameStringNoSuchElement ()
			throws Exception {
		browser.pressEnterInTextInputWithName("X" + emailFieldName);
	}

	@Test
	public void testPressEnterInTextInputWithNameStringInt () throws Exception {
		browser.setTextInputWithName(emailFieldName, 1, emailFieldValue);
		browser.setPasswordInputWithName(passwordFieldName, 1, passwordFieldValue);
		browser.pressEnterInTextInputWithName(emailFieldName, 1);
		assertEquals(indexPageUrl, browser.getUrl());
		browser.back();
	}

	@Test(expected = NoSuchElementException.class)
	public void testPressEnterInTextInputWithNameStringIntNoSuchElement ()
			throws Exception {
		browser.pressEnterInTextInputWithName(emailFieldName, 2);
	}

	@Test
	public void testPressEnterInPasswordInputWithId () throws Exception {
		browser.setTextInputWithId(emailFieldId, emailFieldValue);
		browser.setPasswordInputWithId(passwordFieldId, passwordFieldValue);
		browser.pressEnterInPasswordInputWithId(passwordFieldId);
		assertEquals(indexPageUrl, browser.getUrl());
		browser.back();
	}

	@Test(expected = NoSuchElementException.class)
	public void testPressEnterInPasswordInputWithIdNoSuchElement () throws Exception {
		browser.pressEnterInPasswordInputWithId("X" + passwordFieldId);
	}

	@Test
	public void testPressEnterInPasswordInputWithNameString () throws Exception {
		browser.setTextInputWithName(emailFieldName, emailFieldValue);
		browser.setPasswordInputWithName(passwordFieldName, passwordFieldValue);
		browser.pressEnterInPasswordInputWithName(passwordFieldName);
		assertEquals(indexPageUrl, browser.getUrl());
		browser.back();
	}

	@Test(expected = NoSuchElementException.class)
	public void testPressEnterInPasswordInputWithNameStringNoSuchElement ()
			throws Exception {
		browser.pressEnterInPasswordInputWithName("X" + passwordFieldName);
	}

	@Test
	public void testPressEnterInPasswordInputWithNameStringInt () throws Exception {
		browser.setTextInputWithName(emailFieldName, 1, emailFieldValue);
		browser.setPasswordInputWithName(passwordFieldName, 1, passwordFieldValue);
		browser.pressEnterInPasswordInputWithName(passwordFieldName, 1);
		assertEquals(indexPageUrl, browser.getUrl());
		browser.back();
	}

	@Test(expected = NoSuchElementException.class)
	public void testPressEnterInPasswordInputWithNameStringIntNoSuchElement ()
			throws Exception {
		browser.pressEnterInPasswordInputWithName(passwordFieldName, 2);
	}

	@Test
	public void testClickLinkWithTextString () throws Exception {
		browser.clickLinkWithText(cantSignInLinkText);
		assertEquals(forgotPasswordPageUrl, browser.getUrl());
		browser.back();
	}

	@Test(expected = NoSuchElementException.class)
	public void testClickLinkWithTextStringNoSuchElement () throws Exception {
		browser.clickLinkWithText("X" + cantSignInLinkText);
	}

	@Test
	public void testClickLinkWithTextStringInt () throws Exception {
		browser.clickLinkWithText(cantSignInLinkText, 1);
		assertEquals(forgotPasswordPageUrl, browser.getUrl());
		browser.back();
	}

	@Test(expected = NoSuchElementException.class)
	public void testClickLinkWithTextStringIntNoSuchElement () throws Exception {
		browser.clickLinkWithText(cantSignInLinkText, 2);
	}

	@Test
	public void testClickImageWithUrlString () throws Exception {
		browser.clickImageWithUrl(logoImageUrl);
		assertEquals(homePageUrl, browser.getUrl());
		browser.back();
	}

	@Test(expected = NoSuchElementException.class)
	public void testClickImageWithUrlStringNoSuchElement () throws Exception {
		browser.clickImageWithUrl("X" + logoImageUrl);
	}

	@Test
	public void testClickImageWithUrlStringInt () throws Exception {
		browser.clickImageWithUrl(logoImageUrl, 1);
		assertEquals(homePageUrl, browser.getUrl());
		browser.back();
	}

	@Test(expected = NoSuchElementException.class)
	public void testClickImageWithUrlStringIntNoSuchElement () throws Exception {
		browser.clickLinkWithText(logoImageUrl, 2);
	}

	@Test
	public void testHasLinkWithTextInEmbeddedDiv () throws Exception {
		goToIndexPage();
		assertTrue(browser.hasLinkWithText(searchLinkText));
		browser.back();
	}

	@Test
	public void testHasLinkWithTextBlankPadded () throws Exception {
		browser.clickLinkWithText(cantSignInLinkText);
		assertTrue(browser.hasLinkWithText(returnToLoginPageLinkText));
		browser.back();
	}

	@Test
	public void testContentsOfSpanWithClass () throws Exception {
		assertEquals(cantSignInLinkText,
				browser.contentsOfSpanWithClass(cantSignInLinkSpanClass));
	}
	
	@Test
	public void testContentsOfSpanWithClassWhich () throws Exception {
		assertEquals(cantSignInLinkText,
				browser.contentsOfSpanWithClass(cantSignInLinkSpanClass, 1));
	}
	
	@Test(expected = NoSuchElementException.class)
	public void testContentsOfSpanWithClassNoSuchElement () throws Exception {
		browser.contentsOfSpanWithClass(cantSignInLinkSpanClass, 2);
	}
	
	@Test
	public void testContentsOfSpansWithClass () throws Exception {
		String[] spans = browser.contentsOfSpansWithClass(cantSignInLinkSpanClass);
		assertEquals(1, spans.length);
		assertEquals(cantSignInLinkText, spans[0]);
	}

	protected void goToIndexPage () throws Exception {
		requireEquals(loginPageUrl, browser.getUrl());
		browser.setTextInputWithId(emailFieldId, emailFieldValue);
		browser.setPasswordInputWithId(passwordFieldId, passwordFieldValue);
		browser.clickButtonWithId(signInButtonId);
		requireEquals(indexPageUrl, browser.getUrl());
	}

}
