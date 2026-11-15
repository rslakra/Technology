from sqlalchemy import Boolean, String
from sqlalchemy.orm import Mapped, mapped_column

from iws.persistence.entity.abstract_entity import AbstractEntity


class User(AbstractEntity):
    __tablename__ = "users"

    user_name: Mapped[str] = mapped_column(String(128), unique=True, nullable=False)
    password: Mapped[str] = mapped_column(String(128), nullable=False)
    active: Mapped[bool] = mapped_column(Boolean, default=False, nullable=False)
