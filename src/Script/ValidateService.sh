#!/bin/sh
set -e
component=@project.name@
MaxRetry=10;
count=0;
echo " validating service $component"
STATUS=`/etc/init.d/${component} status`
RESULT=`echo $STATUS | grep -o 'Running'` 

if [ $RESULT = "Running" ]; then
exit 0
fi
exit 1