@echo off
echo.
echo [��Ϣ] �����������Ʒ
echo.

cd /d %~dp0

set ROOT_DIR_REV=%cd%

echo. | call %ROOT_DIR_REV%/package-win-x86_64.bat "" "" "%ROOT_DIR_REV%/../output-cloud"
echo. | call %ROOT_DIR_REV%/package-linux-x86_64.bat "" "SKIL_FRONT_BACK" "%ROOT_DIR_REV%/../output-cloud"

pause