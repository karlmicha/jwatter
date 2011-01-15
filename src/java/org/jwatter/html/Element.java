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
import java.util.List;

/**
 * 
 * @author kschneider
 * 
 */
public interface Element
{
	public String getName ();

	public String getId ();

	public String getCssClass ();

	public String getAttributeValue ( String attribute );

	public String getText ();

	public String getText ( int truncate );

	public String getText ( int truncate, boolean normalizeWhitespace );

	public Element getParent ();

	public Element getChild ( int childindex );

	public List<Element> getChildren ();

	public List<Element> getDescendantsByTagname ( String tagname );

	public String toString ();

	public void inspect ( PrintStream out );
}
