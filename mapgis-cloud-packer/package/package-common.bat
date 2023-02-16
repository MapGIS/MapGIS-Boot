@echo off
echo.
echo [信息] 打包服务器产品
echo.

cd /d %~dp0
cd ../

setlocal

set CURRENT_DIR=%cd%

if "%~1" neq "" set "RELEASE_DIR=%CURRENT_DIR%/release/%~1"

echo "系统架构: %~1"

set OUTPUT_DIR=%CURRENT_DIR%/output
if "%~2" neq "" set "OUTPUT_DIR=%~2"

echo "来源目录: %OUTPUT_DIR%"

if not exist "%RELEASE_DIR%" mkdir "%RELEASE_DIR%"

if exist "%RELEASE_DIR%/bin" rmdir /s/q "%RELEASE_DIR%/bin"
if exist "%RELEASE_DIR%/config" rmdir /s/q "%RELEASE_DIR%/config"
if exist "%RELEASE_DIR%/lib" rmdir /s/q "%RELEASE_DIR%/lib"
if exist "%RELEASE_DIR%/sql" rmdir /s/q "%RELEASE_DIR%/sql"

xcopy /y /e "%OUTPUT_DIR%\bin" "%RELEASE_DIR%\bin\"
xcopy /y /e "%OUTPUT_DIR%\config" "%RELEASE_DIR%\config\"
xcopy /y /e "%OUTPUT_DIR%\lib" "%RELEASE_DIR%\lib\"
xcopy /y /e "%OUTPUT_DIR%\sql" "%RELEASE_DIR%\sql\"

endlocal

cd /d %~dp0