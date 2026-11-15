# Accounts Service

FastAPI service for managing accounts, homes, and items.

## Project layout

```
├── iws/                       # REST API (FastAPI app, services, persistence)
│   ├── config/                # Settings, database, Flyway helpers
│   ├── controller/            # FastAPI routers
│   ├── service/               # Business logic
│   ├── persistence/           # SQLAlchemy models and repositories
│   ├── schema/                # Pydantic models
│   ├── exception/
│   └── component/
├── webapp/                    # Reserved for UI / frontend code
├── flyway/                    # Flyway SQL migrations
├── infra/
│   ├── docker/                # Application Dockerfile
│   └── scripts/               # Helper scripts (Flyway)
└── tests/
```

## Prerequisites

- Python 3.12+
- [uv](https://docs.astral.sh/uv/) package manager
- Docker (for PostgreSQL and Flyway migrations)

## Running the application

**First-time setup**

```bash
make install
cp example.env .env
```

**Start order** (run top to bottom)

```bash
make start-postgres   # 1. database
make migrate          # 2. schema (skip if AUTO_MIGRATE=true and app runs migrations on startup)
make start            # 3. FastAPI app
```

Or use `./runService.sh` for step 3 only (Postgres must already be running).

**Stop order** (reverse of start)

```bash
make stop-app         # 1. FastAPI app
make stop-postgres    # 2. database
```

**Daily dev** — `make start-postgres` and `make start` are safe to re-run; `make start` stops any existing app on `APP_PORT` first.

Use `localhost` in the browser (not `0.0.0.0`). With `CONTEXT_PATH=/account-service`:

| URL | Description |
|-----|-------------|
| http://localhost:8016/account-service/ | Service root (redirects to docs) |
| http://localhost:8016/account-service/docs | Swagger UI |
| http://localhost:8016/account-service/health | Health check |
| http://localhost:8016/account-service/rest/homes | Homes API |

## Testing

```bash
make test
make cov
```

## Database migrations (Flyway)

Migrations live in `flyway/`.

```bash
make migrate          # apply migrations
make flyway-info      # migration status
make revision MSG='add column'  # scaffold a new SQL migration
```

Flyway runs via Docker by default (`infra/scripts/flyway.sh`). Install the [Flyway CLI](https://flywaydb.org/) locally to run migrations without Docker.

## Database storage and access

PostgreSQL runs in Docker (`accounts-postgres`). Data files are stored on your host at `POSTGRES_DATA_DIR` from `.env`:

```
~/Downloads/Data/PostgreSQL/Accounts
```

On your machine that expands to:

```
/Users/<you>/Downloads/Data/PostgreSQL/Accounts
```

`make start-postgres` creates that folder and bind-mounts it into the container at `/var/lib/postgresql/data`. PostgreSQL stores a **directory tree** there (not a single `.db` file). Files appear after Postgres initializes the cluster on first start.

**Verify where data actually lives**

```bash
docker inspect accounts-postgres --format '{{range .Mounts}}{{if eq .Destination "/var/lib/postgresql/data"}}{{.Source}}{{end}}{{end}}'
```

This should match the expanded `POSTGRES_DATA_DIR` path. If it shows something like `/var/lib/docker/volumes/...`, the container was created with an old config. Fix it:

```bash
make stop-postgres
docker rm -f accounts-postgres
make start-postgres
make migrate
```

**Databases in the cluster**

| Database | Valid? | Used by |
|----------|--------|---------|
| `postgres` | Yes | PostgreSQL system database (always present, do not drop) |
| `Accounts` | Yes | Application (`DATABASE_NAME` in `.env`) |
| `AccountsTest` | Yes | Tests (`DATABASE_TEST_NAME` in `.env`) |
| `accounts` | No | Leftover from an older setup (lowercase default). Safe to drop; the app does not use it |

PostgreSQL treats `Accounts` and `accounts` as different databases.

**Fresh start options**

Reset application data only (keeps the cluster, drops app schema/data):

```bash
make reset-db          # empty and remigrate Accounts
make reset-test-db     # empty and remigrate AccountsTest
```

Full wipe (deletes all data files, recreates cluster, runs migrations):

```bash
make fresh-postgres
```

This stops the app, removes the container, clears `POSTGRES_DATA_DIR`, runs `make start-postgres`, then `make migrate`. After a full wipe you should only see `postgres`, `Accounts`, and `AccountsTest`.

To drop the unused lowercase database manually:

```bash
docker exec -it accounts-postgres psql -U postgres -c 'DROP DATABASE IF EXISTS accounts;'
```

**Connection settings** (from `.env`):

| Setting | Value |
|---------|-------|
| Host | `localhost` |
| Port | `5444` |
| User | `postgres` |
| Password | `postgres` |

**Access via `psql` (inside the container)**

```bash
docker exec -it accounts-postgres psql -U postgres -d Accounts
```

List tables:

```sql
\dt
```

**Access via a GUI client** (DBeaver, pgAdmin, DataGrip, etc.)

- Host: `localhost`
- Port: `5444`
- Database: `Accounts`
- User / password: `postgres` / `postgres`

**Access from the app**

The FastAPI service connects using `DATABASE_*` values in `.env` (see `iws/config/settings.py`).

**Change the data location**

Edit `POSTGRES_DATA_DIR` in `.env`, then run `make start-postgres` (it recreates the container if the mount path changed). Stop Postgres first if you are moving an existing data directory manually.

## Make targets

| Target | Description |
|--------|-------------|
| `make start-postgres` | Start PostgreSQL (safe if already running) |
| `make stop-postgres` | Stop PostgreSQL container |
| `make restart-postgres` | Restart PostgreSQL container |
| `make log-postgres` | Tail PostgreSQL logs |
| `make reset-db` | Drop/recreate `Accounts` and run migrations |
| `make reset-test-db` | Drop/recreate `AccountsTest` and run migrations |
| `make fresh-postgres` | Delete all Postgres data files and recreate from scratch |
| `make start` | Start the FastAPI app (stops any existing app on `APP_PORT` first) |
| `make stop-app` | Stop the FastAPI app and app Docker container |

## API endpoints

| Method | Path | Description |
|--------|------|-------------|
| GET | `/account-service/health` | Health check |
| GET/POST | `/account-service/rest/homes` | List / create houses |
| GET/PUT/DELETE | `/account-service/rest/homes/{id}` | House CRUD |
| GET/POST | `/account-service/rest/items` | List / create items |
| GET/PUT/DELETE | `/account-service/rest/items/{id}` | Item CRUD |

### Reference Documentation

For further reference, please consider the following sections:

* [FastAPI documentation](https://fastapi.tiangolo.com/)
* [SQLAlchemy documentation](https://docs.sqlalchemy.org/)
* [Flyway documentation](https://documentation.red-gate.com/fd)
* [uv package manager](https://docs.astral.sh/uv/)

### Guides

The following guides illustrate how to use some features concretely:

* [FastAPI first steps](https://fastapi.tiangolo.com/tutorial/first-steps/)
* [SQLAlchemy ORM quickstart](https://docs.sqlalchemy.org/en/20/orm/quickstart.html)
* [Flyway getting started](https://documentation.red-gate.com/fd/getting-started-with-flyway-184127223.html)

### Additional Links

These additional references should also help you:

* [Pydantic documentation](https://docs.pydantic.dev/)
* [pytest documentation](https://docs.pytest.org/)

# Author

- Rohtash Lakra
