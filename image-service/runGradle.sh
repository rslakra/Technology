#!/bin/bash
#Author: Rohtash Lakra
clear
#set -euo pipefail
#IFS=$'\n\t'
# Source common version function
source "$(dirname "$0")/version.sh"

echo
echo "${JAVA_HOME}"
echo
SNAPSHOT_VERSION=$(buildVersion SNAPSHOT)
RELEASE_VERSION=$(buildVersion)
#java -jar build/libs/image-service-$RELEASE_VERSION.jar
#./gradlew clean build -x test
./gradlew clean bootRun -Pversion=$RELEASE_VERSION
echo
