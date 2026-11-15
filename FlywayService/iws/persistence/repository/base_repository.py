from sqlalchemy import select
from sqlalchemy.ext.asyncio import AsyncSession

from iws.config.database import Base

EntityT = Base
IdT = int | str


class BaseRepository[EntityT: Base, IdT: (int, str)]:
    def __init__(self, session: AsyncSession, model: type[EntityT]) -> None:
        self._session = session
        self._model = model

    async def find_by_id(self, entity_id: IdT) -> EntityT | None:
        return await self._session.get(self._model, entity_id)

    async def find_all(self) -> list[EntityT]:
        result = await self._session.scalars(select(self._model))
        return list(result.all())

    async def save(self, entity: EntityT) -> EntityT:
        self._session.add(entity)
        await self._session.commit()
        await self._session.refresh(entity)
        return entity

    async def delete(self, entity: EntityT) -> None:
        await self._session.delete(entity)
        await self._session.commit()
