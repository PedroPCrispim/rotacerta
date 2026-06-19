@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    http://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@REM ----------------------------------------------------------------------------
@REM Maven Start Up Batch script
@REM
@REM Required ENV vars:
@REM JAVA_HOME - location of a JDK home dir
@REM
@REM Optional ENV vars
@REM MAVEN_BATCH_ECHO - set to 'on' to enable the echoing of the batch commands
@REM MAVEN_BATCH_PAUSE - set to 'on' to wait for a key stroke before ending
@REM MAVEN_OPTS - parameters passed to the Java VM when running Maven
@REM     e.g. to debug Maven itself, use
@REM set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
@REM MAVEN_SKIP_RC - flag to disable loading of mavenrc files
@REM ----------------------------------------------------------------------------

@IF "%MAVEN_BATCH_ECHO%" == "on"  echo %MAVEN_BATCH_ECHO%

@setlocal

set ERROR_CODE=0

@REM To isolate internal variables from possible side effects of the calling environment, drop the prefixes
@set "MAVEN_PROJECTBASEDIR="
@set "MAVEN_ARG_LINE="

@REM ==== START VALIDATION ====
@set "MAVEN_PROJECTBASEDIR=%~dp0"
@IF NOT "%MAVEN_PROJECTBASEDIR:~-1%" == "\" SET "MAVEN_PROJECTBASEDIR=%MAVEN_PROJECTBASEDIR%\"

@set "MAVEN_WRAPPER_JAR=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.jar"
@set "MAVEN_WRAPPER_PROPERTIES=%MAVEN_PROJECTBASEDIR%.mvn\wrapper\maven-wrapper.properties"

@REM Find the wrapper jar
@IF EXIST "%MAVEN_WRAPPER_JAR%" goto :foundWrapperJar

@REM Download the wrapper jar
@set "MAVEN_WRAPPER_URL=https://repo.maven.apache.org/maven2/org/apache/maven/wrapper/maven-wrapper/3.3.2/maven-wrapper-3.3.2.jar"
@echo Downloading Maven Wrapper from %MAVEN_WRAPPER_URL%...

@powershell -Command "&{"^
		"$webclient = New-Object System.Net.WebClient;"^
		"if (-not $webclient.Proxy.IsBypassed('https://repo.maven.apache.org')) {"^
		"  $webclient.Proxy = [System.Net.WebRequest]::GetSystemWebProxy();"^
		"  $webclient.Proxy.Credentials = [System.Net.CredentialCache]::DefaultCredentials;"^
		"}"^
		"$webclient.DownloadFile('%MAVEN_WRAPPER_URL%', '%MAVEN_WRAPPER_JAR%');"^
		"}"
@IF ERRORLEVEL 1 goto :error

:foundWrapperJar
@set "MAVEN_COMMAND=mvn"
@set "MAVEN_ARGS="

@REM Check for Java
@if not "%JAVA_HOME%" == "" goto :foundJava
@set "JAVA_EXE=java.exe"
%JAVA_EXE% -version >NUL 2>&1
@if %ERRORLEVEL% == 0 goto :run
@echo Error: JAVA_HOME is not defined and 'java.exe' is not in your PATH. >&2
@goto :error

:foundJava
@set "JAVA_EXE=%JAVA_HOME%\bin\java.exe"

:run
@set "MAVEN_OPTS=%MAVEN_OPTS% -Dmaven.multiModuleProjectDirectory=%MAVEN_PROJECTBASEDIR%"
@set "WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain"

"%JAVA_EXE%" %MAVEN_OPTS% -classpath "%MAVEN_WRAPPER_JAR%" %WRAPPER_LAUNCHER% %*
@if ERRORLEVEL 1 goto :error
@goto :end

:error
@set ERROR_CODE=1

:end
@exit /B %ERROR_CODE%
