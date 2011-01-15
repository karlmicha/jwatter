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

import org.jwatter.toolkit.control.BrowserController;
import org.jwatter.toolkit.control.BrowserControllerException;

public class CreateBrowserRequest extends AbstractRequest implements
															BrowserControllerRequest
{
	protected String profileName;

	public CreateBrowserRequest ()
	{
		this(null);
	}

	public CreateBrowserRequest ( String profileName )
	{
		super();
		this.profileName = profileName;
	}

	public void handle ( BrowserController controller )
			throws BrowserControllerException
	{
		controller.createBrowser(profileName);
	}

}
