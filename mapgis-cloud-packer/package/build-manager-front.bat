@echo off
echo.
echo [信息] 开始构建服务器产品前端
echo.

cd /d %~dp0

setlocal

set CURRENT_DIR=%cd%
set MANAGER_FRONT_DIR=%CURRENT_DIR%/../../mapgis-manager
set MANAGER_DIST_DIR=%CURRENT_DIR%/../../mapgis-boot/mapgis-module-system/mapgis-module-system-biz/src/main/resources/static

if exist "%MANAGER_DIST_DIR%" rmdir /s/q "%MANAGER_DIST_DIR%"

mkdir "%MANAGER_DIST_DIR%"

cd %MANAGER_FRONT_DIR%

call yarn build:backend

xcopy /s /y /e "%MANAGER_FRONT_DIR%\dist" "%MANAGER_DIST_DIR%\"

endlocal

echo.
echo [信息] 完成构建服务器产品前端
echo.