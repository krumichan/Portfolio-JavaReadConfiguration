set BATCH_PATH=%~PD0
set BATCH_PATH_SLASH=%BATCH_PATH:\=/%
set CONF_PATH=%BATCH_PATH_SLASH:~0,-1%

cmd /k java^
 -jar ./ReadConfiguration.jar^
 %CONF_PATH%/configuration.conf