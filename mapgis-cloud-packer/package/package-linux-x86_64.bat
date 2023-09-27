@echo off
echo.
echo [��Ϣ] �����������Ʒ
echo.

cd /d %~dp0

set ROOT_DIR_REV=%cd%
set BASE_PACKER_DIR=%ROOT_DIR_REV%/../../mapgis-base-packer

set SKIP_FRONT=%~1
if "%SKIP_FRONT%" == "" set SKIP_FRONT=""

set COMPILE_FORNT_BACK=true
if "%~2" == "SKIP_FRONT_BACK" set COMPILE_FORNT_BACK=false

set OUTPUT_DIR=%ROOT_DIR_REV%/../output
if "%~3" neq "" set "OUTPUT_DIR=%~3"

if "%COMPILE_FORNT_BACK%" == "true" (
    echo. | call %BASE_PACKER_DIR%/build.bat "cloud" %SKIP_FRONT%
) else (
    echo ������ǰ�˺ͺ�˱���
)

rem ���Ƶ����Ŀ¼
echo. | call %ROOT_DIR_REV%/copy-dist.bat "%OUTPUT_DIR%"

rem ����ɷ��а�
echo. | call %ROOT_DIR_REV%/package-common.bat linux-x86_64 "%OUTPUT_DIR%"

pause