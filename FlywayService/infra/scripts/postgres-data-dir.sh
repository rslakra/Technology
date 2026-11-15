#!/usr/bin/env bash
# Author: Rohtash Lakra
# Print the expanded PostgreSQL data directory from .env.

set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(cd "${SCRIPT_DIR}/../.." && pwd)"

if [ -f "${PROJECT_DIR}/.env" ]; then
  set -a
  # shellcheck disable=SC1091
  . "${PROJECT_DIR}/.env"
  set +a
fi

path="${POSTGRES_DATA_DIR:-${HOME}/Downloads/Data/PostgreSQL/Accounts}"
case "${path}" in
  "~/"*) printf '%s\n' "${HOME}/${path#~/}" ;;
  "~") printf '%s\n' "${HOME}" ;;
  *) printf '%s\n' "${path}" ;;
esac
