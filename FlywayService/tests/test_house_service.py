import pytest
from iws.exception.record_not_found import RecordNotFoundException
from iws.persistence.entity.house import House
from iws.service.house_service import HouseService


class FakeHouseRepository:
    def __init__(self, houses: dict[int, House] | None = None) -> None:
        self._houses = houses or {}
        self._next_id = max(self._houses.keys(), default=0) + 1

    async def find_by_id(self, house_id: int) -> House | None:
        return self._houses.get(house_id)

    async def find_all(self) -> list[House]:
        return list(self._houses.values())

    async def save(self, house: House) -> House:
        if house.id is None:
            house.id = self._next_id
            self._next_id += 1
        self._houses[house.id] = house
        return house

    async def delete(self, house: House) -> None:
        if house.id is not None:
            self._houses.pop(house.id, None)


@pytest.mark.asyncio
async def test_find_house_by_id():
    house = House(id=1, owner="Rohtash Lakra", fully_paid=True)
    service = HouseService(FakeHouseRepository({1: house}))

    result = await service.find_by_id(1)

    assert result.owner == "Rohtash Lakra"
    assert result.fully_paid is True


@pytest.mark.asyncio
async def test_find_house_by_id_not_found():
    service = HouseService(FakeHouseRepository())

    with pytest.raises(RecordNotFoundException):
        await service.find_by_id(99)
