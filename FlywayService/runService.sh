#!/bin/bash
# Author: Rohtash Lakra
clear
source "$(dirname "$0")/version.sh"

RELEASE_VERSION=$(buildVersion)

echo
echo "Starting account-service ${RELEASE_VERSION}"
echo

make start
echo
