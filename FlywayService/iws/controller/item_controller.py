from typing import Annotated

from fastapi import APIRouter, Depends, status
from sqlalchemy.ext.asyncio import AsyncSession

from iws.config.database import get_db_session
from iws.persistence.entity.item import Item
from iws.persistence.repository.item_repository import ItemRepository
from iws.schema.models import ItemCreate, ItemResponse, ItemUpdate
from iws.service.item_service import ItemService

router = APIRouter(prefix="/items", tags=["items"])

DbSession = Annotated[AsyncSession, Depends(get_db_session)]


def _item_service(session: DbSession) -> ItemService:
    return ItemService(ItemRepository(session))


ItemServiceDep = Annotated[ItemService, Depends(_item_service)]


@router.get("", response_model=list[ItemResponse])
async def list_items(service: ItemServiceDep) -> list[Item]:
    return await service.filter()


@router.get("/{item_id}", response_model=ItemResponse)
async def get_item(item_id: int, service: ItemServiceDep) -> Item:
    return await service.find_by_id(item_id)


@router.post("", response_model=ItemResponse, status_code=status.HTTP_201_CREATED)
async def create_item(payload: ItemCreate, service: ItemServiceDep) -> Item:
    item = Item(name=payload.name, house_id=payload.house_id)
    return await service.create(item)


@router.put("/{item_id}", response_model=ItemResponse)
async def update_item(item_id: int, payload: ItemUpdate, service: ItemServiceDep) -> Item:
    item = Item(id=item_id)
    if payload.name is not None:
        item.name = payload.name
    if payload.house_id is not None:
        item.house_id = payload.house_id
    return await service.update(item)


@router.delete("/{item_id}", response_model=ItemResponse)
async def delete_item(item_id: int, service: ItemServiceDep) -> Item:
    return await service.delete(item_id)
