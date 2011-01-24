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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import org.jwatter.util.FunctionalTestProperties;
import org.jwatter.browser.HtmlUnitWebAutomationFramework;
import org.jwatter.browser.NoSuchElementException;
import org.jwatter.browser.WebAutomationFramework;
import org.jwatter.html.NoSuchOptionException;

public class WebAutomationFrameworkFormTest
{
	private static final Class<WebAutomationFrameworkFormTest> myclass =
			WebAutomationFrameworkFormTest.class;
	protected static Logger logger =
			Logger.getLogger(myclass.getPackage().getName());
	protected static final String propertiesFilename =
	        System.getProperties().getProperty("tests.properties");
	protected static FunctionalTestProperties properties;

	static
	{
	    if (propertiesFilename == null) {
            logger.severe("tests.properties is null, use -Dtests.properties=tests/resources/tests.properties");
	        throw new RuntimeException(
	                "tests.properties is null, use -Dtests.properties=tests/resources/tests.properties");
	    }
		try
		{
			properties = new FunctionalTestProperties(propertiesFilename);
		}
		catch ( IOException e )
		{
			logger.severe(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	protected static final String browserFrameworkClassName =
			properties.getRequiredProperty("browserFrameworkClassName",
					myclass);

	protected static final String testPageUrl =
			properties.getRequiredProperty("testPageUrl", myclass);
	protected static final String textInputId =
			properties.getRequiredProperty("textInputId", myclass);
	protected static final String passwordInputId =
			properties.getRequiredProperty("passwordInputId", myclass);
	protected static final String textareaId =
			properties.getRequiredProperty("textareaId", myclass);
	protected static final String textareaValue =
			properties.getRequiredProperty("textareaValue", myclass);
	protected static final String radioButtonId =
			properties.getRequiredProperty("radioButtonId", myclass);
	protected static final String checkboxId =
			properties.getRequiredProperty("checkboxId", myclass);
	protected static final String selectId =
			properties.getRequiredProperty("selectId", myclass);
	protected static final String option1Text =
			properties.getRequiredProperty("option1Text", myclass);
	protected static final String option2Text =
			properties.getRequiredProperty("option2Text", myclass);
	protected static final String submitButtonId =
			properties.getRequiredProperty("submitButtonId", myclass);
	protected static final String submitButtonText =
			properties.getRequiredProperty("submitButtonText", myclass);
	protected static final String button1Id =
			properties.getRequiredProperty("button1Id", myclass);
	protected static final String button1Text =
			properties.getRequiredProperty("button1Text", myclass);
	protected static final String button2Id =
			properties.getRequiredProperty("button2Id", myclass);
	protected static final String button2Text =
			properties.getRequiredProperty("button2Text", myclass);

	protected static WebAutomationFramework browser;

	@SuppressWarnings("unchecked")
	@BeforeClass
	public static void setUp () throws Exception
	{
		logger.info("Using browser framework " + browserFrameworkClassName);
		Class<? extends Object> browserFrameworkClass;
		try
		{
			browserFrameworkClass = Class.forName(browserFrameworkClassName);
		}
		catch ( Exception e )
		{
			logger.severe("Could not determine class for "
					+ browserFrameworkClassName);
			e.printStackTrace();
			throw e;
		}
		if ( !WebAutomationFramework.class
				.isAssignableFrom(browserFrameworkClass) )
		{
			logger.severe(browserFrameworkClassName
					+ " is not compatible with "
					+ WebAutomationFramework.class.getName());
			throw new Exception(browserFrameworkClassName
					+ " is not compatible with "
					+ WebAutomationFramework.class.getName());
		}
		Constructor<? extends WebAutomationFramework> browserFrameworkConstructor;
		try
		{
			browserFrameworkConstructor =
					((Class<? extends WebAutomationFramework>) browserFrameworkClass)
							.getConstructor();
		}
		catch ( Exception e )
		{
			logger.severe("Could not locate constructor for "
					+ browserFrameworkClassName);
			e.printStackTrace();
			throw e;
		}
		try
		{
			browser =
					(WebAutomationFramework) browserFrameworkConstructor
							.newInstance();
		}
		catch ( Exception e )
		{
			logger.severe("Could not instantiate " + browserFrameworkClassName);
			e.printStackTrace();
			throw e;
		}

		logger.info("Starting browser");
		browser.createBrowser();
		if ( browser instanceof HtmlUnitWebAutomationFramework )
		{
			((HtmlUnitWebAutomationFramework) browser)
					.setJavaScriptEnabled(false);
		}
		logger.info("Using test page " + testPageUrl);
		browser.loadUrl(testPageUrl);
	}

	@AfterClass
	public static void tearDown () throws Exception
	{
	    logger.info("Shutting down browser");
		browser.closeBrowser();
	}

	@Test
	public void testHasTextareaWithId () throws Exception
	{
		assertTrue(browser.hasTextareaWithId(textareaId));
		assertFalse(browser.hasTextareaWithId("X"));
	}

	@Test
	public void testValueOfTextareaWithId () throws Exception
	{
		assertEquals(textareaValue, browser.valueOfTextareaWithId(textareaId));
	}

	@Test(expected = NoSuchElementException.class)
	public void testValueOfTextareaWithIdNoSuchElementException ()
			throws Exception
	{
		browser.valueOfTextareaWithId("X");
	}

	@Test
	public void testHasDropDownMenuWithId () throws Exception
	{
		assertTrue(browser.hasDropDownMenuWithId(selectId));
		assertFalse(browser.hasDropDownMenuWithId("X"));
	}

	@Test
	public void testHasOptionInDropDownMenuWithId () throws Exception
	{
		assertTrue(browser.hasOptionInDropDownMenuWithId(selectId, option1Text));
		assertTrue(browser.hasOptionInDropDownMenuWithId(selectId, option2Text));
		assertFalse(browser.hasOptionInDropDownMenuWithId(selectId, "X"));
	}

	@Test(expected = NoSuchElementException.class)
	public void testHasOptionInDropDownMenuWithIdNoSuchElementException ()
			throws Exception
	{
		browser.hasOptionInDropDownMenuWithId("X", option1Text);
	}

	@Test
	public void testOptionsInDropDownMenuWithId () throws Exception
	{
		assertArrayEquals(new String[]
		{
				option1Text, option2Text
		}, browser.optionsInDropDownMenuWithId(selectId));
	}

	@Test(expected = NoSuchElementException.class)
	public void testOptionsInDropDownMenuWithIdNoSuchElementException ()
			throws Exception
	{
		browser.optionsInDropDownMenuWithId("X");
	}

	@Test
	public void testSelectOptionFromDropDownMenuWithId () throws Exception
	{
		assertEquals(option1Text, browser
				.selectedOptionInDropDownMenuWithId(selectId));
		browser.selectOptionFromDropDownMenuWithId(selectId, option2Text);
		assertEquals(option2Text, browser
				.selectedOptionInDropDownMenuWithId(selectId));
		browser.selectOptionFromDropDownMenuWithId(selectId, option1Text);
		assertEquals(option1Text, browser
				.selectedOptionInDropDownMenuWithId(selectId));
	}

	@Test(expected = NoSuchElementException.class)
	public void testSelectOptionFromDropDownMenuWithIdNoSuchElementException ()
			throws Exception
	{
		browser.selectOptionFromDropDownMenuWithId("X", option1Text);
	}

	@Test(expected = NoSuchOptionException.class)
	public void testSelectOptionFromDropDownMenuWithIdNoSuchOptionException ()
			throws Exception
	{
		browser.selectOptionFromDropDownMenuWithId(selectId, "X");
	}

	@Test(expected = NoSuchElementException.class)
	public void testSelectedOptionInDropDownMenuWithIdNoSuchElementException ()
			throws Exception
	{
		browser.selectedOptionInDropDownMenuWithId("X");
	}
}
