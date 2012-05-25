@ECHO OFF
SET JAVA_HOME=%CD%\jigsaw-jdk
SET PATH=%JAVA_HOME%/bin;%path%

ECHO Step 1: Compile Jigsaw modules.
ECHO *********************************
ECHO		javac -d classes -modulepath classes -sourcepath src ...java...files
PAUSE
javac -d classes -modulepath classes -sourcepath src src/be.a/module-info.java src/be.a/be/a/Hello.java src/be.b/module-info.java src/be.b/be/b/Printer.java
ECHO Done.
ECHO *********************************

ECHO Step 2: Create a module library.
ECHO *********************************
ECHO		jmod create -L mlib
ECHO *********************************
PAUSE
jmod create -L mlib
ECHO Done.
ECHO *********************************

ECHO Step 3: Install modules in library.
ECHO *********************************
ECHO		jmod install -L mlib classes be.b
ECHO		jmod install -L mlib classes be.a
ECHO *********************************
PAUSE
jmod install -L mlib classes be.b
jmod install -L mlib classes be.a
ECHO Done.
ECHO *********************************

ECHO Step 4: Check installed modules in library.
ECHO *********************************
ECHO		jmod -L mlib list
ECHO *********************************
PAUSE
jmod -L mlib list -v
ECHO Done.
ECHO *********************************

ECHO Step 5: Start the module.
ECHO *********************************
ECHO		java -L mlib -m be.a
ECHO *********************************
PAUSE
java -L mlib -m be.a
ECHO Done.
ECHO *********************************

ECHO Step 6: Create module package.
ECHO *********************************
ECHO		jpkg -m classes/be.a jmod be.a
ECHO		jpkg -m classes/be.b jmod be.b
ECHO *********************************
PAUSE
jpkg -m classes/be.a jmod be.a
jpkg -m classes/be.b jmod be.b
ECHO Done.

:cleanup
RD /S /Q mlib
RD /S /Q classes
mkdir classes
pause