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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Method;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import org.jwatter.browser.WebAutomationFramework;

public class ElementActorMethodTest
{
	Method interfaceAttributeMethod;
	Method interfaceAttributeIndexMethod;
	Method interfaceContentMethod;
	Method interfaceAttributeFreeParameterMethod;

	ElementActorMethod actorAttributeMethod;
	ElementActorMethod actorAttributeIndexMethod;
	ElementActorMethod actorContentMethod;
	ElementActorMethod actorAttributeFreeParameterMethod;

	@Before
	public void setUp () throws Exception
	{
		interfaceAttributeMethod =
				WebAutomationFramework.class.getMethod(
						"valueOfTextInputWithId", String.class);
		interfaceAttributeIndexMethod =
				WebAutomationFramework.class.getMethod(
						"valueOfTextInputWithName", String.class, int.class);
		interfaceContentMethod =
				WebAutomationFramework.class.getMethod("clickLinkWithText",
						String.class);
		interfaceAttributeFreeParameterMethod =
				WebAutomationFramework.class.getMethod(
						"setPasswordInputWithId", String.class, String.class);

		actorAttributeMethod = new ElementActorMethod(interfaceAttributeMethod);
		actorAttributeIndexMethod =
				new ElementActorMethod(interfaceAttributeIndexMethod);
		actorContentMethod = new ElementActorMethod(interfaceContentMethod);
		actorAttributeFreeParameterMethod =
				new ElementActorMethod(interfaceAttributeFreeParameterMethod);
	}

	@After
	public void tearDown () throws Exception
	{
	}

	@Test
	public void testIsElementActorMethod ()
	{
		assertTrue(ElementActorMethod
				.isElementActorMethod(interfaceAttributeMethod));
		assertTrue(ElementActorMethod
				.isElementActorMethod(interfaceAttributeIndexMethod));
		assertTrue(ElementActorMethod
				.isElementActorMethod(interfaceContentMethod));
		assertTrue(ElementActorMethod
				.isElementActorMethod(interfaceAttributeFreeParameterMethod));
	}

	@Test
	public void testElementActorMethod ()
	{
	}

	@Test
	public void testIsForElement ()
	{
	}

	@Test
	public void testGetHtmlElementNames ()
	{
	}

	@Test
	public void testGetInterfaceMethod ()
	{
	}

	@Test
	public void testGetReturnType ()
	{
	}

	@Test
	public void testGetNameForElement ()
	{
	}

	@Test
	public void testGetHtmlAttributeNameForParameter ()
	{
	}

	@Test
	public void testIsHtmlAttributeParameter ()
	{
	}

	@Test
	public void testIsHtmlElementContentParameter ()
	{
	}

	@Test
	public void testIsHtmlElementIndexParameter ()
	{
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetOutOfBoundsMethodParameterIndex ()
	{
		actorAttributeMethod.getParameterIndex(1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetAttributeMethodParameterIndex ()
	{
		actorAttributeMethod.getParameterIndex(0);
	}

	@Test
	public void testGetAttributeIndexMethodParameterIndex ()
	{
		assertEquals(0, actorAttributeIndexMethod.getParameterIndex(1));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testGetContentMethodParameterIndex ()
	{
		actorContentMethod.getParameterIndex(0);
	}

	@Test
	public void testGetFreeParameterIndex ()
	{
		assertEquals(0, actorAttributeFreeParameterMethod.getParameterIndex(1));
	}

	@Test
	public void testGetParameterTypes ()
	{
	}

	@Test
	public void testGetParameterNames ()
	{
	}

	@Test
	public void testGetExceptionTypes ()
	{
	}

}
