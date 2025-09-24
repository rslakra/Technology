#
# Author: Rohtash Lakra
#

import asyncio
import logging
import time


class DatabaseEngine:
    def __init__(self, logger: logging.Logger):
        self.logger = logger

    def connect(self):
        self.logger.info("connect")

    def execute(self, query):
        self.logger.info('Executing DB query...')


class UserService:
    def __init__(self, logger: logging.Logger, db_engine: DatabaseEngine):
        self.logger = logger
        self.db = db_engine

    def register(self, payload):
        self.logger.info('register started %s' % payload)
        self.db.execute(...)
        time.sleep(1)  # we simulate I/O wait here
        self.logger.info('register completed for %s' % payload)


class AsyncUserService:
    def __init__(self, logger: logging.Logger, db_engine: DatabaseEngine):
        self.logger = logger
        self.db = db_engine

    async def register(self, payload):
        self.logger.info('register started %s' % payload)
        self.db.execute(...)
        await asyncio.sleep(1)  # we simulate I/O wait here
        self.logger.info('register completed for %s' % payload)
