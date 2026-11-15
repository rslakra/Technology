from sqlalchemy import Boolean, String
from sqlalchemy.orm import Mapped, mapped_column

from iws.persistence.entity.abstract_entity import AbstractEntity


class House(AbstractEntity):
    __tablename__ = "houses"

    owner: Mapped[str | None] = mapped_column(String(128), unique=True, nullable=True)
    fully_paid: Mapped[bool] = mapped_column(Boolean, default=False, nullable=False)
