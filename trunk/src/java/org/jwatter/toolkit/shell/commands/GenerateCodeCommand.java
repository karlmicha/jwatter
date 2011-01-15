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
package org.jwatter.toolkit.shell.commands;

import org.jwatter.toolkit.generate.CodeGenerator;
import org.jwatter.toolkit.generate.CodeGeneratorException;
import org.jwatter.toolkit.shell.CommandExecutionException;
import org.jwatter.util.StringUtil;

public class GenerateCodeCommand extends AbstractCommand implements Command
{
	protected CodeGenerator codeGenerator;

	public GenerateCodeCommand ( CodeGenerator codeGenerator )
	{
		super(new String[]
		{
				"g", "generate"
		}, new String[]
		{
			"classname"
		}, new String[0], "generate code for the current page");
		this.codeGenerator = codeGenerator;
	}

	public void execute ( String... args ) throws CommandExecutionException
	{
		String classname = args[0];
		if ( !StringUtil.isValidClassName(classname) )
		{
			throw new CommandExecutionException(classname
					+ " is not a valid class name");
		}
		try
		{
			codeGenerator.generate(classname);
		}
		catch ( CodeGeneratorException e )
		{
			throw new CommandExecutionException(e);
		}
	}
}
