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
package org.jwatter.toolkit.generate.code;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.jwatter.util.FileUtil;
import org.jwatter.util.OrderedProperties;

public class PropertiesFile extends OrderedProperties
{
	private static final long serialVersionUID = 1L;
	protected String propertiesFilename;

	public PropertiesFile ( String filename ) throws IOException
	{
		super();
		propertiesFilename = filename;
		tryLoad(filename);
	}

	public void tryLoad ( String filename ) throws IOException
	{
		InputStream file = null;
		try
		{
			file = new FileInputStream(filename);
			load(file);
		}
		catch ( FileNotFoundException e )
		{
		}
		finally
		{
			if ( file != null )
			{
				file.close();
			}
		}
	}

	public void replacePropertiesForClass ( OrderedProperties properties,
			String className ) throws IOException
	{
		deleteClassProperties(className);
		putAll(properties);
		save(propertiesFilename);
	}

	public void removePropertiesForClass ( String className )
			throws IOException
	{
		deleteClassProperties(className);
		save(propertiesFilename);
	}

	protected void deleteClassProperties ( String className )
	{
		for ( Iterator<Object> propertyIterator = this.keySet().iterator() ; propertyIterator
				.hasNext() ; )
		{
			if ( ((String) propertyIterator.next()).startsWith(className + ".") )
			{
				propertyIterator.remove();
			}
		}
	}

	protected void save ( String filename ) throws IOException
	{
		File f = new File(filename);

		// make sure parent directory exists
		FileUtil.ensureDirectoryExists(f.getParent());

		try
		{
			OutputStream file = new FileOutputStream(filename);
			store(file, "Autogenerated");
			file.close();
		}
		catch ( FileNotFoundException e )
		{
			// produce more informed error message
			String reason = e.getMessage();
			if ( f.isDirectory() )
			{
				reason = "is a directory";
			}
			throw new FileNotFoundException(filename + "( " + reason + ")");
		}
	}

	public String getFilename ()
	{
		return propertiesFilename;
	}
}