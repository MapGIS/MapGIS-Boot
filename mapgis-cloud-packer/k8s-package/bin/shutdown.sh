#!/bin/bash
cd `dirname $0`

CURRENT_DIR=$(cd `dirname $0`; pwd)
VALUES_FILE=$CURRENT_DIR/values.yaml

getValueFromValues(){
  fileName=$2
  [[ -z $fileName ]] && fileName=$VALUES_FILE
  index=$(sed -n '/'"$1":'/=' $fileName)
  if [ -z $index ];then
    echo ""
    return $?
  fi
  lineStr=$(cat $fileName | awk 'NR=='"$index"'')
  echo ${lineStr: $(expr length $1)+2}
  return $?
}

# namespace
ns=$(getValueFromValues namespace)
[[ -z "$ns" ]] && ns="mapgis"

# uninstall
appFullname=$(getValueFromValues fullnameOverride)
$CURRENT_DIR/helm uninstall $appFullname -n $ns