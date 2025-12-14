#!/bin/bash
# Author: Rohtash Lakra
clear
# Source common version function
source "$(dirname "$0")/version.sh"

echo
#JAVA_VERSION=11
#export JAVA_HOME=$(/usr/libexec/java_home -v $JAVA_VERSION)
echo "${JAVA_HOME}"
echo
#mvn clean install -DskipTests=true
SNAPSHOT_VERSION=$(buildVersion SNAPSHOT)
RELEASE_VERSION=$(buildVersion)
#echo "RELEASE_VERSION: ${RELEASE_VERSION}, SNAPSHOT_VERSION: ${SNAPSHOT_VERSION}"
#mvn clean install -DskipTests=true -DprojectVersion=$RELEASE_VERSION
mvn clean install -Drevision=$SNAPSHOT_VERSION
mvn install -Drevision=$RELEASE_VERSION -DskipTests=true
#mvn clean install -DskipTests=true -DprojectVersion=$(./makeVersion.sh SNAPSHOT)
echo
