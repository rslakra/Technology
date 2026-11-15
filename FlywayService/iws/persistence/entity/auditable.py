from __future__ import annotations

from datetime import datetime
from typing import TypeVar

from sqlalchemy import BigInteger, DateTime, String, func
from sqlalchemy.orm import Mapped, mapped_column

from iws.config.database import Base

AuditorType = TypeVar("AuditorType", bound=str)


class Auditable(Base):
    __abstract__ = True

    created_on: Mapped[int] = mapped_column(
        BigInteger,
        nullable=False,
        server_default=func.extract("epoch", func.now()).cast(BigInteger),
    )
    created_at: Mapped[datetime] = mapped_column(
        DateTime,
        nullable=False,
        server_default=func.now(),
    )
    created_by: Mapped[str] = mapped_column(String(32), nullable=False, default="system")
    updated_on: Mapped[int] = mapped_column(
        BigInteger,
        nullable=False,
        server_default=func.extract("epoch", func.now()).cast(BigInteger),
    )
    updated_at: Mapped[datetime] = mapped_column(
        DateTime,
        nullable=False,
        server_default=func.now(),
        onupdate=func.now(),
    )
    updated_by: Mapped[str] = mapped_column(String(32), nullable=False, default="system")
