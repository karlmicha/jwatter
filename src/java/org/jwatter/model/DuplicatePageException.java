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
 * Exception thrown when two pages with the same class are added to a window.
 */
public class DuplicatePageException
        extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public DuplicatePageException () {
        super();
    }

    public DuplicatePageException (String message) {
        super(message);
    }

    public DuplicatePageException (Class<? extends Page> pageClass) {
        super("Duplicate page " + pageClass.getSimpleName());
    }

    public DuplicatePageException (Class<? extends Page> pageClass,
                                   Class<? extends Window> windowClass) {
        super("Duplicate page " + pageClass.getSimpleName() + " in " + windowClass.getSimpleName());
    }
}
