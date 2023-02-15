@echo off
rem 文件编码为gbk，utf-8时maven控制台乱码
rem chcp 65001
echo.
echo [信息] 开始构建服务器产品 参数1:[maven profile名称,默认local] 参数2:[true 是否跳过前端编译]
echo.

cd /d %~dp0

setlocal

set CURRENT_DIR=%cd%
set BOOT_DIR=%CURRENT_DIR%/../mapgis-boot

set MVN_PROFILE=local
if "%~1" neq "" set MVN_PROFILE=%~1
echo "MVN_PROFILE:%MVN_PROFILE%"

set COMPILE_FRONT=true
if "%~2" == "SKIP_FRONT" set COMPILE_FRONT=false

if "%COMPILE_FRONT%" == "true" (
    echo. | call %CURRENT_DIR%/build-manager-front.bat
    if %errorlevel% NEQ 0 (
        echo "执行出现异常%errorlevel%，将退出"
        pause
        exit /b %errorlevel%
    )
) else (
    echo 已跳过前端编译
)

cd %BOOT_DIR%

call mvn clean package -Dmaven.test.skip=true -P %MVN_PROFILE%

endlocal

echo.
echo [信息] 完成构建服务器产品
echo.

cd /d %~dp0

pause
exit /b 0