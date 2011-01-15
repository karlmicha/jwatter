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
package org.jwatter.browser;

import java.util.List;

import org.jwatter.util.ListFilter;

public abstract class ElementSelector<T>
        extends ListFilter<T> {

    public T filter (List<T> list, int which, String... exceptionArgs)
            throws NoSuchElementException, AmbiguousElementException {
        List<T> elements = filter(list);
        if (elements.size() > 0) {
            if (which == 0) {
                if (elements.size() > 1) {
                    throw new AmbiguousElementException(exceptionArgs);
                }
                else {
                    return elements.get(0);
                }
            }
            else if (which > elements.size()) {
                throw new NoSuchElementException(which, elements.size(), exceptionArgs);
            }
            else {
                return elements.get(which - 1);
            }
        }
        else {
            throw new NoSuchElementException(exceptionArgs);
        }
    }
}
