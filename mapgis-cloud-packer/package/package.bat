@echo off
echo.
echo [信息] 打包服务器产品
echo.

cd /d %~dp0

set ROOT_DIR_REV=%cd%

call %ROOT_DIR_REV%/build.bat
call %ROOT_DIR_REV%/package-win-x86_64.bat
call %ROOT_DIR_REV%/package-linux-x86_64.bat

pause