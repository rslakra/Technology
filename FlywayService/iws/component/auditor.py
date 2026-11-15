from iws.config.settings import get_settings


def get_current_auditor() -> str:
    return get_settings().default_auditor
