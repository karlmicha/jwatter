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
package org.jwatter.browser.finders;

import java.net.URI;
import java.net.URISyntaxException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * This locating mechanism locates elements with a specific URL. It first
 * locates elements using a base locator (or all elements if no base locator is
 * specified) and then filters the elements returned by the base locator on the
 * specified URL attribute.
 * 
 * @author kschneider
 * 
 */
public class ByUrlFilter extends ByElementFilter {

	protected String urlAttribute;
	protected String whichUrl;
	protected String baseUrl;
	protected URI whichURI;
	protected URI baseURI;

	/**
	 * Creates a new instance that locates all elements with the specified URL.
	 * 
	 * @param urlAttribute
	 *            the name of the URL attribute to use
	 * @param url
	 *            the URL to look for (must be absolute)
	 * @throws URISyntaxException
	 *             if the URL is not wellformed
	 */
	public ByUrlFilter (String urlAttribute, String url)
			throws URISyntaxException {
		this(urlAttribute, url, null);
	}

	/**
	 * Creates a new instance that locates all elements with the specified URL.
	 * If the specified URL is not absolute, it is resolved against the
	 * specified base URL. If an element has a relative URL, it is resolved
	 * against the specified base URL before comparing it to the specified URL.
	 * 
	 * @param urlAttribute
	 *            the name of the URL attribute to use
	 * @param url
	 *            the URL to look for
	 * @param baseUrl
	 *            the base URL to use for resolving relative URLs
	 * @throws URISyntaxException
	 *             if any of the URLs are not wellformed
	 */
	public ByUrlFilter (String urlAttribute, String url, String baseUrl)
			throws URISyntaxException {
		this(By.xpath("//*"), urlAttribute, url, baseUrl);
	}

	/**
	 * Creates a new instance that locates elements using the specified base
	 * locator and then filters those elements on the specified URL.
	 * 
	 * @param base
	 *            the locator to use for locating the elements to filter
	 * @param urlAttribute
	 *            the name of the URL attribute to use
	 * @param url
	 *            the URL to look for (must be absolute)
	 * @throws URISyntaxException
	 *             if the URL is not wellformed
	 */
	public ByUrlFilter (By base, String urlAttribute, String url)
			throws URISyntaxException {
		this(base, urlAttribute, url, null);
	}

	/**
	 * Creates a new instance that locates elements using the specified base
	 * locator and then filters those elements on the specified URL. If the
	 * specified URL is not absolute it is resolved against the specified base
	 * URL. If an element has a relative URL, it is resolved against the
	 * specified base URL before comparing it to the specified URL.
	 * 
	 * @param base
	 *            the locator to use for locating the elements to filter
	 * @param urlAttribute
	 *            the name of the URL attribute to use
	 * @param url
	 *            the URL to look for
	 * @param baseUrl
	 *            the base URL to use for resolving relative URLs
	 * @throws URISyntaxException
	 *             if any of the URLs are not wellformed
	 */
	public ByUrlFilter (By base, String urlAttribute, String url, String baseUrl)
			throws URISyntaxException {
		super(base);
		if( url == null ) {
			throw new IllegalArgumentException("url must not be null");
		}
		this.urlAttribute = urlAttribute;
		this.whichUrl = url;
		this.baseUrl = baseUrl;
		baseURI = null;
		if( baseUrl != null ) {
			baseURI = new URI(baseUrl);
			whichURI = baseURI.resolve(whichUrl);
		} else {
			whichURI = new URI(whichUrl);
		}
	}

	@Override
	protected boolean accept (WebElement element) {
		String elUrl = element.getAttribute(urlAttribute);
		if( elUrl == null ) {
			return false;
		}
		try {
			URI elURI = baseURI.resolve(elUrl);
			return elURI.equals(whichURI);
		} catch( IllegalArgumentException e ) {
			return false;
		}
	}

}
