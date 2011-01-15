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

import java.util.List;
import java.util.Map;

import org.jwatter.browser.WebAutomationFramework;
import org.jwatter.html.Element;

public class GetHtmlElementsRequest extends AbstractRequest implements
															BrowserRequest
{
	protected String elementName;
	protected Map<String, String> elementAttributes;
	protected List<Element> toElements;

	public GetHtmlElementsRequest ( String elementName, List<Element> toElements )
	{
		this(elementName, null, toElements);
	}

	public GetHtmlElementsRequest ( String elementName,
			Map<String, String> elementAttributes, List<Element> toElements )
	{
		this.elementName = elementName;
		this.elementAttributes = elementAttributes;
		this.toElements = toElements;
	}

	public void handle ( WebAutomationFramework browser ) throws Exception
	{
		toElements.addAll(browser.getElementsByName(elementName,
				elementAttributes));
	}

}
