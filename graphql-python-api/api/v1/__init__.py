#
# Author: Rohtash Lakra
#
from flask import Blueprint

bp = Blueprint("v1", __name__, url_prefix="/v1")
"""
Create an instance of Blueprint prefixed with '/bp' as named bp.
Parameters:
    name: represents the name of the blueprint, which Flask’s routing mechanism uses and identifies it in the project.
    __name__: The Blueprint’s import name, which Flask uses to locate the Blueprint’s resources.
    url_prefix: the path to prepend to all of the Blueprint’s URLs.
"""


# register end-points here
# bp.register_blueprint(comment_v1_bp)
