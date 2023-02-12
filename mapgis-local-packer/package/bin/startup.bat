@echo off

cd /d %~dp0

set LIBS=^
  mapgis-server

for /d %%l in (%LIBS%) do (
  start "" "startup-server.bat" %%l
)