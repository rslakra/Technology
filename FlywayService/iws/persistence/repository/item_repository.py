from sqlalchemy.ext.asyncio import AsyncSession

from iws.persistence.entity.item import Item
from iws.persistence.repository.base_repository import BaseRepository


class ItemRepository(BaseRepository[Item, int]):
    def __init__(self, session: AsyncSession) -> None:
        super().__init__(session, Item)
