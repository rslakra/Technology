.PHONY: install start stop-app stop test cov format lint check migrate flyway-info revision reset-test-db reset-db fresh-postgres start-postgres stop-postgres log-postgres restart-postgres build-docker-image run-docker-container ci-lint ci-format-check ci-typecheck ci-test status show-config help

# NOTE: Run make from the repository root. Uses .env for configuration.

# =============================================================================
# Environment Variables Configuration
# =============================================================================

APP_ROOT := $(abspath $(dir $(lastword $(MAKEFILE_LIST))))
VENV_DIR ?= .venv
UV_BIN_PATH ?= $(HOME)/.local/bin/uv

APP_HOST ?= 0.0.0.0
APP_PORT ?= 8016
APP_RELOAD ?= --reload
APP_MODULE ?= iws.main:app
APP_PACKAGE ?= iws
PYTHONPATH ?= $(APP_ROOT)

DB_NAME ?= accounts
DB_TEST_NAME ?= accounts_test
DB_CONTAINER ?= accounts-postgres
DB_USER ?= postgres
DB_PASSWORD ?= postgres
DB_PORT ?= 5444

define LOAD_ENV
	set -a && [ -f "$(APP_ROOT)/.env" ] && . "$(APP_ROOT)/.env" && set +a
endef

define DB_ENV
	db_name="$${DATABASE_NAME:-$(DB_NAME)}"; \
	db_test_name="$${DATABASE_TEST_NAME:-$(DB_TEST_NAME)}"; \
	db_user="$${DATABASE_USER:-$(DB_USER)}"; \
	db_password="$${DATABASE_PASSWORD:-$(DB_PASSWORD)}"; \
	db_port="$${DATABASE_PORT:-$(DB_PORT)}"
endef

DOCKER_COMPOSE_FILE ?= docker-compose.yaml
DOCKER_IMAGE_TAG ?= account-service
DOCKER_CONTAINER_NAME ?= account-service
DOCKER_CONTAINER_PORT ?= 8016

FLYWAY_SCRIPT ?= $(APP_ROOT)/infra/scripts/flyway.sh
ENSURE_DB_SCRIPT ?= $(APP_ROOT)/infra/scripts/ensure-db.sh
PORTS_SCRIPT ?= $(APP_ROOT)/infra/scripts/ports.sh
FLYWAY_URL ?= jdbc:postgresql://localhost:$(DB_PORT)/$(DB_NAME)
FLYWAY_USER ?= $(DB_USER)
FLYWAY_PASSWORD ?= $(DB_PASSWORD)

PYTEST_MARKERS ?= "not integration"
COV_FAIL_UNDER ?= 65

BOLD := \033[1m
DIM := \033[2m
GREEN := \033[32m
DARK_GREEN := \033[38;5;28m
BLUE := \033[34m
MAGENTA := \033[35m
DODGER_BLUE := \033[38;2;30;144;255m
BLACK := \033[30m
RESET := \033[0m

define HELP_TITLE
	@printf "\n$(BOLD)$(MAGENTA)%s$(RESET)\n" "$(1)"
endef

define HELP_SECTION
	@printf "\n$(BOLD)$(DODGER_BLUE)%s$(RESET)\n" "$(1)"
endef

define HELP_CMD
	@printf "  $(BOLD)$(DARK_GREEN)make %-24s$(RESET) $(BLACK)- %s$(RESET)\n" "$(1)" "$(2)"
endef

define HELP_CONFIG
	@printf "  $(BLACK)%s$(RESET)\n" "$(1)"
endef

# Ensure uv is on PATH and run commands from APP_ROOT
define UV_RUN
	@uv_bin_path="$(UV_BIN_PATH)"; \
	uv_dir="$$(dirname $$uv_bin_path)"; \
	if ! command -v uv >/dev/null 2>&1; then export PATH="$$uv_dir:$$PATH"; fi; \
	cd "$(APP_ROOT)" && \
	export UV_PROJECT_ENVIRONMENT="$(VENV_DIR)" && \
	export PYTHONPATH="$(PYTHONPATH)" && \
	$(1)
endef

.DEFAULT_GOAL := help

help:
	$(call HELP_TITLE,account-service Makefile)
	$(call HELP_SECTION,General)
	$(call HELP_CMD,install,Install dependencies (venv: $(VENV_DIR)))
	$(call HELP_CMD,show-config,Show current configuration variables)
	$(call HELP_CMD,status,Show current setup status)
	$(call HELP_SECTION,Development)
	$(call HELP_CMD,start,Start FastAPI development server)
	$(call HELP_CMD,stop-app,Stop local server and app container on APP_PORT)
	$(call HELP_CMD,stop,Alias for stop-app)
	$(call HELP_CMD,test,Run tests - add FILE=path/to/test.py for specific file)
	$(call HELP_CMD,cov,Run tests with coverage)
	$(call HELP_SECTION,Database - Flyway)
	$(call HELP_CMD,migrate,Apply Flyway database migrations)
	$(call HELP_CMD,flyway-info,Show Flyway migration status)
	$(call HELP_CMD,revision,Create new Flyway SQL migration - MSG='description')
	$(call HELP_CMD,reset-db,Drop/recreate $(DB_NAME) and run migrations)
	$(call HELP_CMD,reset-test-db,Drop/recreate $(DB_TEST_NAME) and run migrations)
	$(call HELP_CMD,fresh-postgres,Delete all Postgres data and recreate from scratch)
	$(call HELP_SECTION,Code quality)
	$(call HELP_CMD,format,Format code with Ruff)
	$(call HELP_CMD,lint,Run Ruff linter)
	$(call HELP_CMD,check,Check formatting + types + linting)
	$(call HELP_SECTION,CI/CD)
	$(call HELP_CMD,ci-lint,Run linting without auto-fix)
	$(call HELP_CMD,ci-format-check,Check code formatting)
	$(call HELP_CMD,ci-typecheck,Run mypy type checker)
	$(call HELP_CMD,ci-test,Run tests with coverage XML)
	$(call HELP_SECTION,PostgreSQL)
	$(call HELP_CMD,start-postgres,Start PostgreSQL container)
	$(call HELP_CMD,stop-postgres,Stop PostgreSQL container)
	$(call HELP_CMD,log-postgres,View PostgreSQL container logs)
	$(call HELP_CMD,restart-postgres,Restart PostgreSQL container)
	$(call HELP_CMD,build-docker-image,Build application Docker image)
	$(call HELP_CMD,run-docker-container,Run application container)
	$(call HELP_SECTION,Configuration)
	$(call HELP_CONFIG,Run from: $(APP_ROOT))
	$(call HELP_CONFIG,Override variables: make start APP_PORT=9000 DB_NAME=mydb)
	@printf "\n"

show-config:
	@echo "Current Configuration"
	@echo "====================="
	@echo "  APP_ROOT=$(APP_ROOT)"
	@echo "  VENV_DIR=$(VENV_DIR)"
	@echo "  APP_HOST=$(APP_HOST)"
	@echo "  APP_PORT=$(APP_PORT)"
	@echo "  APP_MODULE=$(APP_MODULE)"
	@echo "  APP_PACKAGE=$(APP_PACKAGE)"
	@echo "  PYTHONPATH=$(PYTHONPATH)"
	@echo "  DB_NAME=$(DB_NAME)"
	@echo "  DB_TEST_NAME=$(DB_TEST_NAME)"
	@echo "  DB_PORT=$(DB_PORT)"
	@echo "  FLYWAY_SCRIPT=$(FLYWAY_SCRIPT)"
	@echo "  FLYWAY_URL=$(FLYWAY_URL)"

status:
	@echo "Setup Status"
	@echo "============"
	@uv_bin_path="$(UV_BIN_PATH)"; \
	if command -v uv >/dev/null 2>&1; then \
		echo "UV CLI: installed"; \
	elif [ -f "$$uv_bin_path" ]; then \
		echo "UV CLI: installed at $$uv_bin_path"; \
	else \
		echo "UV CLI: not installed (run: make install)"; \
	fi
	@if [ -f "$(APP_ROOT)/$(VENV_DIR)/pyvenv.cfg" ]; then \
		echo "Python venv: created ($(VENV_DIR))"; \
	else \
		echo "Python venv: not found (run: make install)"; \
	fi
	@if [ -f "$(APP_ROOT)/.env" ]; then \
		echo "Environment: .env found"; \
	else \
		echo "Environment: copy example.env to .env"; \
	fi
	@if docker ps --format '{{.Names}}' | grep -q '^$(DB_CONTAINER)$$'; then \
		echo "PostgreSQL: running ($(DB_CONTAINER))"; \
	else \
		echo "PostgreSQL: not running (run: make start-postgres)"; \
	fi
	@$(LOAD_ENV); \
	app_port="$${APP_PORT:-$(APP_PORT)}"; \
	if "$(PORTS_SCRIPT)" port-in-use "$$app_port"; then \
		echo "App server: running on port $$app_port"; \
	else \
		echo "App server: not running on port $$app_port"; \
	fi; \
	if docker ps --format '{{.Names}}' | grep -q '^$(DOCKER_CONTAINER_NAME)$$'; then \
		echo "App container: running ($(DOCKER_CONTAINER_NAME))"; \
	fi

install:
	@echo "Setting up account-service..."
	@if ! command -v python3 >/dev/null 2>&1; then \
		echo "Python 3 not found."; exit 1; \
	fi
	@uv_bin_path="$(UV_BIN_PATH)"; \
	uv_dir="$$(dirname $$uv_bin_path)"; \
	if ! command -v uv >/dev/null 2>&1; then \
		if [ ! -f "$$uv_bin_path" ]; then \
			curl -LsSf https://astral.sh/uv/install.sh | sh || exit 1; \
		fi; \
		export PATH="$$uv_dir:$$PATH"; \
	fi; \
	cd "$(APP_ROOT)" && \
	export UV_PROJECT_ENVIRONMENT="$(VENV_DIR)" && \
	uv sync --group dev
	@chmod +x "$(FLYWAY_SCRIPT)" "$(ENSURE_DB_SCRIPT)" "$(PORTS_SCRIPT)" "$(APP_ROOT)/infra/scripts/postgres-data-dir.sh"
	@echo "Done. Next: cp example.env .env && make start-postgres && make migrate"

stop-app:
	@$(LOAD_ENV); \
	app_port="$${APP_PORT:-$(APP_PORT)}"; \
	"$(PORTS_SCRIPT)" stop-app "$$app_port" "$(DOCKER_CONTAINER_NAME)"

stop: stop-app

start: stop-app
	@$(LOAD_ENV); \
	app_port="$${APP_PORT:-$(APP_PORT)}"; \
	app_host="$${APP_HOST:-$(APP_HOST)}"; \
	context_path="$${CONTEXT_PATH:-/account-service}"; \
	echo "Starting http://localhost:$$app_port$${context_path}/docs"; \
	uv_bin_path="$(UV_BIN_PATH)"; \
	uv_dir="$$(dirname $$uv_bin_path)"; \
	if ! command -v uv >/dev/null 2>&1; then export PATH="$$uv_dir:$$PATH"; fi; \
	cd "$(APP_ROOT)" && \
	export UV_PROJECT_ENVIRONMENT="$(VENV_DIR)" && \
	export PYTHONPATH="$(PYTHONPATH)" && \
	uv run uvicorn $(APP_MODULE) --host "$$app_host" --port "$$app_port" $(APP_RELOAD)

test:
	$(call UV_RUN,if [ -n "$(FILE)" ]; then uv run python -m pytest $(FILE); else uv run python -m pytest; fi)

cov:
	$(call UV_RUN,if [ -n "$(FILE)" ]; then uv run python -m pytest --cov=$(APP_PACKAGE) --cov-report=term-missing $(FILE); else uv run python -m pytest --cov=$(APP_PACKAGE) --cov-report=term-missing; fi)

format:
	@uv_bin_path="$(UV_BIN_PATH)"; \
	uv_dir="$$(dirname $$uv_bin_path)"; \
	if ! command -v uv >/dev/null 2>&1; then export PATH="$$uv_dir:$$PATH"; fi; \
	cd "$(APP_ROOT)" && export UV_PROJECT_ENVIRONMENT="$(VENV_DIR)" && \
	if [ -n "$(FILE)" ]; then \
		uv run python -m ruff check --fix $(FILE) && uv run python -m ruff format $(FILE); \
	else \
		uv run python -m ruff check --fix && uv run python -m ruff format; \
	fi

lint:
	@uv_bin_path="$(UV_BIN_PATH)"; \
	uv_dir="$$(dirname $$uv_bin_path)"; \
	if ! command -v uv >/dev/null 2>&1; then export PATH="$$uv_dir:$$PATH"; fi; \
	cd "$(APP_ROOT)" && export UV_PROJECT_ENVIRONMENT="$(VENV_DIR)" && \
	if [ -n "$(FILE)" ]; then uv run python -m ruff check $(FILE); else uv run python -m ruff check; fi

check:
	$(call UV_RUN,uv run python -m ruff check && uv run python -m ruff format --check && uv run python -m mypy $(APP_PACKAGE))

ci-lint:
	$(call UV_RUN,uv run python -m ruff check iws webapp tests --output-format=github)

ci-format-check:
	$(call UV_RUN,uv run python -m ruff format --check iws webapp tests)

ci-typecheck:
	$(call UV_RUN,uv run python -m mypy $(APP_PACKAGE) --show-error-codes)

ci-test:
	$(call UV_RUN,uv run python -m pytest -vv -m $(PYTEST_MARKERS) --cov=$(APP_PACKAGE) --cov-report=xml --cov-report=term --cov-fail-under=$(COV_FAIL_UNDER))

migrate:
	@echo "Running Flyway migrations..."
	@"$(ENSURE_DB_SCRIPT)"
	@"$(FLYWAY_SCRIPT)" migrate

flyway-info:
	@"$(FLYWAY_SCRIPT)" info

revision:
	@if [ -z "$(MSG)" ]; then \
		read -p "Enter migration description: " msg; \
	else \
		msg="$(MSG)"; \
	fi; \
	version=$$(ls "$(APP_ROOT)/flyway"/V*.sql 2>/dev/null | wc -l | tr -d ' '); \
	next=$$((version + 1)); \
	slug=$$(echo "$$msg" | tr '[:upper:]' '[:lower:]' | tr ' ' '_' | tr -cd 'a-z0-9_'); \
	file="$(APP_ROOT)/flyway/V$${next}__$${slug}.sql"; \
	printf '%s\n\n' "-- $$msg" > "$$file"; \
	echo "Created $$file"

reset-test-db:
	@$(LOAD_ENV); $(DB_ENV); \
	docker exec -e PGPASSWORD="$$db_password" $(DB_CONTAINER) psql -U "$$db_user" -c "DROP DATABASE IF EXISTS \"$$db_test_name\" WITH (FORCE);"; \
	docker exec -e PGPASSWORD="$$db_password" $(DB_CONTAINER) psql -U "$$db_user" -c "CREATE DATABASE \"$$db_test_name\";"; \
	FLYWAY_URL="jdbc:postgresql://localhost:$$db_port/$$db_test_name" $(MAKE) migrate

reset-db:
	@$(LOAD_ENV); $(DB_ENV); \
	docker exec -e PGPASSWORD="$$db_password" $(DB_CONTAINER) psql -U "$$db_user" -c "DROP DATABASE IF EXISTS \"$$db_name\" WITH (FORCE);"; \
	docker exec -e PGPASSWORD="$$db_password" $(DB_CONTAINER) psql -U "$$db_user" -c "CREATE DATABASE \"$$db_name\" WITH TEMPLATE template0;"; \
	FLYWAY_URL="jdbc:postgresql://localhost:$$db_port/$$db_name" $(MAKE) migrate

fresh-postgres: stop-app
	@$(LOAD_ENV); \
	pg_data="$("$(APP_ROOT)/infra/scripts/postgres-data-dir.sh")"; \
	echo "WARNING: Deleting all PostgreSQL data in: $$pg_data"; \
	$(MAKE) stop-postgres; \
	docker rm -f $(DB_CONTAINER) 2>/dev/null || true; \
	if [ -d "$$pg_data" ]; then \
		find "$$pg_data" -mindepth 1 -maxdepth 1 -exec rm -rf {} +; \
	fi; \
	$(MAKE) start-postgres; \
	$(MAKE) migrate

start-postgres:
	@$(LOAD_ENV); $(DB_ENV); \
	pg_data="$("$(APP_ROOT)/infra/scripts/postgres-data-dir.sh")"; \
	mkdir -p "$$pg_data"; \
	if docker ps -a --format '{{.Names}}' | grep -qx '$(DB_CONTAINER)'; then \
		current=$$(docker inspect $(DB_CONTAINER) --format '{{range .Mounts}}{{if eq .Destination "/var/lib/postgresql/data"}}{{.Source}}{{end}}{{end}}' 2>/dev/null || true); \
		if [ -n "$$current" ] && [ "$$current" != "$$pg_data" ]; then \
			echo "PostgreSQL data directory changed:"; \
			echo "  was: $$current"; \
			echo "  now: $$pg_data"; \
			echo "Recreating container $(DB_CONTAINER)..."; \
			docker rm -f $(DB_CONTAINER) >/dev/null; \
		fi; \
	fi; \
	if docker ps --format '{{.Names}}' | grep -q '^$(DB_CONTAINER)$$'; then \
		echo "PostgreSQL already running ($(DB_CONTAINER))"; \
		echo "Data directory: $$pg_data"; \
	elif "$(PORTS_SCRIPT)" port-in-use "$$db_port"; then \
		echo "ERROR: Port $$db_port is already in use. Stop the other service first."; \
		exit 1; \
	else \
		docker compose -f "$(APP_ROOT)/$(DOCKER_COMPOSE_FILE)" up -d postgres; \
	fi; \
	if docker ps --format '{{.Names}}' | grep -q '^$(DB_CONTAINER)$$'; then \
		echo "Waiting for PostgreSQL..."; \
		until docker exec $(DB_CONTAINER) pg_isready -U "$$db_user" -d postgres >/dev/null 2>&1; do sleep 1; done; \
		"$(ENSURE_DB_SCRIPT)"; \
		echo "PostgreSQL is ready."; \
		echo "Data directory: $$pg_data"; \
	fi

stop-postgres:
	docker compose -f "$(APP_ROOT)/$(DOCKER_COMPOSE_FILE)" down

restart-postgres:
	docker compose -f "$(APP_ROOT)/$(DOCKER_COMPOSE_FILE)" down && \
	docker compose -f "$(APP_ROOT)/$(DOCKER_COMPOSE_FILE)" up -d postgres

log-postgres:
	docker compose -f "$(APP_ROOT)/$(DOCKER_COMPOSE_FILE)" logs -f --tail=100

build-docker-image:
	docker build -f "$(APP_ROOT)/infra/docker/Dockerfile" -t $(DOCKER_IMAGE_TAG) "$(APP_ROOT)"

run-docker-container: stop-app
	@$(LOAD_ENV); \
	app_port="$${APP_PORT:-$(DOCKER_CONTAINER_PORT)}"; \
	"$(PORTS_SCRIPT)" ensure-port-free "$$app_port" "App port"; \
	docker run -d -p "$$app_port":8016 --name $(DOCKER_CONTAINER_NAME) $(DOCKER_IMAGE_TAG)
