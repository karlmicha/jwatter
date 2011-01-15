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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.jwatter.browser.WebAutomationFramework;

/**
 * Annotation to be used on {@link WebAutomationFramework} methods to indicate
 * which HTML element a method handles and which attribute value it expects as
 * its argument.
 * 
 * Example:
 * 
 * <pre>
 * &#064;HtmlElement(name = &quot;img&quot;)
 * public void clickImageWithUrl ( @HtmlAttribute(&quot;src&quot;) String imageurl )
 * 		throws NoSuchElementException, AmbiguousElementException,
 * 		URISyntaxException, Exception;
 * 
 * &#064;HtmlElement(name =
 * {
 * 		&quot;input&quot;, &quot;button&quot;
 * }, attributeName =
 * {
 * 		&quot;type&quot;, &quot;&quot;
 * }, attributeValue =
 * {
 * 		&quot;submit&quot;, &quot;&quot;
 * })
 * public void clickButtonWithId ( @HtmlAttribute(&quot;id&quot;) String id )
 * 		throws NoSuchElementException, Exception;
 * </pre>
 * 
 * @author kschneider
 * 
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HtmlElement
{

	String[] name ();

	String[] attributeName () default {};

	String[] attributeValue () default {};
}
