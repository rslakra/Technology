#!/usr/bin/env bash
# Author: Rohtash Lakra
# Run Flyway migrations using Docker (no local Flyway install required).

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "${SCRIPT_DIR}/../.." && pwd)"

cd "${PROJECT_DIR}"

if [ -f "${PROJECT_DIR}/.env" ]; then
  set -a
  # shellcheck disable=SC1091
  . "${PROJECT_DIR}/.env"
  set +a
fi

FLYWAY_URL="${FLYWAY_URL:-jdbc:postgresql://localhost:5444/Accounts}"
FLYWAY_USER="${FLYWAY_USER:-postgres}"
FLYWAY_PASSWORD="${FLYWAY_PASSWORD:-postgres}"
FLYWAY_COMMAND="${1:-migrate}"

docker run --rm \
  --network host \
  -v "${PROJECT_DIR}/flyway:/flyway/sql" \
  -e "FLYWAY_URL=${FLYWAY_URL}" \
  -e "FLYWAY_USER=${FLYWAY_USER}" \
  -e "FLYWAY_PASSWORD=${FLYWAY_PASSWORD}" \
  -e "FLYWAY_LOCATIONS=filesystem:/flyway/sql" \
  flyway/flyway:10-alpine \
  "${FLYWAY_COMMAND}"
