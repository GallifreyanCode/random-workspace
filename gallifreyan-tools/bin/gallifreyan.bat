@ECHO OFF
IF "%1"=="" GOTO activate
GOTO:%~1 2>NUL
ECHO Invalid argument: %1
GOTO:EOF

:activate
CLS
echo *********************************************************
PING -n 1 127.0.0.1>nul
echo *                   Gallifreyan Scripts                 *
PING -n 1 127.0.0.1>nul
echo *********************************************************
PING -n 1 127.0.0.1>nul
echo *                                                       *
echo * Installation: %GALLIFREYAN_HOME%
echo *                                                       *
PING -n 1 127.0.0.1>nul
echo *********************************************************
PING -n 1 127.0.0.1>nul
echo * -commands                                             *
PING -n 1 127.0.0.1>nul
echo *********************************************************
echo ...
PING -n 1 127.0.0.1>nul
COLOR 0a
GOTO:EOF

:-commands
echo list of commands:
echo * gradle
GOTO:EOF

:gradle
set /p groupId=groupId? 
set /p artifactId=artifactId? 
set /p version=version? 

mkdir %artifactId%
xcopy NewGradle.groovy %artifactId% /Y
echo %CD%\%artifactId%
cd %artifactId%
groovy %GALLIFREYAN_HOME%\scripts\NewGradle.groovy %groupId% %artifactId% %version%
GOTO Common

:2
REM Preprocess value 2

GOTO Common

:3
REM Preprocess value 3


:Common
REM Common processing of preprocessed values
REM End of batch file
GOTO:EOF
