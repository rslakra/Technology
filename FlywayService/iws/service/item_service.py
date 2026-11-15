from iws.component.auditor import get_current_auditor
from iws.exception.record_not_found import RecordNotFoundException
from iws.persistence.entity.item import Item
from iws.persistence.repository.item_repository import ItemRepository
from iws.service.abstract_service import AbstractService


class ItemService(AbstractService[Item, int]):
    def __init__(self, repository: ItemRepository) -> None:
        self._repository = repository

    async def create(self, item: Item) -> Item:
        auditor = get_current_auditor()
        item.created_by = auditor
        item.updated_by = auditor
        return await self._repository.save(item)

    async def find_by_id(self, item_id: int) -> Item:
        item = await self._repository.find_by_id(item_id)
        if item is None:
            raise RecordNotFoundException()
        return item

    async def filter(self) -> list[Item]:
        return await self._repository.find_all()

    async def update(self, item: Item) -> Item:
        if item.id is not None:
            existing = await self.find_by_id(item.id)
            existing.name = item.name
            existing.house_id = item.house_id
            existing.updated_by = get_current_auditor()
            item = existing
        return await self._repository.save(item)

    async def delete(self, item_id: int) -> Item:
        item = await self.find_by_id(item_id)
        await self._repository.delete(item)
        return item
