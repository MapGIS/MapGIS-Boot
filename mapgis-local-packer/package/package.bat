@echo off
echo.
echo [信息] 打包服务器产品
echo.

cd /d %~dp0

set ROOT_DIR_REV=%cd%
set BASE_PACKER_DIR=%ROOT_DIR_REV%/../../common-packer

echo. | call %BASE_PACKER_DIR%/build.bat "local"
echo. | call %ROOT_DIR_REV%/copy-local-dist.bat "%ROOT_DIR_REV%/../output-local"

echo. | call %ROOT_DIR_REV%/package-common.bat win-x86_64 "output-local"
echo. | call %ROOT_DIR_REV%/package-common.bat linux-x86_64 "output-local"

pause