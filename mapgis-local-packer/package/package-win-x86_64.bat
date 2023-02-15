@echo off
echo.
echo [信息] 打包服务器产品
echo.

cd /d %~dp0

set ROOT_DIR_REV=%cd%
set BASE_PACKER_DIR=%ROOT_DIR_REV%/../../mapgis-base-packer

set SKIP_FRONT=%~1

set COMPILE_FORNT_BACK=true
if "%~2" == "SKIP_FRONT_BACK" set COMPILE_FORNT_BACK=false

set OUTPUT_DIR="%ROOT_DIR_REV%/../outpt
if "%~3" neq "" set "OUTPUT_DIR=%~3"

if "%COMPILE_FORNT_BACK%" == "true" (
    echo. | call %BASE_PACKER_DIR%/build.bat "local" %SKIP_FRONT%
) else (
    echo 已跳过前端和后端编译
)

echo. | call %ROOT_DIR_REV%/copy-dist.bat "%OUTPUT_DIR%"

echo. | call %ROOT_DIR_REV%/package-common.bat win-x86_64 "%OUTPUT_DIR%"

pause