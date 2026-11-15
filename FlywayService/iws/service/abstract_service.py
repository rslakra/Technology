class AbstractService[EntityT, IdT]:
    async def find_by_id(self, entity_id: IdT) -> EntityT:
        raise NotImplementedError
