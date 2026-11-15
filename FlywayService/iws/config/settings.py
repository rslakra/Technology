from functools import lru_cache

from pydantic import AliasChoices, Field
from pydantic_settings import BaseSettings, SettingsConfigDict


class Settings(BaseSettings):
    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore",
        populate_by_name=True,
    )

    app_env: str = "development"
    app_host: str = "0.0.0.0"
    app_port: int = 8016
    app_context_path: str = Field(
        default="/account-service",
        validation_alias=AliasChoices("CONTEXT_PATH", "APP_CONTEXT_PATH"),
    )
    rest_prefix: str = Field(
        default="/rest",
        validation_alias=AliasChoices("REST_PREFIX", "APP_REST_PREFIX"),
    )
    default_auditor: str = "Rohtash Lakra"

    database_host: str = "localhost"
    database_port: int = 5444
    database_user: str = "postgres"
    database_password: str = "postgres"
    database_name: str = "Accounts"
    database_test_name: str = "AccountsTest"

    flyway_url: str = "jdbc:postgresql://localhost:5444/Accounts"
    flyway_user: str = "postgres"
    flyway_password: str = "postgres"
    flyway_locations: str = "filesystem:flyway"

    auto_migrate: bool = False

    @property
    def database_url(self) -> str:
        return (
            f"postgresql+asyncpg://{self.database_user}:{self.database_password}"
            f"@{self.database_host}:{self.database_port}/{self.database_name}"
        )

    @property
    def database_test_url(self) -> str:
        return (
            f"postgresql+asyncpg://{self.database_user}:{self.database_password}"
            f"@{self.database_host}:{self.database_port}/{self.database_test_name}"
        )

    @property
    def is_development(self) -> bool:
        return self.app_env in {"development", "develop"}


@lru_cache
def get_settings() -> Settings:
    return Settings()
