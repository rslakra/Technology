#!/bin/bash
#Author: Rohtash Lakra
clear
#set -euo pipefail
#IFS=$'\n\t'
# Source common version function
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "${SCRIPT_DIR}/../version.sh"

echo
echo "${JAVA_HOME}"
echo
SNAPSHOT_VERSION=$(buildVersion SNAPSHOT)
RELEASE_VERSION=$(buildVersion)
#java -jar build/libs/account-service-$RELEASE_VERSION.jar
#./gradlew clean build -x test
./gradlew clean bootRun -Pversion=$RELEASE_VERSION
echo
