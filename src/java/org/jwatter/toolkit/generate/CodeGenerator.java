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

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.jwatter.browser.WebAutomationFramework;
import org.jwatter.html.Element;
import org.jwatter.model.PageImpl;
import org.jwatter.test.WebFunctionalTestCase;
import org.jwatter.toolkit.control.BrowserController;
import org.jwatter.toolkit.control.BrowserControllerException;
import org.jwatter.toolkit.control.request.BrowserInfo;
import org.jwatter.toolkit.control.request.GetBrowserInfoRequest;
import org.jwatter.toolkit.control.request.GetHtmlElementsRequest;
import org.jwatter.toolkit.generate.code.Assignment;
import org.jwatter.toolkit.generate.code.ClassDefinition;
import org.jwatter.toolkit.generate.code.CodeFactory;
import org.jwatter.toolkit.generate.code.MethodBuilder;
import org.jwatter.toolkit.generate.code.MethodCall;
import org.jwatter.toolkit.generate.code.MethodDefinition;
import org.jwatter.toolkit.generate.code.ObjectCreation;
import org.jwatter.toolkit.generate.code.PropertiesFile;
import org.jwatter.toolkit.generate.code.ReturnStatement;
import org.jwatter.toolkit.generate.code.SourceFile;
import org.jwatter.toolkit.shell.UserCommunicationManager;
import org.jwatter.util.OrderedProperties;
import org.jwatter.util.ReflectUtil;
import org.jwatter.util.StringUtil;

public class CodeGenerator
{
	// protected field in Page class
	protected static final String BROWSER_INSTANCE_NAME = "browserFramework";

	// truncate element text in method Javadoc
	protected static final int TRUNCATE_ELEMENT_TEXT_IN_JAVADOC = 40;

	protected static final String pageSubpackageName = "pages";
	protected static final String actorSubpackageName = "pages.actor";
	protected static final String testSubpackageName = "tests";

	protected BrowserController browserController;
	protected UserCommunicationManager confirmManager;
	protected BrowserInfo browserInfo;
	protected PropertiesFile actorPropertiesFile;
	protected String sourceDirectory;
	protected String basePackageName;
	protected String pagePackageName;
	protected String actorPackageName;
	protected String testPackageName;
	protected String actorClassName;
	protected String pageClassName;
	protected String pageTestClassName;
	protected SourceFile actorClassFile;
	protected SourceFile pageClassFile;
	protected SourceFile pageTestClassFile;
	protected ClassDefinition actorClassDefinition;
	protected ClassDefinition pageClassDefinition;
	protected ClassDefinition pageTestClassDefinition;
	protected OrderedProperties actorProperties;
	protected Set<String> actorMethodUids;

	public CodeGenerator ( BrowserController browsercontroller,
			UserCommunicationManager confirmmanager, BrowserInfo browserinfo,
			PropertiesFile actorpropertiesfile, String sourcedirectory,
			String basepackagename )
	{
		this.browserController = browsercontroller;
		this.confirmManager = confirmmanager;
		this.browserInfo = browserinfo;
		this.actorPropertiesFile = actorpropertiesfile;
		this.sourceDirectory = sourcedirectory;
		this.basePackageName = basepackagename;
		pagePackageName = basePackageName + "." + pageSubpackageName;
		actorPackageName = basePackageName + "." + actorSubpackageName;
		testPackageName = basePackageName + "." + testSubpackageName;
	}

	public void generate ( String pageclassname ) throws CodeGeneratorException
	{
		this.pageClassName = pageclassname;
		this.actorClassName = pageclassname + "Actor";
		this.pageTestClassName = pageclassname + "Test";
		getBrowserInfo();

		try
		{
			// create new actor class file
			actorClassFile =
					SourceFile.createNewSourceFile(actorPackageName,
							actorClassName);

			// check if class file already exists
			String actorclassfilename =
					actorClassFile.getFilename(sourceDirectory);
			boolean createactorclassfile = true;
			if ( new File(actorclassfilename).exists() )
			{
				if ( !confirmManager.confirm(actorClassName
						+ " already exists. Do you want to overwrite it?") )
				{
					createactorclassfile = false;
				}
			}
			if ( createactorclassfile )
			{
				// create actor class
				System.out.print("Generating actor class...");
				generateActorClass();
				System.out.println();
				System.out.println("Generated " + actorMethodUids.size()
						+ " actor methods");
				System.out.println("Writing properties to "
						+ actorPropertiesFile.getFilename());
				actorPropertiesFile.replacePropertiesForClass(actorProperties,
						actorClassDefinition.getName());
				System.out.println("Writing actor class to "
						+ actorclassfilename);
				actorClassFile.save(sourceDirectory);
			}

			// create page class (only if it does not exist already)
			pageClassFile =
					SourceFile.createNewSourceFile(pagePackageName,
							pageClassName);
			String pageclassfilename =
					pageClassFile.getFilename(sourceDirectory);
			if ( !new File(pageclassfilename).exists() )
			{
				System.out
						.println("Writing page class to " + pageclassfilename);
				generatePageClass();
				pageClassFile.save(sourceDirectory);
			}

			// create page test class (only if it does not exist already)
			pageTestClassFile =
					SourceFile.createNewSourceFile(testPackageName,
							pageTestClassName);
			String pagetestclassfilename =
					pageTestClassFile.getFilename(sourceDirectory);
			if ( !new File(pagetestclassfilename).exists() )
			{
				System.out.println("Writing test class to "
						+ pagetestclassfilename);
				generatePageTestClass();
				pageTestClassFile.save(sourceDirectory);
			}
		}
		catch ( IOException e )
		{
			throw new CodeGeneratorException(e);
		}
	}

	protected void generateActorClass () throws CodeGeneratorException
	{
		actorClassDefinition =
				CodeFactory.createClassDefinition(Modifier.PUBLIC,
						actorClassName, PageImpl.class);
		actorClassFile.addClassDefinition(actorClassDefinition);

		// object to collect all properties for this actor class
		actorProperties = new OrderedProperties();

		// url path for this page
		actorProperties.put(actorClassFile.getClassName() + ".url", browserInfo
				.getPath());

		// add comments
		actorClassFile.addComment("Autogenerated " + new Date().toString());
		actorClassFile.addComment("Page class: " + pageClassName);
		actorClassFile.addComment("Actor class: " + actorClassName);
		actorClassFile.addComment("URL: " + browserInfo.getUrl());
		actorClassFile.addComment("Browser version: "
				+ browserInfo.getBrowserVersion());

		// imports
		actorClassFile.addImport(WebAutomationFramework.class);
		actorClassFile.addImport(PageImpl.class);

		// constructor
		MethodBuilder constructorbuilder =
				actorClassDefinition.addConstructor(Modifier.PUBLIC,
						new Class<?>[]
						{
							WebAutomationFramework.class
						}, new String[]
						{
							BROWSER_INSTANCE_NAME
						}, new Class<?>[]
						{
							Exception.class
						});
		constructorbuilder.addSuperConstructorCall(BROWSER_INSTANCE_NAME,
				actorClassName + ".class");

		// add actor methods for all elements that have actor methods
		actorMethodUids = new HashSet<String>();
		for ( String elementname : ElementActorBroker.getSupportedElements() )
		{
			ArrayList<Element> elements = getHtmlElements(elementname);
			for ( int elementindex = 1 ; elementindex <= elements.size() ; elementindex++ )
			{
				Element element = elements.get(elementindex - 1);
				for ( ElementActorMethod method : ElementActorBroker
						.getElementActorMethods(element) )
				{
					// All actor methods in the same actor class must have
					// unique ids. A new actor method will not be added to the
					// class definition if another actor method with the same
					// uid already exists.
					// However, this does not preclude name clashes between
					// actor methods with different uids. If another actor
					// method with the same name (but different uid) already
					// exists, we append a number to the name of the new method
					// to make it unique.
					String actormethoduid =
							method.getUniqueIdForElement(element);
					if ( !actorMethodUids.contains(actormethoduid) )
					{
						actorMethodUids.add(actormethoduid);
						generateActorMethodDefinitionForElement(method,
								element, elementindex);
					}
				}
			}
		}
	}

	protected void generateActorMethodDefinitionForElement (
			ElementActorMethod method, Element element, int elementindex )
			throws CodeGeneratorException
	{
		Class<?> returntype = method.getReturnType();
		String methodname = method.getNameForElement(element);
		Class<?>[] parametertypes = method.getParameterTypes();
		String[] parameternames = method.getParameterNames();
		Class<?>[] exceptiontypes = method.getExceptionTypes();
		Method interfacemethod = method.getInterfaceMethod();

		// for Javadoc
		String elementtext =
				element.getText(TRUNCATE_ELEMENT_TEXT_IN_JAVADOC, true);
		String description =
				"Calls "
						+ ReflectUtil.getSignature(interfacemethod)
						+ " for "
						+ element.toString()
						+ (elementtext.length() > 0 ? " \"" + elementtext
								+ "\"" : "");
		String returndoc =
				returntype != void.class ? "the value of "
						+ interfacemethod.getName() : null;
		String[] parameterdoc = new String[parameternames.length];
		for ( int i = 0 ; i < parameternames.length ; i++ )
		{
			parameterdoc[i] =
					"the " + parameternames[i] + " parameter of "
							+ interfacemethod.getName();
		}
		String[] exceptiondoc = new String[exceptiontypes.length];
		for ( int i = 0 ; i < exceptiontypes.length ; i++ )
		{
			exceptiondoc[i] =
					"if " + interfacemethod.getName() + " throws "
							+ exceptiontypes[i].getSimpleName();
		}

		// arguments for the call to the interface method:
		// - attribute and element content parameters are passed field names
		// - other parameters are passed actor method parameters
		String[] interfacemethodarguments =
				new String[interfacemethod.getParameterTypes().length];
		for ( int interfaceparameterindex = 0 ; interfaceparameterindex < interfacemethodarguments.length ; interfaceparameterindex++ )
		{
			if ( method.isHtmlAttributeParameter(interfaceparameterindex) )
			{
				// create property and field for attribute
				String attributename =
						method
								.getHtmlAttributeNameForParameter(interfaceparameterindex);
				interfacemethodarguments[interfaceparameterindex] =
						generateAttributePropertyField(element, elementindex,
								attributename);
			}
			else if ( method
					.isHtmlElementContentParameter(interfaceparameterindex) )
			{
				// create property and field for content
				interfacemethodarguments[interfaceparameterindex] =
						generateElementContentPropertyField(element,
								elementindex);
			}
			else
			{
				interfacemethodarguments[interfaceparameterindex] =
						parameternames[method
								.getParameterIndex(interfaceparameterindex)];
			}
		}

		// build actor method
		MethodBuilder actormethodbuilder =
				CodeFactory.createNewMethodDefinition(Modifier.PROTECTED,
						returntype, methodname, parametertypes, parameternames,
						exceptiontypes);

		// add call to interface method
		if ( returntype == void.class )
		{
			actormethodbuilder.addMethodCall(BROWSER_INSTANCE_NAME,
					interfacemethod, interfacemethodarguments);
		}
		else
		{
			actormethodbuilder.addStatement(new ReturnStatement(new MethodCall(
					BROWSER_INSTANCE_NAME, method.getInterfaceMethod(),
					interfacemethodarguments)));
		}

		// add method documentation
		actormethodbuilder.addJavadoc(description, parameterdoc, returndoc,
				exceptiondoc);

		// obtain complete method definition
		MethodDefinition methoddefinition =
				actormethodbuilder.getMethodDefinition();

		// make sure method does not exist already
		int count = 1;
		while ( actorClassDefinition.containsMethod(methoddefinition) )
		{
			methoddefinition.setName(methodname + (++count));
		}

		// add method to actor class
		actorClassDefinition.addMethodDefinition(methoddefinition);

		// add imports for the method's exception types
		for ( Class<?> exceptionclass : exceptiontypes )
		{
			if ( exceptionclass.getPackage() != Object.class.getPackage() )
			{
				actorClassFile.addImport(exceptionclass);
			}
		}
	}

	protected String generateAttributePropertyField ( Element element,
			int elementindex, String attribute ) throws CodeGeneratorException
	{
		String fieldname =
				element.getName() + elementindex
						+ StringUtil.titleCase(attribute);
		if ( actorClassDefinition.hasFieldDeclaration(fieldname) ) return fieldname;

		String propertyname =
				element.getName() + elementindex + "." + attribute;
		String propertypath =
				actorClassFile.getClassName() + "." + propertyname;
		String propertyvalue = element.getAttributeValue(attribute);
		actorProperties.put(propertypath, propertyvalue);
		generateActorFieldDeclaration(fieldname, propertyname);
		return fieldname;
	}

	protected String generateElementContentPropertyField ( Element element,
			int elementindex ) throws CodeGeneratorException
	{
		String fieldname = element.getName() + elementindex + "Content";
		if ( actorClassDefinition.hasFieldDeclaration(fieldname) ) return fieldname;

		String propertyname = element.getName() + elementindex + ".content";
		String propertypath =
				actorClassFile.getClassName() + "." + propertyname;
		String propertyvalue = element.getText().trim();
		actorProperties.put(propertypath, propertyvalue);
		generateActorFieldDeclaration(fieldname, propertyname);
		return fieldname;
	}

	protected void generateActorFieldDeclaration ( String fieldname,
			String propertyname )
	{
		actorClassDefinition.addFieldDeclaration(CodeFactory
				.createFieldDeclaration(Modifier.PROTECTED | Modifier.STATIC
						| Modifier.FINAL, String.class, fieldname,
						"getRequiredProperty(\"" + propertyname + "\", "
								+ actorClassName + ".class)"));
	}

	protected void generatePageClass ()
	{
		pageClassDefinition =
				CodeFactory.createClassDefinition(Modifier.PUBLIC,
						pageClassName, actorClassName);
		pageClassFile.addClassDefinition(pageClassDefinition);
		pageClassFile.addComment("Autogenerated stub " + new Date().toString());
		pageClassFile.addComment("Page class: " + pageClassName);
		pageClassFile.addComment("Actor class: " + actorClassName);
		pageClassFile.addComment("URL: " + browserInfo.getUrl());
		pageClassFile.addImport(actorPackageName, actorClassName);
		pageClassFile.addImport(WebAutomationFramework.class);
		MethodBuilder constructorbuilder =
				pageClassDefinition.addConstructor(Modifier.PUBLIC,
						new Class<?>[]
						{
							WebAutomationFramework.class
						}, new String[]
						{
							"browserFramework"
						}, new Class<?>[]
						{
							Exception.class
						});
		constructorbuilder.addSuperConstructorCall("browserFramework");
	}

	protected void generatePageTestClass ()
	{
		pageTestClassDefinition =
				CodeFactory.createClassDefinition(Modifier.PUBLIC,
						pageTestClassName, WebFunctionalTestCase.class);
		pageTestClassFile.addClassDefinition(pageTestClassDefinition);
		pageTestClassFile.addComment("Autogenerated " + new Date().toString());
		pageTestClassFile.addComment("Test class: " + pageTestClassName);
		pageTestClassFile.addComment("Page class: " + pageClassName);
		pageTestClassFile.addComment("URL: " + browserInfo.getUrl());
		pageTestClassFile.addImport(WebFunctionalTestCase.class);
		pageTestClassFile.addImport(pagePackageName, pageClassName);

		// field to store page object
		String pagefieldname = StringUtil.firstLowercase(pageClassName);
		pageTestClassDefinition.addFieldDeclaration(CodeFactory
				.createFieldDeclaration(Modifier.PROTECTED, pageClassName,
						pagefieldname));

		// constructor to construct page object
		MethodBuilder constructorBuilder =
				pageTestClassDefinition.addConstructor(Modifier.PUBLIC, null,
						null, new Class<?>[]
						{
							Exception.class
						});
		constructorBuilder
				.addStatement(new Assignment(pagefieldname, new ObjectCreation(
						pageClassName, getBrowserInstanceFieldname())));
	}

	protected void getBrowserInfo () throws CodeGeneratorException
	{
		try
		{
			browserController.send(new GetBrowserInfoRequest(browserInfo));
		}
		catch ( BrowserControllerException e )
		{
			throw new CodeGeneratorException(e);
		}
	}

	protected ArrayList<Element> getHtmlElements ( String elementname )
			throws CodeGeneratorException
	{
		try
		{
			ArrayList<Element> elements = new ArrayList<Element>();
			browserController.send(new GetHtmlElementsRequest(elementname,
					elements));
			return elements;
		}
		catch ( BrowserControllerException e )
		{
			throw new CodeGeneratorException(e);
		}
	}

	protected static String getBrowserInstanceFieldname ()
	{
		for ( Field field : WebFunctionalTestCase.class.getDeclaredFields() )
		{
			if ( field.getType() == WebAutomationFramework.class )
			{
				return field.getName();
			}
		}
		return null;
	}

}
