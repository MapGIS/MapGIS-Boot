@echo off

cd /d %~dp0

set LIBS=mapgis-cloud-module-gateway,mapgis-cloud-module-monitor,mapgis-module-auth-server,mapgis-module-file-server,mapgis-module-job-server,mapgis-module-gen-server,mapgis-module-system-server
for /d %%l in (%LIBS%) do (
  start "" "startup-server.bat" %%l
)