@echo off
echo.
echo [��Ϣ] ��ʼ�����ɹ��ļ���֯�ɲ����ּܹ�ƽ̨�Ĳ�Ʒ�� 
echo        ����1:[���Ŀ¼,Ĭ��Ϊoutput]
echo.

cd /d %~dp0

setlocal
set CURRENT_DIR=%cd%
set BOOT_DIR=%CURRENT_DIR%/../../mapgis-boot

set OUT_DIR=%CURRENT_DIR%/../output
if "%~1" neq "" set "OUT_DIR=%~1"

if exist "%OUT_DIR%" rmdir /s/q "%OUT_DIR%"

mkdir "%OUT_DIR%"
mkdir "%OUT_DIR%/config"
mkdir "%OUT_DIR%/lib"
mkdir "%OUT_DIR%/bin"

xcopy /y "%BOOT_DIR%\mapgis-server\target\mapgis-server-*.jar" "%OUT_DIR%\lib\"
xcopy /y "%BOOT_DIR%\local-script\config\application.example.properties" "%OUT_DIR%\config\"
xcopy /y /e "%CURRENT_DIR%\bin" "%OUT_DIR%\bin\"

endlocal

pause
exit /b 0