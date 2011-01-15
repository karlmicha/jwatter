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
package org.jwatter.model;

import org.jwatter.browser.WebAutomationFramework;

public class PageFactory {

    /**
     * Creates a new page of the specified class.
     * 
     * @param pageClass
     *        the requested page class
     * @param browserFramework
     *        the browser automation framework
     * @return an instance of the requested page class
     * @throws Exception
     *         if the page cannot be created for some reason
     */
    public static <T extends Page> T newPage (Class<T> pageClass,
                                              WebAutomationFramework browserFramework)
            throws Exception {
        return pageClass.getConstructor(WebAutomationFramework.class).newInstance(browserFramework);
    }
}
