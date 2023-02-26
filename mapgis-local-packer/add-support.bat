@echo off
echo.
echo [信息] 添加服务器产品运行环境
echo.

cd /d %~dp0

setlocal

set CURRENT_DIR=%cd%

set RELEASE_DIR=%CURRENT_DIR%/release
set SUPPORT_DIR=\\192.168.17.59\03-Support

xcopy /y /e "%SUPPORT_DIR%" "%RELEASE_DIR%\"

endlocal