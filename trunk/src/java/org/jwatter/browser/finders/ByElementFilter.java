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

import java.util.List;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import org.jwatter.util.ListFilter;

/**
 * A locating mechanism that first retrieves elements using a base locating
 * mechanism and then applies a filter to the list of elements and returns the
 * list of elements that pass the filter. A concrete subclass of this class must
 * implement the {@link #accept(WebElement)} method that is used by the filter.
 * 
 * @author kschneider
 * 
 */
public abstract class ByElementFilter extends By {

	protected org.openqa.selenium.By base;

	/**
	 * Creates a new instance that applies the filter to all elements in the
	 * HTML tree.
	 */
	public ByElementFilter () {
		this(By.xpath("//*"));
	}

	/**
	 * Creates a new instance that applies the filter to all elements located by
	 * the specified locating mechanism.
	 * 
	 * @param base
	 *            the locating mechanism to use for locating elements
	 */
	public ByElementFilter (org.openqa.selenium.By base) {
		this.base = base;
	}

	@Override
	public List<WebElement> findElements (SearchContext context) {
		ListFilter<WebElement> listFilter = new ListFilter<WebElement>() {
			@Override
			public boolean eval (WebElement element) {
				return accept(element);
			}
		};
		return listFilter.filter(context.findElements(base));
	}

	protected abstract boolean accept (WebElement element);

}
