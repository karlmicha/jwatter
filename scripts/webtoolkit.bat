@ECHO OFF
REM Copyright 2011 Karl-Michael Schneider
REM 
REM Licensed under the Apache License, Version 2.0 (the "License");
REM you may not use this file except in compliance with the License.
REM You may obtain a copy of the License at
REM 
REM     http://www.apache.org/licenses/LICENSE-2.0
REM 
REM Unless required by applicable law or agreed to in writing, software
REM distributed under the License is distributed on an "AS IS" BASIS,
REM WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
REM See the License for the specific language governing permissions and
REM limitations under the License.

SET JAVAHOME=%PROGRAMFILES%\Java\jdk1.5.0_11
SET JAVALIB=%JAVAHOME%\lib
SET JAVA=%JAVAHOME%\bin\java.exe
SET CLASSDIR=build\classes
SET JARDIR=classes

SET PROPERTIESFILE=resources\toolkit.properties

REM Uncomment the following line on Windows systems with slow filesystem
REM SET FIREFOXWRAPPER=-Dwebdriver.firefox.bin=scripts\webdriver_firefox_wrapper.bat

SET CLASSPATH=%JAVALIB%
SET CLASSPATH=%CLASSPATH%;%CLASSDIR%
SET CLASSPATH=%CLASSPATH%;%JARDIR%\selenium\selenium-java-2.0a2.jar
SET CLASSPATH=%CLASSPATH%;%JARDIR%\selenium\json-20080701.jar

"%JAVA%" -Xmx512m -cp "%CLASSPATH%" %FIREFOXWRAPPER% -Dtoolkit.properties=%PROPERTIESFILE% org.jwatter.toolkit.Toolkit %1
