from datetime import datetime

from pydantic import BaseModel, ConfigDict, Field


class AuditableSchema(BaseModel):
    model_config = ConfigDict(from_attributes=True)

    created_on: int | None = None
    created_at: datetime | None = None
    created_by: str | None = None
    updated_on: int | None = None
    updated_at: datetime | None = None
    updated_by: str | None = None


class HouseCreate(BaseModel):
    owner: str | None = None
    fully_paid: bool = False


class HouseUpdate(BaseModel):
    owner: str | None = None
    fully_paid: bool | None = None


class HouseResponse(AuditableSchema):
    id: int
    owner: str | None = None
    fully_paid: bool


class ItemCreate(BaseModel):
    name: str | None = None
    house_id: int = Field(..., ge=1)


class ItemUpdate(BaseModel):
    name: str | None = None
    house_id: int | None = Field(default=None, ge=1)


class ItemResponse(AuditableSchema):
    id: int
    name: str | None = None
    house_id: int
