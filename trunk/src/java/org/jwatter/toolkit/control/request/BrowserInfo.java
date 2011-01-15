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
package org.jwatter.toolkit.control.request;

import java.net.MalformedURLException;
import java.net.URL;

public class BrowserInfo
{
	protected String url;
	protected String pageTitle;
	protected String browserVersion;

	/**
	 * @return the url
	 */
	public String getUrl ()
	{
		return url;
	}

	/**
	 * @param url
	 *            the url to set
	 */
	public void setUrl ( String url )
	{
		this.url = url;
	}

	/**
	 * @return the pageTitle
	 */
	public String getPageTitle ()
	{
		return pageTitle;
	}

	/**
	 * @param pageTitle
	 *            the pageTitle to set
	 */
	public void setPageTitle ( String pageTitle )
	{
		this.pageTitle = pageTitle;
	}

	/**
	 * @return the browserVersion
	 */
	public String getBrowserVersion ()
	{
		return browserVersion;
	}

	/**
	 * @param browserVersion
	 *            the browserVersion to set
	 */
	public void setBrowserVersion ( String browserVersion )
	{
		this.browserVersion = browserVersion;
	}

	public String getPath ()
	{
		if ( url == null ) return null;
		try
		{
			return new URL(url).getPath();
		}
		catch ( MalformedURLException e )
		{
			return null;
		}
	}
}
