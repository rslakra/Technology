#!/usr/bin/env bash
# Author: Rohtash Lakra
# Helpers to avoid multiple services binding the same host port.

set -euo pipefail

port_pids() {
  lsof -nP -iTCP:"${1}" -sTCP:LISTEN -t 2>/dev/null || true
}

port_in_use() {
  [ -n "$(port_pids "$1")" ]
}

stop_docker_container() {
  local name="$1"
  if docker ps -a --format '{{.Names}}' 2>/dev/null | grep -qx "$name"; then
    echo "Stopping Docker container: ${name}"
    docker rm -f "$name" >/dev/null 2>&1 || true
  fi
}

stop_port_listeners() {
  local port="$1"
  local pids
  pids="$(port_pids "$port")"
  if [ -n "$pids" ]; then
    echo "Stopping process(es) on port ${port}"
    # shellcheck disable=SC2086
    kill $pids 2>/dev/null || true
    sleep 1
  fi
}

case "${1:-}" in
  port-in-use)
    port_in_use "$2"
    ;;
  stop-app)
    stop_docker_container "${3:-}"
    stop_port_listeners "$2"
    ;;
  ensure-port-free)
    if port_in_use "$2"; then
      echo "ERROR: ${3:-Port $2} is already in use (port $2). Run 'make stop-app' first." >&2
      exit 1
    fi
    ;;
  *)
    echo "Usage: $0 {port-in-use|stop-app|ensure-port-free} PORT [ARG]" >&2
    exit 1
    ;;
esac
