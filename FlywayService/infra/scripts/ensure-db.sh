#!/usr/bin/env bash
# Author: Rohtash Lakra
# Create application databases if they do not exist (safe to re-run).

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

DB_CONTAINER="${DB_CONTAINER:-accounts-postgres}"
DB_USER="${DATABASE_USER:-postgres}"
DB_PASSWORD="${DATABASE_PASSWORD:-postgres}"
DB_NAME="${DATABASE_NAME:-Accounts}"
DB_TEST_NAME="${DATABASE_TEST_NAME:-AccountsTest}"

if ! docker ps --format '{{.Names}}' | grep -qx "${DB_CONTAINER}"; then
  echo "ERROR: PostgreSQL container '${DB_CONTAINER}' is not running. Run 'make start-postgres' first." >&2
  exit 1
fi

ensure_database() {
  local name="$1"
  local exists
  exists="$(docker exec -e PGPASSWORD="${DB_PASSWORD}" "${DB_CONTAINER}" \
    psql -U "${DB_USER}" -tAc "SELECT 1 FROM pg_database WHERE datname = '${name}'")"
  if [ "${exists}" != "1" ]; then
    echo "Creating database \"${name}\"..."
    docker exec -e PGPASSWORD="${DB_PASSWORD}" "${DB_CONTAINER}" \
      psql -U "${DB_USER}" -c "CREATE DATABASE \"${name}\""
  fi
}

ensure_database "${DB_NAME}"
ensure_database "${DB_TEST_NAME}"
