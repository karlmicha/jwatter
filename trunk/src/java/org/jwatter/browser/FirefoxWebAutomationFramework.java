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

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;


public class FirefoxWebAutomationFramework
		extends WebDriverWebAutomationFramework
		implements WebAutomationFramework {

	/**
	 * Creates a new Firefox browser instance with a blank window.
	 * 
	 * @throws Exception
	 *             if an error occurs
	 */
	@Override
	public void createBrowser () throws Exception {
	    FirefoxProfile profile = new FirefoxProfile();
	    profile.setAcceptUntrustedCertificates(false);
		browser = new FirefoxDriver(profile);
		initBrowser();
	}

	/**
	 * Creates a new Firefox browser instance with a blank window, using the specified
	 * profile.
	 * 
	 * @param profileName
	 *             the name of the profile to use
	 * @throws Exception
	 *             if an error occurs
	 */
	@Override
	public void createBrowser ( String profileName ) throws Exception {
	    FirefoxProfile profile = new ProfilesIni().getProfile(profileName);
	    profile.setAcceptUntrustedCertificates(false);
		browser = new FirefoxDriver(profile);
		initBrowser();
	}

	@Override
	public String getBrowserVersion () {
		return "Firefox";
	}
}
