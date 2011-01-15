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

public abstract class By extends org.openqa.selenium.By {

	public static By tag (final String tagname) {
		if( tagname == null ) {
			throw new IllegalArgumentException(
					"Cannot find elements with a null tagname.");
		}
		return new By() {
			@Override
			public List<WebElement> findElements (SearchContext context) {
				return context.findElements(By.xpath("//" + tagname));
			}
		};
	}
}
