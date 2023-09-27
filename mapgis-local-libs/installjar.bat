@echo off
cd /d %~dp0
set dir=%CD%\libs
call mvn install:install-file -DgroupId=jdk.tools -DartifactId=jdk.tools -Dversion=1.8 -Dpackaging=jar "-Dfile=%CD%\jdk-1.8-tools\tools.jar"
cd libs
for /f "delims=" %%i in ('dir /b /a-d *.jar') do (
    echo.
    echo -------------------  start install:%%i  -------------------
    call mvn install:install-file -DgroupId=com.zondy.mapgis -DartifactId=%%~ni -Dversion=1.0.0 -Dpackaging=jar "-Dfile=%dir%\%%i"
    echo ------------------- finished install:%%i ------------------
    echo.
)
rem install shenwei jar 使用maven profile来控制项目中sqlite的依赖版本
set jar_libs=%dir%\..\modified3rdLibs
call mvn install:install-file -DgroupId=org.xerial -DartifactId=sqlite-jdbc -Dversion=3.32.3.3-shenwei -Dpackaging=jar "-Dfile=%jar_libs%\sqlite-jdbc-3.32.3.3-shenwei.jar"

rem install longxin jar 使用maven profile来控制项目中sqlite的依赖版本
set jar_libs=%dir%\..\modified3rdLibs
call mvn install:install-file -DgroupId=org.xerial -DartifactId=sqlite-jdbc -Dversion=3.25.2-longxin -Dpackaging=jar "-Dfile=%jar_libs%\sqlite-jdbc-3.25.2-longxin.jar"
pause