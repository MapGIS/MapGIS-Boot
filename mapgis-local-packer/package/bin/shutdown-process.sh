#!/bin/bash
# kill process by process cmd keyword
procArg=$1
selfName=shutdown-process.sh
if [ -z "$procArg" ] ; then
    echo "第一个参数必须为进程关键字，不能为空"
    exit -1;
fi
ps -aux | grep "${procArg}" | grep -v "${selfName}" | grep -v grep | awk '{print $2}' | while read pid
do
    echo "${procArg} is running, to kill pid=$pid"
    kill -9 $pid
    echo "kill result: $?"
done
