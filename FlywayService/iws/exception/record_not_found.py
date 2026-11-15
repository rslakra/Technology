class RecordNotFoundException(Exception):
    """Raised when a requested database record does not exist."""

    def __init__(self, message: str | None = None, *args: object) -> None:
        if message is None:
            super().__init__("Record not found.")
        elif args:
            super().__init__(message.format(*args))
        else:
            super().__init__(message)
