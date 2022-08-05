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

{{- define "mapgis.imageRepository" -}}
  {{ .Values.imageRepository | default ( include "mapgis.name" . ) }}
{{- end -}}

{{- define "mapgis.imageTag" -}}
  {{ .Values.imageTag | default "1.0" }}
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
    {{ include "mapgis.fullname" . }}-storage-class
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