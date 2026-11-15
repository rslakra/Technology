from sqlalchemy import String
from sqlalchemy.orm import Mapped, mapped_column

from iws.persistence.entity.abstract_entity import AbstractEntity


class NamedEntity(AbstractEntity):
    __abstract__ = True

    name: Mapped[str | None] = mapped_column(String(128), nullable=True)
