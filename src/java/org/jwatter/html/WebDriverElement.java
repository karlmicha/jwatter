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
package org.jwatter.html;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;

public class WebDriverElement implements Element
{
	protected final String[] DEFAULT_ATTRIBUTES = new String[]
	{
			"id", "name", "type", "alt", "class", "src", "href", "value"
	};
	public static final int TRUNCATE_ELEMENT_TEXT = 40;

	protected WebElement webdriverElement;
	protected String nameCache = null;
	protected Map<String, String> attributeCache =
			new HashMap<String, String>();

	public WebDriverElement ( WebElement element )
	{
		webdriverElement = element;
	}

	public String getName ()
	{
		if ( nameCache == null )
		{
			nameCache = webdriverElement.getTagName();
		}
		return nameCache;
	}

	public String getId ()
	{
		return getAttributeValue("id");
	}

	public String getCssClass ()
	{
		return getAttributeValue("class");
	}

	public String getAttributeValue ( String attribute )
	{
		if ( !attributeCache.containsKey(attribute) )
		{
			String value = webdriverElement.getAttribute(attribute);
			attributeCache.put(attribute, value);
			return value;
		}
		return attributeCache.get(attribute);
	}

	public String getText ()
	{
		return getText(0);
	}

	public String getText ( int truncate )
	{
		return getText(truncate, false);
	}

	public String getText ( int truncate, boolean normalizeWhitespace )
	{
		if ( truncate < 0 )
		{
			throw new IllegalArgumentException(
					"cannot truncate to negative length");
		}
		String text = webdriverElement.getText();
		if ( normalizeWhitespace )
		{
			text = text.replaceAll("\\s+", " ").trim();
		}
		if ( truncate == 0 || text.length() <= truncate ) return text;
		if ( truncate <= 3 ) return text.substring(0, truncate);
		return text.substring(0, truncate - 3) + "...";
	}

	public Element getParent ()
	{
		try
		{
			return new WebDriverElement(webdriverElement.findElement(By
					.xpath("parent::*")));
		}
		catch ( NoSuchElementException e )
		{
			return null;
		}
	}

	public Element getChild ( int childindex )
	{
		if ( childindex < 1 )
		{
			throw new IllegalArgumentException("childindex cannot be negative");
		}
		try
		{
			return new WebDriverElement(webdriverElement.findElement(By
					.xpath("*[" + childindex + "]")));
		}
		catch ( NoSuchElementException e )
		{
			return null;
		}
	}

	public List<Element> getChildren ()
	{
		return toElementList(webdriverElement.findElements(By.xpath("*")));
	}

	public List<Element> getDescendantsByTagname ( String tagname )
	{
		return toElementList(webdriverElement.findElements(By.tagName(tagname)));
	}

	public void inspect ( PrintStream out )
	{
		out.println(toString() + " \"" + getText(TRUNCATE_ELEMENT_TEXT, true)
				+ "\"");
		for ( Element child : getChildren() )
		{
			out.println("|- " + child.toString() + " \""
					+ child.getText(TRUNCATE_ELEMENT_TEXT, true) + "\"");
		}
	}

	@Override
	public String toString ()
	{
		StringBuilder a = new StringBuilder();
		for ( String att : DEFAULT_ATTRIBUTES )
		{
			String value = getAttributeValue(att);
			if ( value != null )
			{
				if ( a.length() > 0 ) a.append(", ");
				a.append(att).append("=\"").append(value).append("\"");
			}
		}
		if ( a.length() > 0 )
		{
			return getName() + "[" + a.toString() + "]";
		}
		else
		{
			return getName();
		}
	}

	protected static List<Element> toElementList ( List<WebElement> webelements )
	{
		List<Element> elements = new ArrayList<Element>();
		for ( WebElement e : webelements )
		{
			elements.add(new WebDriverElement(e));
		}
		return elements;
	}
}
