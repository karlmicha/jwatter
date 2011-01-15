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

import org.w3c.dom.Element;


import watij.finders.BaseFinder;

public class InnerTextFinder extends BaseFinder {

	protected String what;

	protected boolean caseSensitive;

	public InnerTextFinder (String what) {
		this(what, true);
	}

	public InnerTextFinder (String what, boolean caseSensitive) {
		this.what = what;
		this.caseSensitive = caseSensitive;
	}

	public boolean matches (Element element) throws Exception {
		String innerText = element.getTextContent().trim();
		if( what == null ) {
			return innerText == null;
		} else {
			return caseSensitive ? what.equals(innerText) : what
					.equalsIgnoreCase(innerText);
		}
	}

}
