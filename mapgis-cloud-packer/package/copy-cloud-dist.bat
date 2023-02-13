@echo off
echo.
echo [信息] 开始拷贝成果文件组织成不区分架构平台的产品包 
echo        参数1:[输出目录,默认为output-cloud]
echo.

cd /d %~dp0

setlocal
set CURRENT_DIR=%cd%
set BOOT_DIR=%CURRENT_DIR%/../../mapgis-boot

set OUT_DIR=%CURRENT_DIR%/../output-cloud
if "%~1" neq "" set "OUT_DIR=%~1"

if exist "%OUT_DIR%" rmdir /s/q "%OUT_DIR%"

mkdir "%OUT_DIR%"
mkdir "%OUT_DIR%/lib"
mkdir "%OUT_DIR%/bin"
mkdir "%OUT_DIR%/sql"

xcopy /y "%BOOT_DIR%\mapgis-cloud-module\mapgis-cloud-module-gateway\target\mapgis-cloud-module-gateway-*.jar" "%OUT_DIR%\lib\"
xcopy /y "%BOOT_DIR%\mapgis-cloud-module\mapgis-cloud-module-monitor\target\mapgis-cloud-module-monitor-*.jar" "%OUT_DIR%\lib\"
xcopy /y "%BOOT_DIR%\mapgis-module-auth\mapgis-module-auth-server\target\mapgis-module-auth-server-*.jar" "%OUT_DIR%\lib\"
xcopy /y "%BOOT_DIR%\mapgis-module-system\mapgis-module-system-server\target\mapgis-module-system-server-*.jar" "%OUT_DIR%\lib\"
xcopy /y "%BOOT_DIR%\mapgis-module-job\mapgis-module-job-server\target\mapgis-module-job-server-*.jar" "%OUT_DIR%\lib\"
xcopy /y "%BOOT_DIR%\mapgis-module-file\mapgis-module-file-server\target\mapgis-module-file-server-*.jar" "%OUT_DIR%\lib\"
xcopy /y "%BOOT_DIR%\mapgis-module-gen\mapgis-module-gen-server\target\mapgis-module-gen-server-*.jar" "%OUT_DIR%\lib\"
xcopy /y /e "%BOOT_DIR%\cloud-script\sql" "%OUT_DIR%\sql\"
xcopy /y /e "%CURRENT_DIR%\bin" "%OUT_DIR%\bin\"

endlocal

pause
exit /b 0