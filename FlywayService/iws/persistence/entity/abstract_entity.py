from sqlalchemy import BigInteger
from sqlalchemy.orm import Mapped, mapped_column

from iws.persistence.entity.auditable import Auditable


class AbstractEntity(Auditable):
    __abstract__ = True

    id: Mapped[int] = mapped_column(BigInteger, primary_key=True, autoincrement=True)
