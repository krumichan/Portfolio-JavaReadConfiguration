set BATCH_PATH=%~PD0
set BATCH_PATH_SLASH=%BATCH_PATH:\=/%
set CONF_PATH=%BATCH_PATH_SLASH:~0,-1%

cmd /k java^
 -Xdebug^
 -Xrunjdwp:transport=dt_socket,server=y,address=42201,suspend=y^
 -jar ./ReadConfiguration.jar^
 %CONF_PATH%/configuration.conf