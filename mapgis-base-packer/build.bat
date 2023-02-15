@echo off
rem �ļ�����Ϊgbk��utf-8ʱmaven����̨����
rem chcp 65001
echo.
echo [��Ϣ] ��ʼ������������Ʒ ����1:[maven profile����,Ĭ��local] ����2:[true �Ƿ�����ǰ�˱���]
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
        echo "ִ�г����쳣%errorlevel%�����˳�"
        pause
        exit /b %errorlevel%
    )
) else (
    echo ������ǰ�˱���
)

cd %BOOT_DIR%

call mvn clean package -Dmaven.test.skip=true -P %MVN_PROFILE%

endlocal

echo.
echo [��Ϣ] ��ɹ�����������Ʒ
echo.

cd /d %~dp0

pause
exit /b 0