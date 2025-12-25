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
RELEASE_VERSION=$(buildVersion)
mvn package cargo:run -Drevision=$RELEASE_VERSION -Pcargo-run
echo
