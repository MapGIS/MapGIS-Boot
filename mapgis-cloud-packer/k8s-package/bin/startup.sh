#!/bin/bash
cd `dirname $0`

CURRENT_DIR=$(cd `dirname $0`; pwd)
VALUES_FILE=$CURRENT_DIR/values.yaml

die() {
  echo "ERROR: $1"
  exit 2
}

warning() {
  echo -e "\e[33;1;33mWARNING: $1\e[33;0m"
}

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

# nsCount existNs ---> 1
# nsCount notExistNs ---> 0
# nsCount --> die namespace is empty
nsCount() {
  ns=$1
  [[ -z "$ns" ]] && die "namespace is empty"
  echo `kubectl get ns --ignore-not-found=true | awk -F ' ' '{ print $1 }' | grep "^$ns$" -c`
}

nfsServer=$(getValueFromValues nfsServer)
nfsPath=$(getValueFromValues nfsPath)
nfsStorageClassName=$(getValueFromValues nfsStorageClassName)

if [[ -z $nfsServer && -n $nfsStorageClassName ]]; then
  if [[ `kubectl get storageclass $nfsStorageClassName --ignore-not-found=true --no-headers=true | grep -c .` != 1 ]]; then
      die 'storage class name does not exist';
    fi
fi

# namespace
ns=$(getValueFromValues namespace)
[[ -z "$ns" ]] && ns="mapgis"
if [[ $(nsCount $ns) -gt 0 ]];then
  echo "namespace exist: $ns"
else
  kubectl create ns $ns
fi

# install
appFullname=$(getValueFromValues fullnameOverride)
if [[ $($CURRENT_DIR/helm list -n $ns | awk -F ' ' '{ print $1 }' | grep '^'${appFullname}'$' -c) -gt 0 ]];then
  $CURRENT_DIR/helm upgrade $appFullname $CURRENT_DIR -n $ns
else
  $CURRENT_DIR/helm install $appFullname $CURRENT_DIR -n $ns
fi