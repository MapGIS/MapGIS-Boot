@echo off
echo.
echo [信息] 打包服务器产品
echo.

cd /d %~dp0

set ROOT_DIR_REV=%cd%

echo. | call %ROOT_DIR_REV%/package-win-x86_64.bat "" "" "%ROOT_DIR_REV%/../output-local"
echo. | call %ROOT_DIR_REV%/package-linux-x86_64.bat "" "SKIL_FRONT_BACK" "%ROOT_DIR_REV%/../output-local"

pause