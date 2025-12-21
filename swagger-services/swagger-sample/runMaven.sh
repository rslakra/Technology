#!/bin/bash
# Author: Rohtash Lakra
# Source common version function
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
source "${SCRIPT_DIR}/../version.sh"

echo
echo "${JAVA_HOME}"
echo
SNAPSHOT_VERSION=$(buildVersion SNAPSHOT)
RELEASE_VERSION=$(buildVersion)
#java -jar target/account-service-$RELEASE_VERSION.jar
#mvn clean package -DskipTests
mvn clean spring-boot:run -Drevision=$RELEASE_VERSION
echo
