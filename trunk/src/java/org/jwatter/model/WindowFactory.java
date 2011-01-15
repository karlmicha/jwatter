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

public class WindowFactory {

    /**
     * Creates a new window of the specified class.
     * 
     * @param windowClass
     *        the requested window class
     * @param browserFramework
     *        the browser automation framework
     * @return an instance of the requested window class
     * @throws Exception
     *         if the window cannot be created for some reason
     */
    public static <T extends Window> T newWindow (Class<T> windowClass,
                                                  WebAutomationFramework browserFramework)
            throws Exception {
        return windowClass.getConstructor(WebAutomationFramework.class)
                          .newInstance(browserFramework);
    }

    /**
     * Creates a new frame of the specified class.
     * 
     * @param frameClass
     *        the requested frame class
     * @param browserFramework
     *        the browser automation framework
     * @param parent
     *        the frame parent window
     * @return an instance of the requested class
     * @throws Exception
     *         if the frame cannot be created for some reason
     */
    public static <T extends Frame> T newFrame (Class<T> frameClass,
                                                WebAutomationFramework browserFramework,
                                                Window parent)
            throws Exception {
        return frameClass.getConstructor(WebAutomationFramework.class, Window.class)
                         .newInstance(browserFramework, parent);
    }

}
