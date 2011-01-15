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

import org.w3c.dom.Element;


import watij.finders.BaseFinder;

public class UrlFinder extends BaseFinder {

	protected String attr;
	protected String whichUrl;
	protected String baseUrl;
	protected URI whichURI;
	protected URI baseURI;

	/**
	 * Constructs a new URL finder that matches the given URL against the href
	 * attribute of an element. Only absolute URLs match.
	 * 
	 * @param url
	 *            an absolute URL to look for.
	 * @throws URISyntaxException
	 */
	public UrlFinder (String url) throws URISyntaxException {
		this("href", url);
	}

	/**
	 * Constructs a new URL finder. Only absolute URLs match.
	 * 
	 * @param attr
	 *            the attribute that contains the URL of an element (e.g. href
	 *            or src)
	 * @param url
	 *            an absolute URL to look for
	 * @throws URISyntaxException
	 */
	public UrlFinder (String attr, String url) throws URISyntaxException {
		this(attr, url, null);
	}

	/**
	 * Constructs a new URL finder. If baseUrl is null, url must be absolute,
	 * and the URL of an element must be absolute in order to match. If baseUrl
	 * is not null, both url and the element URL will be resolved against
	 * baseUrl.
	 * 
	 * @param attr
	 *            the attribute that contains the URL of an element (e.g. href
	 *            or src)
	 * @param url
	 *            the URL to look for
	 * @param baseUrl
	 * @throws URISyntaxException
	 */
	public UrlFinder (String attr, String url, String baseUrl)
			throws URISyntaxException {
		if( url == null ) {
			throw new IllegalArgumentException("url must not be null");
		}
		this.attr = attr;
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

	public boolean matches (Element element) throws Exception {
		String elUrl = element.getAttribute(attr);
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
