#!/bin/bash
# Author: Rohtash Lakra
VERSION="0.0"
# Build Version Function
function buildVersion() {
  GIT_COMMIT_COUNT=$(git rev-list HEAD --count)
  if [ $? -ne 0 ]; then
    VERSION="${VERSION}.1"
  else
    VERSION="${VERSION}.${GIT_COMMIT_COUNT}"
  fi
  SNAPSHOT="${SNAPSHOT:-$!}"
  if [[ ! -z ${SNAPSHOT} ]]; then
      VERSION="${VERSION}-SNAPSHOT"
  fi

  echo "${VERSION}";
}

echo
echo "${JAVA_HOME}"
echo
SNAPSHOT_VERSION=$(buildVersion SNAPSHOT)
RELEASE_VERSION=$(buildVersion)
mvn package cargo:run -Drevision=$SNAPSHOT_VERSION
echo
