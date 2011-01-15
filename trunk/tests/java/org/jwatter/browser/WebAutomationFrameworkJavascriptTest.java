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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.jwatter.util.FunctionalTestProperties;
import org.jwatter.browser.HtmlUnitWebAutomationFramework;
import org.jwatter.browser.WebAutomationFramework;

public class WebAutomationFrameworkJavascriptTest
{
	private static final Class<WebAutomationFrameworkJavascriptTest> myclass =
			WebAutomationFrameworkJavascriptTest.class;
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
	protected static final String alertLinkText =
			properties.getRequiredProperty("alertLinkText", myclass);
	protected static final String confirmLinkText =
			properties.getRequiredProperty("confirmLinkText", myclass);
	protected static final String confirmReturnValueSpanClass =
			properties.getRequiredProperty("confirmReturnValueSpanClass",
					myclass);

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
		browser.closeBrowser();
	}
	
	@Before
	public void resetBrowser () throws Exception
	{
	    browser.enableJavascriptAlert();
	    browser.enableJavascriptConfirm();
	}

	@Test
	public void hasLinkWithTextTest () throws Exception
	{
		assertTrue(browser.hasLinkWithText(alertLinkText));
	}

	@Test(timeout = 1000)
	public void disableJavascriptAlertTest () throws Exception
	{
		browser.disableJavascriptAlert();
		browser.clickLinkWithText(alertLinkText);
	}

//	@Test
	public void enableJavascriptAlertTest () throws Exception
	{
		System.out.println("Please click OK on the alert window!");
		long t = System.currentTimeMillis();
		browser.clickLinkWithText(alertLinkText);
		long t2 = System.currentTimeMillis();
		System.out.println("dt = " + (t2 - t) + " ms");
		assertTrue(t2 - t > 500);
	}

	@Test(timeout = 1000)
	public void disableJavascriptConfirmTrueTest () throws Exception
	{
		browser.disableJavascriptConfirm(true);
		browser.clickLinkWithText(confirmLinkText);
		assertEquals("true", browser
				.contentsOfSpanWithClass(confirmReturnValueSpanClass));
	}

	@Test(timeout = 1000)
	public void disableJavascriptConfirmFalseTest () throws Exception
	{
		browser.disableJavascriptConfirm(false);
		browser.clickLinkWithText(confirmLinkText);
		assertEquals("false", browser
				.contentsOfSpanWithClass(confirmReturnValueSpanClass));
	}

	@Test
	public void enableJavascriptConfirmTest () throws Exception
	{
		System.out.println("Please click OK or Cancel on the confirm window!");
		long t = System.currentTimeMillis();
		browser.clickLinkWithText(confirmLinkText);
		long t2 = System.currentTimeMillis();
		System.out.println("dt = " + (t2 - t) + " ms");
		assertTrue(t2 - t > 500);
	}
}
