"""Flyway migration helpers."""

from __future__ import annotations

import os
import shutil
import subprocess
from pathlib import Path

from iws.config.settings import Settings, get_settings

PROJECT_ROOT = Path(__file__).resolve().parents[2]
FLYWAY_SCRIPT = PROJECT_ROOT / "infra" / "scripts" / "flyway.sh"


def _flyway_binary() -> str:
    return os.environ.get("FLYWAY_BIN", "flyway")


def _flyway_env(settings: Settings) -> dict[str, str]:
    env = os.environ.copy()
    env["FLYWAY_URL"] = settings.flyway_url
    env["FLYWAY_USER"] = settings.flyway_user
    env["FLYWAY_PASSWORD"] = settings.flyway_password
    env["FLYWAY_LOCATIONS"] = settings.flyway_locations
    return env


def run_flyway(command: str, settings: Settings | None = None) -> subprocess.CompletedProcess[str]:
    settings = settings or get_settings()
    env = _flyway_env(settings)
    flyway = _flyway_binary()

    if shutil.which(flyway) is not None:
        config_file = PROJECT_ROOT / "flyway.conf"
        cmd = [flyway, f"-configFiles={config_file}", command]
        return subprocess.run(
            cmd,
            check=True,
            text=True,
            capture_output=True,
            cwd=PROJECT_ROOT,
            env=env,
        )

    if not FLYWAY_SCRIPT.is_file():
        raise FileNotFoundError(
            "Flyway CLI not found and Docker script is missing at "
            f"{FLYWAY_SCRIPT}. Install Flyway or run `make migrate`."
        )

    return subprocess.run(
        [str(FLYWAY_SCRIPT), command],
        check=True,
        text=True,
        capture_output=True,
        cwd=PROJECT_ROOT,
        env=env,
    )


def migrate(settings: Settings | None = None) -> None:
    run_flyway("migrate", settings=settings)


def info(settings: Settings | None = None) -> subprocess.CompletedProcess[str]:
    return run_flyway("info", settings=settings)
