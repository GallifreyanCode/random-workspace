@ECHO OFF
CLS
ECHO Installing Gallifreyan Tools
ECHO ...
GOTO install

:install
IF EXIST %HOMEPATH%\.gallifreyan-t RD /S /Q %HOMEPATH%\.gallifreyan-t
mkdir %HOMEPATH%\.gallifreyan-t
cd ../../
XCOPY gallifreyan-tools %HOMEPATH%\.gallifreyan-t /Y /E /Q
ECHO ...
GOTO check-install

:check-install
IF NOT EXIST %HOMEPATH%\.gallifreyan-t\bin\gallifreyan.bat GOTO install-failure
IF NOT EXIST %HOMEPATH%\.gallifreyan-t\scripts GOTO install-failure
ECHO Installation Complete
ECHO ...
GOTO set-environment

:set-environment
ECHO Creating Environment Variable
ECHO ...
for %%G in ("%path:;=" "%") do IF %%G == %GALLIFREYAN_HOME% GOTO environment-exists
SETX GALLIFREYAN_HOME "C:%HOMEPATH%\.gallifreyan-t"
SETX PATH "%GALLIFREYAN_HOME%\bin"
ECHO ...
GOTO check-environment

:environment-exists
ECHO Environment Exists
pause
GOTO check-environment

:check-environment
ECHO Installation complete
pause
GOTO:EOF

:install-failure
ECHO Installation failure
pause
GOTO:EOF

:environment-failure
ECHO Environment failure
pause
GOTO:EOF