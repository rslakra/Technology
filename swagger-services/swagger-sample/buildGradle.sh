#!/bin/bash
# Author: Rohtash Lakra
# Source common version function
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "${SCRIPT_DIR}/../version.sh"

echo
echo "${JAVA_HOME}"
echo
#./gradlew clean build -x test
SNAPSHOT_VERSION=$(buildVersion SNAPSHOT)
RELEASE_VERSION=$(buildVersion)
#echo "RELEASE_VERSION: ${RELEASE_VERSION}, SNAPSHOT_VERSION: ${SNAPSHOT_VERSION}"
#./gradlew clean build -x test -Pversion=$RELEASE_VERSION
./gradlew clean build -x test -Pversion=$SNAPSHOT_VERSION
./gradlew build -x test -Pversion=$RELEASE_VERSION
#./gradlew clean build -x test -Pversion=$(./makeVersion.sh SNAPSHOT)
echo
