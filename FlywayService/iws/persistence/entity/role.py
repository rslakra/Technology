from sqlalchemy import Boolean, String
from sqlalchemy.orm import Mapped, mapped_column

from iws.persistence.entity.abstract_entity import AbstractEntity


class Role(AbstractEntity):
    __tablename__ = "roles"

    name: Mapped[str] = mapped_column(String(32), unique=True, nullable=False)
    active: Mapped[bool] = mapped_column(Boolean, default=False, nullable=False)
