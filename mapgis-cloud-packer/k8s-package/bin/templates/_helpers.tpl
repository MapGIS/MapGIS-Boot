{{/*
Expand the name of the chart.
*/}}
{{- define "mapgis.name" -}}
  {{- default .Chart.Name .Values.nameOverride | trunc 63 | trimSuffix "-" }}
{{- end }}

{{/*
Create a default fully qualified app name.
We truncate at 63 chars because some Kubernetes name fields are limited to this (by the DNS naming spec).
If release name contains chart name it will be used as a full name.
*/}}
{{- define "mapgis.fullname" -}}
  {{- if .Values.fullnameOverride }}
    {{- .Values.fullnameOverride | trunc 63 | trimSuffix "-" }}
  {{- else }}
    {{- $name := default .Chart.Name .Values.nameOverride }}
    {{- if contains $name .Release.Name }}
      {{- .Release.Name | trunc 63 | trimSuffix "-" }}
    {{- else }}
      {{- printf "%s-%s" .Release.Name $name | trunc 63 | trimSuffix "-" }}
    {{- end }}
  {{- end }}
{{- end }}

{{/*
Common labels
*/}}
{{- define "mapgis.labels" -}}
  {{ include "mapgis.selectorLabels" . }}
{{- end }}

{{- define "mapgis.kindLabel" -}}
  kind: {{ include "mapgis.fullname" . }}
{{- end }}

{{/*
Selector labels
*/}}
{{- define "mapgis.selectorLabels" -}}
  app: {{ include "mapgis.fullname" . }}
{{- end }}

{{/*
App Port
*/}}
{{- define "mapgis.appPort" -}}
  {{ .Values.appPort | default "32000" }}
{{- end -}}

{{/*
Image
*/}}
{{- define "mapgis.imageRegistry" -}}
  {{ .Values.imageRegistry | default "registry.cn-beijing.aliyuncs.com" }}
{{- end -}}

{{- define "mapgis.imageNamespace" -}}
  {{ .Values.imageNamespace | default "mapgis" }}
{{- end -}}

{{- define "mapgis.imageNfsClientProvisioner" -}}
  {{ .Values.imageNfsClientProvisioner | default "sig-storage/nfs-subdir-external-provisioner:v4.0.2" }}
{{- end -}}

{{- define "mapgis.imageNacos" -}}
  {{ .Values.imageNacos | default "nacos/nacos-server" }}
{{- end -}}

{{- define "mapgis.imageRedis" -}}
  {{ .Values.imageRedis | default "redis" }}
{{- end -}}

{{- define "mapgis.imageMySQL" -}}
  {{- if .Values.imageMySQL -}}
    {{ .Values.imageMySQL }}
  {{- else -}}
    {{ include "mapgis.name" . }}-mysql:10.6.2.10
  {{- end -}}
{{- end -}}

{{- define "mapgis.imageGateway" -}}
  {{- if .Values.imageGateway -}}
    {{ .Values.imageGateway }}
  {{- else -}}
    {{ include "mapgis.name" . }}-gateway:10.6.2.10
  {{- end -}}
{{- end -}}

{{- define "mapgis.imageMonitor" -}}
  {{- if .Values.imageMonitor -}}
    {{ .Values.imageMonitor }}
  {{- else -}}
    {{ include "mapgis.name" . }}-monitor:10.6.2.10
  {{- end -}}
{{- end -}}

{{- define "mapgis.imageAuth" -}}
  {{- if .Values.imageAuth -}}
    {{ .Values.imageAuth }}
  {{- else -}}
    {{ include "mapgis.name" . }}-auth:10.6.2.10
  {{- end -}}
{{- end -}}

{{- define "mapgis.imageSystem" -}}
  {{- if .Values.imageSystem -}}
    {{ .Values.imageSystem }}
  {{- else -}}
    {{ include "mapgis.name" . }}-system:10.6.2.10
  {{- end -}}
{{- end -}}

{{- define "mapgis.imageFile" -}}
  {{- if .Values.imageFile -}}
    {{ .Values.imageFile }}
  {{- else -}}
    {{ include "mapgis.name" . }}-file:10.6.2.10
  {{- end -}}
{{- end -}}

{{- define "mapgis.imageJob" -}}
  {{- if .Values.imageJob -}}
    {{ .Values.imageJob }}
  {{- else -}}
    {{ include "mapgis.name" . }}-job:10.6.2.10
  {{- end -}}
{{- end -}}

{{- define "mapgis.imageGen" -}}
  {{- if .Values.imageGen -}}
    {{ .Values.imageGen }}
  {{- else -}}
    {{ include "mapgis.name" . }}-gen:10.6.2.10
  {{- end -}}
{{- end -}}

{{- define "mapgis.imagePullPolicy" -}}
  {{ .Values.imagePullPolicy | default "IfNotPresent" }}
{{- end -}}

{{- define "mapgis.imagePullSecret" -}}
  {{ if .Values.imagePullSecret -}}
      imagePullSecrets:
      - name: {{ .Values.imagePullSecret }}
  {{- end }}
{{- end -}}

{{/*
NFS server
*/}}
{{- define "mapgis.nfsServer" -}}
  {{ .Values.nfsServer | default "none" }}
{{- end -}}

{{- define "mapgis.nfsPath" -}}
  {{ .Values.nfsPath | default "/nfs/data" }}
{{- end -}}

{{- define "mapgis.nfsStorageClassName" -}}
  {{- if .Values.nfsStorageClassName -}}
    {{ .Values.nfsStorageClassName }}
  {{- else -}}
    {{ .Release.Namespace }}-{{ include "mapgis.fullname" . }}-storage-class
  {{- end -}}
{{- end -}}

{{/*
Resources
*/}}
{{- define "mapgis.resourceCpuLimit" -}}
  {{ .Values.resourceCpuLimit | default "2" }}
{{- end -}}

{{- define "mapgis.resourceMemoryLimit" -}}
  {{ .Values.resourceMemoryLimit | default "4Gi" }}
{{- end -}}

{{/*
Database Name
*/}}
{{- define "mapgis.dbName" -}}
  {{- if .Values.dbName -}}
    {{ .Values.dbName }}
  {{- else -}}
    mapgis-cloud-{{ include "mapgis.name" . }}
  {{- end -}}
{{- end -}}

{{- define "mapgis.configDbName" -}}
  {{ .Values.configDbName | default "mapgis-cloud-config" }}
{{- end -}}