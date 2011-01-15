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

public class BrowserTargetRequest extends AbstractRequest implements
															BrowserRequest
{
	public static final int FRAME = 1;
	public static final int WINDOW = 2;

	protected int type;
	protected int frameIndex;
	protected String address = null;

	public BrowserTargetRequest ( int frameIndex )
	{
		this.type = FRAME;
		this.frameIndex = frameIndex;
	}

	public BrowserTargetRequest ( int type, String address )
	{
		if ( type != FRAME && type != WINDOW )
		{
			throw new IllegalArgumentException("unknown target type " + type);
		}
		this.type = type;
		this.address = address;
	}

	public void handle ( WebAutomationFramework browser ) throws Exception
	{
		if ( type == WINDOW )
		{
			browser.setTargetToWindow(address);
		}
		else if ( address != null )
		{
			browser.setTargetToFrameWithName(address);
		}
		else
		{
			browser.setTargetToFrame(frameIndex);
		}
	}
}
