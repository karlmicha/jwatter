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


/**
 * Exception thrown when a page of a specific class does not exist or is not found (for example in a
 * window).
 */
public class NoSuchPageException
        extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoSuchPageException () {
        super();
    }

    public NoSuchPageException (String message) {
        super(message);
    }

    public NoSuchPageException (Class<? extends Page> pageClass) {
        super("page not found: " + pageClass.getSimpleName());
    }

    public NoSuchPageException (Class<? extends Page> pageClass,
                                Class<? extends Window> windowClass) {
        super(pageClass.getSimpleName() + " page does not exist in " + windowClass.getSimpleName());
    }
}
