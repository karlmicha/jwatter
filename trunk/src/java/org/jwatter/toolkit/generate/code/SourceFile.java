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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jwatter.util.FileUtil;
import org.jwatter.util.StringUtil;

public class SourceFile
{
	protected String packageName;
	protected String className;
	protected List<Comment> comments;
	protected Set<String> imports;
	protected ClassDefinition classDefinition;

	protected SourceFile ( String packagename, String classname )
	{
		this.packageName = packagename;
		this.className = classname;
		comments = new ArrayList<Comment>();
		imports = new HashSet<String>();
		classDefinition = null;
	}

	public static SourceFile createNewSourceFile ( String packagename,
			String filename )
	{
		return new SourceFile(packagename, filename);
	}

	public static SourceFile createNewSourceFile ( Package classpackage,
			String filename )
	{
		return createNewSourceFile(classpackage.getName(), filename);
	}

	public void addComment ( String comment )
	{
		comments.add(new Comment(comment));
	}

	public void addImport ( String packagename )
	{
		addImport(packagename, "*");
	}

	public void addImport ( String packagename, String classname )
	{
		imports.add(packagename + "." + classname);
	}

	public void addImport ( Class<?> importedclass )
	{
		addImport(importedclass.getPackage().getName(), importedclass
				.getSimpleName());
	}

	public void addImport ( Package importedpackage )
	{
		addImport(importedpackage.getName());
	}

	public void addImport ( Package importedpackage, String classname )
	{
		addImport(importedpackage.getName(), classname);
	}

	public void addClassDefinition ( ClassDefinition classdefinition )
			throws MalformedCodeError
	{
		if ( this.classDefinition != null )
		{
			throw new MalformedCodeError(
					"attempt to add another class definition ("
							+ classdefinition.getName() + ") to "
							+ getClassName());
		}
		this.classDefinition = classdefinition;
	}

	public String getPackageName ()
	{
		return packageName;
	}

	public String getClassName ()
	{
		return packageName + "." + className;
	}

	public String getSimpleClassName ()
	{
		return className;
	}

	public String getPackageDir ()
	{
		return getPackageDir(null);
	}

	public String getPackageDir ( String sourcedirectory )
	{
		return (sourcedirectory != null ? sourcedirectory + "/" : "")
				+ packageName.replaceAll("\\.", "/");
	}

	public String getFilename ()
	{
		return getFilename(null);
	}

	public String getFilename ( String sourcedirectory )
	{
		return getPackageDir(sourcedirectory) + "/" + className + ".java";
	}

	public void write ( OutputStream out ) throws IOException
	{
		write(new PrintWriter(new OutputStreamWriter(out, "UTF-8")));
	}

	public void write ( PrintWriter writer ) throws IOException
	{
		for ( Comment comment : comments )
		{
			writer.println(comment.format());
		}

		writer.println("package " + packageName + ";");
		writer.println();

		List<String> importList = new ArrayList<String>();
		importList.addAll(imports);
		Collections.sort(importList);
		for ( String importClass : importList )
		{
			writer.println("import " + importClass + ";");
		}

		writer.println();
		if ( classDefinition != null )
		{
			writer.print(classDefinition.format());
		}

		if ( writer.checkError() )
		{
			throw new IOException(
					"unknown I/O error while writing the source file for "
							+ getClassName());
		}
	}

	public void save ( String sourcedirectory ) throws IOException
	{
		FileUtil.ensureDirectoryExists(getPackageDir(sourcedirectory));
		OutputStream classFile =
				new FileOutputStream(getFilename(sourcedirectory));
		write(classFile);
		classFile.close();
	}

	protected String getEncoding ()
	{
		if ( StringUtil.containsNonLatin1Characters(packageName) ) return "UTF-8";
		if ( StringUtil.containsNonLatin1Characters(className) ) return "UTF-8";
		for ( String importclass : imports )
		{
			if ( StringUtil.containsNonLatin1Characters(importclass) ) return "UTF-8";
		}
		for ( Comment comment : comments )
		{
			String enc = comment.getEncoding();
			if ( enc.equals("UTF-8") ) return enc;
		}
		return classDefinition.getEncoding();
	}

}
