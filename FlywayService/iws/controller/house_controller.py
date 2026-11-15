from typing import Annotated

from fastapi import APIRouter, Depends, status
from sqlalchemy.ext.asyncio import AsyncSession

from iws.config.database import get_db_session
from iws.persistence.entity.house import House
from iws.persistence.repository.house_repository import HouseRepository
from iws.schema.models import HouseCreate, HouseResponse, HouseUpdate
from iws.service.house_service import HouseService

router = APIRouter(prefix="/homes", tags=["homes"])

DbSession = Annotated[AsyncSession, Depends(get_db_session)]


def _house_service(session: DbSession) -> HouseService:
    return HouseService(HouseRepository(session))


HouseServiceDep = Annotated[HouseService, Depends(_house_service)]


@router.get("", response_model=list[HouseResponse])
async def list_houses(service: HouseServiceDep) -> list[House]:
    return await service.filter()


@router.get("/{house_id}", response_model=HouseResponse)
async def get_house(house_id: int, service: HouseServiceDep) -> House:
    return await service.find_by_id(house_id)


@router.post("", response_model=HouseResponse, status_code=status.HTTP_201_CREATED)
async def create_house(payload: HouseCreate, service: HouseServiceDep) -> House:
    house = House(owner=payload.owner, fully_paid=payload.fully_paid)
    return await service.create(house)


@router.put("/{house_id}", response_model=HouseResponse)
async def update_house(house_id: int, payload: HouseUpdate, service: HouseServiceDep) -> House:
    house = House(id=house_id)
    if payload.owner is not None:
        house.owner = payload.owner
    if payload.fully_paid is not None:
        house.fully_paid = payload.fully_paid
    return await service.update(house)


@router.delete("/{house_id}", response_model=HouseResponse)
async def delete_house(house_id: int, service: HouseServiceDep) -> House:
    return await service.delete(house_id)
