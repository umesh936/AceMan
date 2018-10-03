#!/bin/bash

# turn on echo
#set -x verbose
# Set Time Zone
#TZ='Asia/Kolkata';
#export TZ

componentName=@project.name@
date=`date`;
echo "Deploying latest Version on $date"
SERVICE_FILE="/home/mettl/${componentName}/app.jar"
INIT_LINK="/etc/init.d/${componentName}"


if [  -f $SERVICE_FILE ]; then
    echo "file exists"
    rm -rf ${SERVICE_FILE}
    rm -rf ${INIT_LINK}
fi


INSTALLERS_BASE="/home/mettl/${componentName}/installers"

cp ${INSTALLERS_BASE}/${componentName}.jar ${SERVICE_FILE}  
# create soft link
ln -s $SERVICE_FILE $INIT_LINK

CURRENT_TIME=`date +%y-%m-%d_%H:%M:%S`

if [ ! -f /home/mettl/${componentName}/lastDeploy.txt ]; then
    touch /home/mettl/${componentName}/lastDeploy.txt
fi
echo "$CURRENT_TIME" > /home/mettl/${componentName}/lastDeploy.txt

chmod +x $SERVICE_FILE

rm -rf $INSTALLERS_BASE/${componentName}.jar 
