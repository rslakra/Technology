from sqlalchemy.ext.asyncio import AsyncSession

from iws.persistence.entity.house import House
from iws.persistence.repository.base_repository import BaseRepository


class HouseRepository(BaseRepository[House, int]):
    def __init__(self, session: AsyncSession) -> None:
        super().__init__(session, House)
