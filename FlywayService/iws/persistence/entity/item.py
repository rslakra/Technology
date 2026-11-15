from sqlalchemy import BigInteger, ForeignKey
from sqlalchemy.orm import Mapped, mapped_column, relationship

from iws.persistence.entity.house import House
from iws.persistence.entity.named_entity import NamedEntity


class Item(NamedEntity):
    __tablename__ = "items"

    house_id: Mapped[int] = mapped_column(
        BigInteger,
        ForeignKey("houses.id"),
        nullable=False,
    )
    house: Mapped[House] = relationship("House", lazy="joined")
