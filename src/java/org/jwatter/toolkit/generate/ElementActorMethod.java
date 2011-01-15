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

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.jwatter.html.Element;
import org.jwatter.util.ReflectUtil;
import org.jwatter.util.StringUtil;

/**
 * Wrapper around a method in the {@link WebAutomationFramework} interface.
 * Knows which paramaters the interface methods expects and how they are mapped
 * to element attributes, element content and actor method parameters.
 * 
 * An instance of this class represents any actor method that calls the wrapped
 * interface method. As such, multiple actor methods in a generated actor page
 * class can be represented by the same instance. It provides methods to
 * retrieve a name and unique identifier for a specific actor method, given the
 * element that the actor method acts upon.
 * 
 * @author kschneider
 * 
 */
public class ElementActorMethod
{
	public static boolean isElementActorMethod ( Method method )
	{
		return method.isAnnotationPresent(HtmlElement.class);
	}

	protected Method interfaceMethod;

	// element names in the method annotation
	protected String[] elementNames;
	// attribute names in the method annotation
	protected String[] elementAttributeNames;
	// attribute values in the method annotation
	protected String[] elementAttributeValues;

	// name in attribute annotation on parameter, or null if no attribute
	// annotation present
	protected String[] parameterAttributeNames;
	// index of parameter with element content annotation, or null if no element
	// content annotation present
	protected Integer elementContentParameterIndex;
	// index of parameter with element index annotation, or null if no element
	// index annotation present
	protected Integer elementIndexParameterIndex;

	// parameter types of this actor method
	protected Class<?>[] parameterTypes;
	// parameter names of this actor method
	protected String[] parameterNames;

	// mapping of interface method parameters to actor method parameters
	protected Map<Integer, Integer> parameterMap;

	public ElementActorMethod ( Method interfaceMethod )
			throws IllegalArgumentException, HtmlAnnotationError
	{
		// check if annotation is present
		HtmlElement elementAnnotation =
				interfaceMethod.getAnnotation(HtmlElement.class);
		if ( elementAnnotation == null )
		{
			throw new IllegalArgumentException("missing @"
					+ HtmlElement.class.getSimpleName()
					+ " annotation on method "
					+ ReflectUtil.getSignature(interfaceMethod));
		}

		this.interfaceMethod = interfaceMethod;
		this.elementNames = elementAnnotation.name();
		this.elementAttributeNames = elementAnnotation.attributeName();
		this.elementAttributeValues = elementAnnotation.attributeValue();

		checkElementAnnotation();
		checkParameterAnnotations();
	}

	protected void checkElementAnnotation () throws HtmlAnnotationError
	{
		// check if element annotation is complete
		if ( elementNames == null || elementNames.length == 0 )
		{
			throw new HtmlAnnotationError("missing element name in @"
					+ HtmlElement.class.getSimpleName()
					+ " annotation on method "
					+ ReflectUtil.getSignature(interfaceMethod));
		}
		for ( String elementName : elementNames )
		{
			if ( elementName == null || elementName.equals("") )
			{
				throw new HtmlAnnotationError("null or empty name in @"
						+ HtmlElement.class.getSimpleName()
						+ " annotation on method "
						+ ReflectUtil.getSignature(interfaceMethod));
			}
		}
	}

	protected void checkParameterAnnotations () throws HtmlAnnotationError
	{
		// check if parameter annotations (if present) are complete
		Annotation[][] parameterAnnotations =
				interfaceMethod.getParameterAnnotations();
		parameterAttributeNames = new String[parameterAnnotations.length];
		elementContentParameterIndex = null;
		elementIndexParameterIndex = null;
		parameterMap = new LinkedHashMap<Integer, Integer>();
		int actorMethodParameterCount = 0;
		for ( int parameterIndex = 0 ; parameterIndex < parameterAnnotations.length ; parameterIndex++ )
		{
			parameterAttributeNames[parameterIndex] = null;

			// each parameter can have at most one annotation
			int annotationCount = 0;

			// check all annotations on current parameter
			for ( Annotation parameterAnnotation : parameterAnnotations[parameterIndex] )
			{
				if ( parameterAnnotation instanceof HtmlAttribute )
				{
					annotationCount++;
					String attribute =
							((HtmlAttribute) parameterAnnotation).value();
					if ( attribute == null || attribute.equals("") )
					{
						throw new HtmlAnnotationError("null or empty name in @"
								+ HtmlAttribute.class.getSimpleName()
								+ " annotation on a parameter of method "
								+ ReflectUtil.getSignature(interfaceMethod));
					}
					parameterAttributeNames[parameterIndex] = attribute;
				}
				else if ( parameterAnnotation instanceof HtmlElementContent )
				{
					annotationCount++;

					// only one parameter can have element content annotation
					if ( elementContentParameterIndex != null )
					{
						throw new HtmlAnnotationError(
								"@"
										+ HtmlElementContent.class
												.getSimpleName()
										+ " annotation present on more than one parameter of method "
										+ ReflectUtil
												.getSignature(interfaceMethod));
					}
					elementContentParameterIndex = parameterIndex;
				}
				else if ( parameterAnnotation instanceof HtmlElementIndex )
				{
					annotationCount++;

					// only one parameter can have element index annotation
					if ( elementIndexParameterIndex != null )
					{
						throw new HtmlAnnotationError(
								"@"
										+ HtmlElementIndex.class
												.getSimpleName()
										+ " annotation present on more than one parameter of method "
										+ ReflectUtil
												.getSignature(interfaceMethod));
					}
					elementIndexParameterIndex = parameterIndex;

					// element index parameter is passed through
					parameterMap.put(new Integer(parameterIndex), new Integer(
							actorMethodParameterCount++));
				}
			}

			if ( annotationCount == 0 )
			{
				// parameter without annotation is passed through
				parameterMap.put(new Integer(parameterIndex), new Integer(
						actorMethodParameterCount++));
			}

			// more than one annotation present on current parameter?
			else if ( annotationCount > 1 )
			{
				throw new HtmlAnnotationError(
						"multiple annotations present on a parameter of method "
								+ ReflectUtil.getSignature(interfaceMethod));
			}
		}

		// actor method parameters are the interface method parameters that do
		// not correspond to HTML element attributes or HTML element content
		parameterTypes = new Class<?>[actorMethodParameterCount];
		// actor parameter names are:
		// "which" if the parameter is mapped to an element index parameter
		// arg1, arg2, ... otherwise
		parameterNames = new String[actorMethodParameterCount];
		int argCount = 0;
		for ( Entry<Integer, Integer> mapping : parameterMap.entrySet() )
		{
			parameterTypes[mapping.getValue()] =
					interfaceMethod.getParameterTypes()[mapping.getKey()];
			parameterNames[mapping.getValue()] =
					isHtmlElementIndexParameter(mapping.getKey())	? "which"
																	: ("arg" + (++argCount));
		}
	}

	public boolean isForElement ( Element element )
	{
		return matchesElementAnnotation(element)
				&& hasRequiredAttributesAndContent(element);
	}

	protected boolean matchesElementAnnotation ( Element element )
	{
		String elementName = element.getName();
		for ( int i = 0 ; i < elementNames.length ; i++ )
		{
			// element matches
			if ( elementName.equals(elementNames[i]) )
			{
				// no restriction on attributes
				if ( i >= elementAttributeNames.length
						|| elementAttributeNames[i] == null
						|| elementAttributeNames[i].equals("") ) return true;

				String attributeValue =
						element.getAttributeValue(elementAttributeNames[i]);
				if ( attributeValue == null ) return false;

				// restrictAttribute but no restrictValue in annotation means
				// element must have the attribute, no matter what its value
				return i >= elementAttributeValues.length
						|| attributeValue.equals(elementAttributeValues[i]);
			}
		}

		// no match found
		return false;
	}

	protected boolean hasRequiredAttributesAndContent ( Element element )
	{
		for ( int parameterIndex = 0 ; parameterIndex < parameterAttributeNames.length ; parameterIndex++ )
		{
			if ( parameterAttributeNames[parameterIndex] != null )
			{
				if ( element
						.getAttributeValue(parameterAttributeNames[parameterIndex]) == null ) return false;
			}
			else if ( isHtmlElementContentParameter(parameterIndex) )
			{
				String text = element.getText();
				if ( text == null || text.trim().length() == 0 ) return false;
			}
		}
		return true;
	}

	public String[] getHtmlElementNames ()
	{
		return elementNames;
	}

	public Method getInterfaceMethod ()
	{
		return interfaceMethod;
	}

	public Class<?> getReturnType ()
	{
		return interfaceMethod.getReturnType();
	}

	/**
	 * Get the name for an actor method represented by this instance that acts
	 * upon a specific element. The name is constructed from the name of the
	 * interface method and the values of all element attributes that are
	 * parameters of the interface method.
	 * 
	 * Note: If the element has an <code>href</code> or <code>src</code>
	 * attribute and that attribute is a parameter of the interface method, and
	 * the attribute value is a URL, only the filename (and the query if
	 * present) is used to build the actor method name.
	 * 
	 * @param element
	 *            the element that the actor method represented by this instance
	 *            acts upon
	 * @return the name of the actor method
	 */
	public String getNameForElement ( Element element )
	{
		StringBuilder name = new StringBuilder();
		name.append(interfaceMethod.getName());
		for ( int interfaceParameterIndex = 0 ; interfaceParameterIndex < parameterAttributeNames.length ; interfaceParameterIndex++ )
		{
			String attribute = parameterAttributeNames[interfaceParameterIndex];
			if ( attribute != null )
			{
				String value = element.getAttributeValue(attribute);
				if ( attribute.equals("href") || attribute.equals("src") )
				{
					value = StringUtil.getUrlFilename(value);
				}
				name.append(StringUtil.makeIdentifier(value));
			}
			else if ( isHtmlElementContentParameter(interfaceParameterIndex) )
			{
				String content =
						StringUtil.makeIdentifier(element.getText(), 20);
				name.append(content);
			}
		}

		return name.toString();
	}

	/**
	 * Get a unique identifier for an actor method represented by this instance
	 * that acts upon a specific element. The unique identifier is constructed
	 * as follows, where <i>f</i> is the name of the interface method,
	 * <i>name</i> is the value of the {@link HtmlAttribute} annotation on the
	 * interface method parameter, <i>value</i> is the value of that attribute
	 * in the element, and <i>content</i> is the text content of the element:
	 * <ul>
	 * <li>if the interface method signature is
	 * <code>f(@HtmlAttribute("name") String)</code>:<br>
	 * <i>f</i>.A.<i>name</i>.<i>value</i>
	 * <li>if the interface method signature is
	 * <code>f(@HtmlAttribute("name") String, @HtmlElementIndex int)</code>:<br>
	 * <i>f</i>.I.A.<i>name</i>.<i>value</i>
	 * <li>if the interface method signature is
	 * <code>f(@HtmlElementContent String)</code>:<br>
	 * <i>f</i>.C.<i>content</i>
	 * <li>if the interface method signature is
	 * <code>f(@HtmlElementContent String, @HtmlElementIndex int)</code>:<br>
	 * <i>f</i>.I.C.<i>content</i>
	 * </ul>
	 * 
	 * @param element
	 *            the element that the actor method represented by this instance
	 *            acts upon
	 * @return the unique identifier for the actor method
	 */
	public String getUniqueIdForElement ( Element element )
	{
		StringBuilder uid = new StringBuilder();
		uid.append(interfaceMethod.getName());
		if ( hasHtmlElementIndexParameter() )
		{
			uid.append(".I");
		}
		for ( String attributeName : parameterAttributeNames )
		{
			if ( attributeName != null )
			{
				uid.append(".A.").append(attributeName).append(".").append(
						element.getAttributeValue(attributeName));
			}
		}
		if ( hasHtmlElementContentParameter() )
		{
			uid.append(".C.").append(element.getText());
		}
		return uid.toString();
	}

	// parameterIndex is for interface method parameter
	public String getHtmlAttributeNameForParameter ( int parameterIndex )
			throws IndexOutOfBoundsException
	{
		return parameterAttributeNames[parameterIndex];
	}

	public boolean isHtmlAttributeParameter ( int parameterIndex )
			throws IndexOutOfBoundsException
	{
		return parameterAttributeNames[parameterIndex] != null;
	}

	public boolean isHtmlElementContentParameter ( int parameterIndex )
			throws IndexOutOfBoundsException
	{
		return elementContentParameterIndex != null
				&& elementContentParameterIndex.intValue() == parameterIndex;
	}

	public boolean isHtmlElementIndexParameter ( int parameterIndex )
			throws IndexOutOfBoundsException
	{
		return elementIndexParameterIndex != null
				&& elementIndexParameterIndex.intValue() == parameterIndex;
	}

	public boolean hasHtmlElementContentParameter ()
	{
		return elementContentParameterIndex != null;
	}

	public boolean hasHtmlElementIndexParameter ()
	{
		return elementIndexParameterIndex != null;
	}

	public int getParameterIndex ( int interfaceParameterIndex )
			throws IndexOutOfBoundsException, IllegalArgumentException
	{
		if ( interfaceParameterIndex < 0
				|| interfaceParameterIndex >= parameterAttributeNames.length )
		{
			throw new IndexOutOfBoundsException();
		}
		Integer actorParameterIndex =
				parameterMap.get(new Integer(interfaceParameterIndex));
		if ( actorParameterIndex == null )
		{
			throw new IllegalArgumentException("parameter "
					+ (interfaceParameterIndex + 1) + " of method "
					+ ReflectUtil.getSignature(interfaceMethod)
					+ " is not a parameter of the corresponding actor method");
		}
		return actorParameterIndex.intValue();
	}

	public Class<?>[] getParameterTypes ()
	{
		return parameterTypes;
	}

	public String[] getParameterNames ()
	{
		return parameterNames;
	}

	public Class<?>[] getExceptionTypes ()
	{
		return interfaceMethod.getExceptionTypes();
	}
}
