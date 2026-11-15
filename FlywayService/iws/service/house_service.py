from iws.component.auditor import get_current_auditor
from iws.exception.record_not_found import RecordNotFoundException
from iws.persistence.entity.house import House
from iws.persistence.repository.house_repository import HouseRepository
from iws.service.abstract_service import AbstractService


class HouseService(AbstractService[House, int]):
    def __init__(self, repository: HouseRepository) -> None:
        self._repository = repository

    async def create(self, house: House) -> House:
        auditor = get_current_auditor()
        house.created_by = auditor
        house.updated_by = auditor
        return await self._repository.save(house)

    async def find_by_id(self, house_id: int) -> House:
        house = await self._repository.find_by_id(house_id)
        if house is None:
            raise RecordNotFoundException("Record not found for id={}", house_id)
        return house

    async def filter(self) -> list[House]:
        return await self._repository.find_all()

    async def update(self, house: House) -> House:
        if house.id is not None:
            existing = await self.find_by_id(house.id)
            existing.owner = house.owner
            existing.fully_paid = house.fully_paid
            existing.updated_by = get_current_auditor()
            house = existing
        return await self._repository.save(house)

    async def delete(self, house_id: int) -> House:
        house = await self.find_by_id(house_id)
        await self._repository.delete(house)
        return house
