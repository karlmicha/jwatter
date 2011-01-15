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
package org.jwatter.toolkit.generate;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.jwatter.browser.WebAutomationFramework;
import org.jwatter.html.Element;
import org.jwatter.util.ListFilter;

/**
 * Knows which methods the {@link WebAutomationFramework} has, for which HTML
 * elements and which attributes. Relies on the {@link HtmlElement} annotations
 * in {@link WebAutomationFramework}.
 * 
 * @author kschneider
 * 
 */
public class ElementActorBroker
{
	protected static final List<ElementActorMethod> actorMethods;
	protected static final HashMap<String, List<ElementActorMethod>> actorMethodsForElement;

	static
	{
		actorMethods = new ArrayList<ElementActorMethod>();
		actorMethodsForElement =
				new HashMap<String, List<ElementActorMethod>>();

		for ( Method method : WebAutomationFramework.class.getMethods() )
		{
			if ( !ElementActorMethod.isElementActorMethod(method) ) continue;

			ElementActorMethod actorMethod = new ElementActorMethod(method);

			actorMethods.add(actorMethod);

			for ( String elementName : actorMethod.getHtmlElementNames() )
			{
				if ( !actorMethodsForElement.containsKey(elementName) )
				{
					actorMethodsForElement.put(elementName,
							new ArrayList<ElementActorMethod>());
				}
				actorMethodsForElement.get(elementName).add(actorMethod);
			}
		}
	}

	public static Collection<String> getSupportedElements ()
	{
		return actorMethodsForElement.keySet();
	}

	public static List<ElementActorMethod> getElementActorMethods ()
	{
		return actorMethods;
	}

	public static List<ElementActorMethod> getElementActorMethods (
			final Element element )
	{
		if ( !actorMethodsForElement.containsKey(element.getName()) ) return Collections
				.emptyList();

		return new ListFilter<ElementActorMethod>()
		{
			@Override
			public boolean eval ( ElementActorMethod method )
			{
				return method.isForElement(element);
			}
		}.filter(actorMethodsForElement.get(element.getName()));
	}

}
