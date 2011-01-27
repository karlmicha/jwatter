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
package org.jwatter.test;

import static org.jwatter.util.Require.requireTrue;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.MissingResourceException;
import java.util.logging.Logger;

import junit.framework.AssertionFailedError;
import junit.framework.TestCase;
import junit.framework.TestResult;

import org.jwatter.util.FunctionalTestProperties;
import org.jwatter.util.UnsatisfiedRequirementError;
import org.jwatter.browser.WebAutomationFramework;

/**
 * @author kschneider
 * 
 */
public abstract class WebFunctionalTestCase extends TestCase {

	protected static Logger logger = Logger
			.getLogger(WebFunctionalTestCase.class.getName());

	protected static final String PROPERTIES_FILENAME_PROPERTY =
		"tests.properties";
	protected static final String defaultPropertiesFilename =
		"functionaltest.web.properties";
	protected static String propertiesFilename;
	static {
		propertiesFilename = System.getProperty(PROPERTIES_FILENAME_PROPERTY);
		if (propertiesFilename != null) {
			logger.info(PROPERTIES_FILENAME_PROPERTY + "="
						+ propertiesFilename);
		} else {
			propertiesFilename = defaultPropertiesFilename;
			logger.info("Property " + PROPERTIES_FILENAME_PROPERTY
					+ " is not set, using " + defaultPropertiesFilename);
		}
	}

	protected static FunctionalTestProperties properties;

	static {
		logger.info("Loading properties from " + propertiesFilename);
		try {
			properties = new FunctionalTestProperties(propertiesFilename);
		} catch( IOException e ) {
			throw new RuntimeException(e);
		}
	}

	protected static String browserFrameworkClassName;
	protected static String browserProfileName;
	protected static Class<? extends WebAutomationFramework> defaultBrowserFramework;
	protected static String startUrl;

	static {
		browserFrameworkClassName = getRequiredProperty("browserFrameworkClassName",
		        WebFunctionalTestCase.class);
		browserProfileName = properties.getProperty("profileName",
		        WebFunctionalTestCase.class);
		try {
			defaultBrowserFramework = getBrowserFrameworkClass(browserFrameworkClassName);
		} catch( Exception e ) {
			logger.severe(e.getMessage());
			e.printStackTrace();
		}
		startUrl = properties.getProperty("startUrl", WebFunctionalTestCase.class);
	}

	protected WebAutomationFramework browserFramework;

	/**
	 * Creates a new web functional test case using the browser framework
	 * specified in the properties file.
	 * 
	 * @throws Exception
	 *             if the browser framework cannot be instantiated
	 */
	protected WebFunctionalTestCase () throws Exception {
		this(defaultBrowserFramework);
	}

	/**
	 * Creates a new web functional test case using the browser framework
	 * specified in the properties file.
	 * 
	 * @param name
	 *            a name for the test case
	 * @throws Exception
	 *             if the browser framework cannot be instantiated
	 */
	protected WebFunctionalTestCase (String name) throws Exception {
		this(defaultBrowserFramework, name);
	}

	/**
	 * Creates a new web functional test case using the specified browser
	 * framework.
	 * 
	 * @param frameworkClass
	 *            the class of the browser framework to use
	 * @throws Exception
	 *             if the browser framework cannot be instantiated
	 */
	protected WebFunctionalTestCase (
			Class<? extends WebAutomationFramework> frameworkClass)
			throws Exception {
		this(frameworkClass, null);
	}

	/**
	 * Creates a new web functional test case using the specified browser
	 * framework.
	 * 
	 * @param frameworkClass
	 *            the class of the browser framework to use
	 * @param name
	 *            a name for the test case
	 * @throws Exception
	 *             if the browser framework cannot be instantiated
	 */
	protected WebFunctionalTestCase (
			Class<? extends WebAutomationFramework> frameworkClass, String name)
			throws Exception {
		this(createBrowserFrameworkInstance(frameworkClass), name);
	}

	/**
	 * Creates a new web functional test case using the specified browser
	 * framework instance.
	 * 
	 * @param framework
	 *            the browser framework to use for the tests
	 * @throws Exception
	 *             if an error occurs
	 */
	protected WebFunctionalTestCase (WebAutomationFramework framework)
			throws Exception {
		this(framework, null);
	}

	/**
	 * Creates a new web functional test case using the specified browser
	 * framework instance.
	 * 
	 * @param framework
	 *            the browser framework to use for the tests
	 * @param name
	 *            a name for the test case
	 * @throws Exception
	 *             if an error occurs
	 */
	protected WebFunctionalTestCase (WebAutomationFramework framework,
			String name) throws Exception {
		super(name);
		browserFramework = framework;
	}

	@SuppressWarnings("unchecked")
	protected static Class<? extends WebAutomationFramework> getBrowserFrameworkClass (
			String className) throws Exception {
		Class<?> browserFrameworkClass = Class.forName(className);
		if( WebAutomationFramework.class
				.isAssignableFrom(browserFrameworkClass) ) {
			return (Class<? extends WebAutomationFramework>) browserFrameworkClass;
		} else {
			throw new Exception(className + " is not compatible with "
					+ WebAutomationFramework.class.getName());
		}
	}

	protected static WebAutomationFramework createBrowserFrameworkInstance (
			Class<? extends WebAutomationFramework> frameworkClass)
			throws Exception {
		logger.info("Using browser framework " + frameworkClass.getName());
		Constructor<? extends WebAutomationFramework> browserFrameworkConstructor;
		try {
			browserFrameworkConstructor = ((Class<? extends WebAutomationFramework>) frameworkClass)
					.getConstructor();
		} catch( Exception e ) {
			logger.severe("Could not locate constructor for "
					+ frameworkClass.getName());
			e.printStackTrace();
			throw e;
		}
		try {
			return (WebAutomationFramework) browserFrameworkConstructor
					.newInstance();
		} catch( Exception e ) {
			logger.severe("Could not instantiate " + frameworkClass.getName());
			e.printStackTrace();
			throw e;
		}
	}

	@Override
	protected void setUp () throws Exception {
		super.setUp();
		try {
		    if( browserProfileName != null ) {
		        logger.info("using " + browserProfileName + " profile");
		        logger.info("creating " + browserFramework.getBrowserVersion()
		                + " browser");
		        try {
		            browserFramework.createBrowser(browserProfileName);
		        } catch( NullPointerException e ) {
		            logger.severe("could not create "
		                    + browserFramework.getBrowserVersion()
						    + " browser (profile " + browserProfileName
						    + " may not exist)");
		            throw new Exception("could not create "
						    + browserFramework.getBrowserVersion()
						    + " browser (profile " + browserProfileName
						    + " may not exist)");
		        }
		    } else {
		        logger.info("creating new " + browserFramework.getBrowserVersion()
					    + " profile");
		        logger.info("creating " + browserFramework.getBrowserVersion()
					    + " browser");
		        browserFramework.createBrowser();
		    }

		    if (startUrl != null) {
		        logger.info("loading " + startUrl);
		        browserFramework.loadUrl(startUrl);
		    }
		} catch (Exception e) {
		    super.tearDown();
		    throw e;
		} catch (Error e) {
		    super.tearDown();
		    throw e;
		}
	}

	@Override
	protected void tearDown () throws Exception {
	    try {
	        logger.info("shutting down browser");
	        browserFramework.closeBrowser();
	    } finally {
	        super.tearDown();
	    }
	}

	@Override
	public void run(TestResult result) {
	    logger.info("Running " + this.getClass().getSimpleName() + "." +
	                this.getName());
	    super.run(result);
	}

	public static String getProperty (String propertyName) {
		return properties.getProperty(propertyName);
	}

	public static String getProperty (String propertyName,
			Class<? extends Object> cls) {
		return properties.getProperty(propertyName, cls);
	}

	public static String getRequiredProperty (String propertyName)
			throws MissingResourceException {
		return properties.getRequiredProperty(propertyName);
	}

	public static String getRequiredProperty (String propertyName,
			Class<?> cls)
			throws MissingResourceException {
		return properties.getRequiredProperty(propertyName, cls);
	}

	protected void asserting (String message, boolean condition) {
		try {
			assertTrue(message, condition);
		} catch( AssertionFailedError ae ) {
			try {
				logger.info(message);
				logger.info("URL=" + browserFramework.getUrl());
				logger.info("Title=" + browserFramework.getTitle());
				logger.info("Contents=" + browserFramework.getContents());
			} catch( Exception e ) {
			}
			throw ae;
		}
	}
	
	protected void asserting (boolean condition) {
		asserting("assertion failed", condition);
	}

	protected void requiring (String message, boolean condition) {
		try {
			requireTrue(message, condition);
		} catch( UnsatisfiedRequirementError ure ) {
			try {
				logger.info(message);
				logger.info("URL=" + browserFramework.getUrl());
				logger.info("Title=" + browserFramework.getTitle());
				logger.info("Contents=" + browserFramework.getContents());
			} catch( Exception e ) {
			}
			throw ure;
		}
	}
	
	protected void requiring (boolean condition) {
		requiring("required condition is not true", condition);
	}

}
