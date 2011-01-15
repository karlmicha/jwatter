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
package org.jwatter.toolkit.control;

import org.jwatter.toolkit.control.request.Request;

public class RequestBuffer
{
	protected Request request;

	public RequestBuffer ()
	{
		request = null;
	}

	public synchronized void submit ( Request request ) throws InterruptedException
	{
		while ( this.request != null )
		{
			wait();
		}
		this.request = request;
		notifyAll();
	}

	public synchronized Request retrieve () throws InterruptedException
	{
		while ( this.request == null )
		{
			wait();
		}
		Request retrieve = this.request;
		this.request = null;
		return retrieve;
	}
}
