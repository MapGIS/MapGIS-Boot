@echo off
rem 文件编码为gbk，utf-8时maven控制台乱码
rem chcp 65001
echo.
echo [信息] 开始构建服务器产品
echo.

cd /d %~dp0
cd ../

setlocal

set CURRENT_DIR=%cd%
set BOOT_DIR=%CURRENT_DIR%/../mapgis-boot

set MVN_PROFILE=cloud
if "%~1" neq "" set MVN_PROFILE=%~1
echo "MVN_PROFILE:%MVN_PROFILE%"

set OUT_DIR=%CURRENT_DIR%/output
if "%~2" neq "" set "OUT_DIR=%CURRENT_DIR%/%~2"

if exist "%OUT_DIR%" rmdir /s/q "%OUT_DIR%"

set COMPILE_FRONT=true
if "%~3" == "SKIL_FRONT" set COMPILE_FRONT=false

if "%COMPILE_FRONT%" == "true" (
    echo. | call %CURRENT_DIR%/package/build-manager-front.bat
    if %errorlevel% NEQ 0 (
        echo "执行出现异常%errorlevel%，将退出"
        pause
        exit /b %errorlevel%
    )
) else (
    echo 已跳过前端编译
)

cd %BOOT_DIR%

call mvn clean package -pl ^
com.zondy.mapgis:mapgis-cloud-module-gateway,^
com.zondy.mapgis:mapgis-cloud-module-monitor,^
com.zondy.mapgis:mapgis-module-auth-server,^
com.zondy.mapgis:mapgis-module-system-server,^
com.zondy.mapgis:mapgis-module-job-server,^
com.zondy.mapgis:mapgis-module-file-server^
 -am -Dmaven.test.skip=true -P %MVN_PROFILE%

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
xcopy /y /e "%BOOT_DIR%\cloud-script\sql" "%OUT_DIR%\sql\"
xcopy /y /e "%CURRENT_DIR%\package\bin" "%OUT_DIR%\bin\"

endlocal

echo.
echo [信息] 完成构建服务器产品
echo.

cd /d %~dp0