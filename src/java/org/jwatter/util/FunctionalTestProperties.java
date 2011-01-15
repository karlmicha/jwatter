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
/**
 * 
 */
package org.jwatter.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * @author kschneider
 *
 */
public class FunctionalTestProperties extends Properties {
	
	static final long serialVersionUID = 0L;
	
	protected static final Logger logger = Logger.getLogger(FunctionalTestProperties.class.getPackage().getName());
	
	public FunctionalTestProperties (String propertiesFilename) throws IOException {
		super();
		
		InputStream propStream = null;
		try {
			propStream = new FileInputStream(propertiesFilename);
			load(propStream);
		} catch (IOException e) {
			logger.severe("unable to load properties file: " + e.getMessage());
			throw new IOException("unable to load properties file: " + e.getMessage());
		} finally {
			if( propStream != null ) {
				propStream.close();
			}
		}

	}
	
	public String getProperty (String propertyName, Class<? extends Object> cls) {
		return getProperty(propertyName, cls, null);
	}
	
	public String getProperty (String propertyName, Class<? extends Object> cls, String defaultValue) {
		return getProperty(cls.getName() + "." + propertyName, defaultValue);
	}
	
	public String getRequiredProperty (String propertyName)
			throws MissingResourceException {
		String propertyValue = getProperty(propertyName);
		if( null == propertyValue ) {
			logger.severe("unable to get required property " + propertyName);
			throw new MissingResourceException("unable to get required property " + propertyName, null, propertyName);
		}
		return propertyValue;
	}
	
	public String getRequiredProperty (String propertyName, Class<? extends Object> cls)
			throws MissingResourceException {
		String propertyValue = getProperty(propertyName, cls);
		if( null == propertyValue ) {
			logger.severe("unable to get required property " + cls.getName() + "." + propertyName);
			throw new MissingResourceException("unable to get required property " + cls.getName() + "." + propertyName,
					cls.getName(), propertyName);
		}
		return propertyValue;
	}

}
