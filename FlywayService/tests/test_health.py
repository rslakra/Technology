import pytest
from httpx import ASGITransport, AsyncClient
from iws.config.settings import get_settings
from iws.main import create_app


@pytest.fixture
def app():
    return create_app()


@pytest.fixture
async def client(app):
    transport = ASGITransport(app=app)
    async with AsyncClient(transport=transport, base_url="http://test") as async_client:
        yield async_client


@pytest.mark.asyncio
async def test_health(client):
    context_path = get_settings().app_context_path.rstrip("/")
    response = await client.get(f"{context_path}/health")
    assert response.status_code == 200
    assert response.json() == {"status": "ok"}
