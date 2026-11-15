#!/bin/bash
# Author: Rohtash Lakra
clear
source "$(dirname "$0")/version.sh"

RELEASE_VERSION=$(buildVersion)
SNAPSHOT_VERSION=$(SNAPSHOT=1 buildVersion)

echo
echo "Building account-service snapshot: ${SNAPSHOT_VERSION}, release: ${RELEASE_VERSION}"
echo

make install
make ci-test
make build-docker-image DOCKER_IMAGE_TAG="account-service:${RELEASE_VERSION}"
echo
