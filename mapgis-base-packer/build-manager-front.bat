@echo off
echo.
echo [��Ϣ] ��ʼ������������Ʒǰ��
echo.

cd /d %~dp0

setlocal

set CURRENT_DIR=%cd%
set MANAGER_FRONT_DIR=%CURRENT_DIR%/../mapgis-manager
set MANAGER_DIST_DIR=%CURRENT_DIR%/../mapgis-boot/mapgis-module-system/mapgis-module-system-biz/src/main/resources/static/manager-ui

if exist "%MANAGER_DIST_DIR%" rmdir /s/q "%MANAGER_DIST_DIR%"

mkdir "%MANAGER_DIST_DIR%"

cd %MANAGER_FRONT_DIR%

call yarn install
call yarn build:backend

xcopy /s /y /e "%MANAGER_FRONT_DIR%\dist\*" "%MANAGER_DIST_DIR%\"

endlocal

echo.
echo [��Ϣ] ��ɹ�����������Ʒǰ��
echo.

pause
exit /b 0