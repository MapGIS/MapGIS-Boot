@echo off
echo.
echo [info] package  mapgis-boot
echo.

cd /d %~dp0
cd ../

setlocal

set CURRENT_DIR=%cd%
set DIST_DIR_WEB_APPS_WIN_X86_64="%CURRENT_DIR%\release\win-x86_64"
set DIST_DIR_WEB_APPS_LINUX_X86_64="%CURRENT_DIR%\release\linux-x86_64"
set DIST_DIR_WEB_APPS_LINUX_ARM64="%CURRENT_DIR%\release\linux-arm64"
set VERSION=10.6.4.10
set PACKAGE_NAME_WEB_APPS_WIN_X86_64=mapgis-boot-%VERSION%-win-x86_64
set PACKAGE_NAME_WEB_APPS_LINUX_X86_64=mapgis-boot-%VERSION%-linux-x86_64
set PACKAGE_NAME_WEB_APPS_LINUX_ARM64=mapgis-boot-%VERSION%-linux-arm64
set INSTALL_DIR=%CURRENT_DIR%\..\install\
set INSTALL_DIR_WIN_X86_64="%INSTALL_DIR%%PACKAGE_NAME_WEB_APPS_WIN_X86_64%"
set INSTALL_DIR_LINUX_X86_64="%INSTALL_DIR%%PACKAGE_NAME_WEB_APPS_LINUX_X86_64%"
set INSTALL_DIR_LINUX_ARM64="%INSTALL_DIR%%PACKAGE_NAME_WEB_APPS_LINUX_ARM64%"

echo. & pause
echo.

if exist "%INSTALL_DIR%" rmdir /s/q "%INSTALL_DIR%"

mkdir "%INSTALL_DIR%"
mkdir "%INSTALL_DIR_WIN_X86_64%"
mkdir "%INSTALL_DIR_LINUX_X86_64%"
mkdir "%INSTALL_DIR_LINUX_ARM64%"


xcopy /s /y "%DIST_DIR_WEB_APPS_WIN_X86_64%" "%INSTALL_DIR_WIN_X86_64%"
xcopy /s /y "%DIST_DIR_WEB_APPS_LINUX_X86_64%" "%INSTALL_DIR_LINUX_X86_64%"
xcopy /s /y "%DIST_DIR_WEB_APPS_LINUX_ARM64%" "%INSTALL_DIR_LINUX_ARM64%"
7z a "%INSTALL_DIR%%PACKAGE_NAME_WEB_APPS_WIN_X86_64%.zip" %INSTALL_DIR_WIN_X86_64%
7z a "%INSTALL_DIR%%PACKAGE_NAME_WEB_APPS_LINUX_X86_64%.tar" %INSTALL_DIR_LINUX_X86_64%
7z a "%INSTALL_DIR%%PACKAGE_NAME_WEB_APPS_LINUX_X86_64%.tar.gz" "%INSTALL_DIR%%PACKAGE_NAME_WEB_APPS_LINUX_X86_64%.tar"
7z a "%INSTALL_DIR%%PACKAGE_NAME_WEB_APPS_LINUX_ARM64%.tar" %INSTALL_DIR_LINUX_ARM64%
7z a "%INSTALL_DIR%%PACKAGE_NAME_WEB_APPS_LINUX_ARM64%.tar.gz" "%INSTALL_DIR%%PACKAGE_NAME_WEB_APPS_LINUX_ARM64%.tar"
echo.
echo [info] package end 
echo. & pause

endlocal

cd package