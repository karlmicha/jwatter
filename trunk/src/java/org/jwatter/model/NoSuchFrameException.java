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
 * Thrown when a frame with a given name does not exist in a window or frame.
 */
public class NoSuchFrameException
        extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NoSuchFrameException () {
        super();
    }

    public NoSuchFrameException (String message) {
        super(message);
    }

    public NoSuchFrameException (Class<? extends Frame> frameClass,
                                 Class<? extends Window> windowClass) {
        super(frameClass.getSimpleName() + " does not exist in " + windowClass.getSimpleName());
    }

    public NoSuchFrameException (String name, Class<? extends Window> windowClass) {
        super("frame " + name + " does not exist in " + windowClass.getSimpleName());
    }

}
