@echo off

cd /d %~dp0

call package-common.bat linux-x86_64

if %errorlevel% NEQ 0 ( 
    exit /b %errorlevel% 
)