@echo off
echo.
echo [信息] 开始构建服务器产品前端
echo.

cd /d %~dp0

setlocal

set CURRENT_DIR=%cd%
set MANAGER_FRONT_DIR=%CURRENT_DIR%/../../mapgis-manager

cd %MANAGER_FRONT_DIR%

call yarn build

endlocal

echo.
echo [信息] 完成构建服务器产品前端
echo.