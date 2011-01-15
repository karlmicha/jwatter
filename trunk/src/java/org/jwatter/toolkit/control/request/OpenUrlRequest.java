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

import org.jwatter.browser.WebAutomationFramework;

public class OpenUrlRequest extends AbstractRequest implements BrowserRequest
{
	protected String url;

	public OpenUrlRequest ( String url )
	{
		this.url = url;
	}

	public void handle ( WebAutomationFramework browser ) throws Exception
	{
		System.out.print("Opening " + url);
		browser.loadUrl(url);
		System.out.println();
	}

}
