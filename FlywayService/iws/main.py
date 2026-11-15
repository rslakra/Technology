import logging
from collections.abc import AsyncIterator
from contextlib import asynccontextmanager

from fastapi import FastAPI, Request
from fastapi.responses import JSONResponse, RedirectResponse

from iws.config.database import engine
from iws.config.flyway import migrate
from iws.config.settings import get_settings
from iws.controller.house_controller import router as house_router
from iws.controller.item_controller import router as item_router
from iws.exception.record_not_found import RecordNotFoundException

logger = logging.getLogger(__name__)
settings = get_settings()


@asynccontextmanager
async def lifespan(_: FastAPI) -> AsyncIterator[None]:
    logger.info("Starting account-service on %s:%s", settings.app_host, settings.app_port)
    if settings.auto_migrate:
        migrate(settings)
    yield
    await engine.dispose()
    logger.info("Stopped account-service")


def create_app() -> FastAPI:
    context_path = settings.app_context_path.rstrip("/")
    docs_url = f"{context_path}/docs" if context_path else "/docs"
    openapi_url = f"{context_path}/openapi.json" if context_path else "/openapi.json"
    redoc_url = f"{context_path}/redoc" if context_path else "/redoc"
    rest_prefix = f"{context_path}{settings.rest_prefix}" if context_path else settings.rest_prefix
    health_path = f"{context_path}/health" if context_path else "/health"

    app = FastAPI(
        title="Accounts Service",
        description="FastAPI accounts service",
        version="0.0.0",
        lifespan=lifespan,
        docs_url=docs_url,
        openapi_url=openapi_url,
        redoc_url=redoc_url,
    )

    @app.exception_handler(RecordNotFoundException)
    async def handle_not_found(_: Request, exc: RecordNotFoundException) -> JSONResponse:
        return JSONResponse(status_code=404, content={"detail": str(exc)})

    @app.get("/", include_in_schema=False)
    async def root() -> RedirectResponse:
        return RedirectResponse(url=docs_url)

    if context_path:
        @app.get(context_path, include_in_schema=False)
        async def context_root() -> RedirectResponse:
            return RedirectResponse(url=docs_url)

    @app.get(health_path, tags=["health"])
    async def health() -> dict[str, str]:
        return {"status": "ok"}

    app.include_router(house_router, prefix=rest_prefix)
    app.include_router(item_router, prefix=rest_prefix)
    return app


app = create_app()
