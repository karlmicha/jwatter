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

import org.jwatter.browser.WebAutomationFramework;

public class GetWindowNamesRequest extends AbstractRequest implements
															BrowserRequest
{
	// list contains the name of the currently active window, followed by the
	// names of all open windows that are descendants of the default window
	// (including the default window)
	protected List<String> toWindowNames;

	public GetWindowNamesRequest ( List<String> toWindowNames )
	{
		this.toWindowNames = toWindowNames;
	}

	public void handle ( WebAutomationFramework browser ) throws Exception
	{
		toWindowNames.clear();
		toWindowNames.add(browser.getWindowName());
		toWindowNames.addAll(browser.getWindowNames());
	}

}
