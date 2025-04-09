#
# Author: Rohtash Lakra
# Reference - https://realpython.com/flask-project/
#
import importlib.metadata
import logging
import os
from datetime import datetime
from pathlib import Path
from typing import Any

from dotenv import load_dotenv
from flask import Flask, Blueprint, make_response, current_app, request, jsonify
from flask_cors import CORS
# https://flask.palletsprojects.com/en/3.0.x/deploying/proxy_fix/
from werkzeug.middleware.proxy_fix import ProxyFix
from werkzeug.exceptions import NotFound

from api import bp as api_bp
from common.config import Config
from framework.enums import EnvType
from framework.enums import KeyEnum
from framework.logger import DefaultLogger
from webapp.routes import bp as webapp_bp

logger = logging.getLogger(__name__)


class WebApp:
    """Create WebApp class"""
    __HOST = 'host'
    __PORT = 'port'
    __DEBUG = 'debug'

    def __init__(self):
        self.path = Path()
        self.basedir = str(self.path.cwd())
        logger.debug(f"basedir={self.basedir}")
        # sys.path.append(self.basedir)
        # logger.debug(f"sys.path={sys.path}")
        self.environment: dict = {}
        self.app: Flask = None

    def __load_env(self, test_mode: bool = False):
        logger.debug(f"__load_env({test_mode})")
        # with self.app.app_context():
        flask_version = importlib.metadata.version("flask")
        logger.debug(f"Running Application [{self.app.name}] on version [{flask_version}] with testMode [{test_mode}]")
        logger.info(f"ENV_TYPE={EnvType.get_env_type()}")
        # Load the environment variables
        dotEnvFileName = ".env.test" if test_mode or EnvType.is_testing(EnvType.get_env_type()) else ".env"
        logger.info(f"dotEnvFileName={dotEnvFileName}")
        env_file_path = self.path.cwd().joinpath(dotEnvFileName)  # self.path.cwd() / '.env'
        logger.debug(f"env_file_path={env_file_path}")

        # loads .env file and updates the local env object
        load_dotenv(dotenv_path=env_file_path)
        self.set_env(self.__HOST, os.getenv(self.__HOST, "127.0.0.1"))
        self.set_env(self.__PORT, os.getenv(self.__PORT, '8080'))
        self.set_env(self.__DEBUG, os.getenv(self.__DEBUG, False))
        self.set_env(KeyEnum.ENV_TYPE.name, EnvType.get_env_type())
        if test_mode or EnvType.is_testing(EnvType.get_env_type()):
            self.set_env(KeyEnum.ENV_TYPE.name, EnvType.TEST.name)

        self.set_env(KeyEnum.LOG_FILE_NAME.name, os.getenv(KeyEnum.LOG_FILE_NAME.name, 'iws.log'))
        logger.debug(f"environment={self.environment}")

    def set_env(self, key: str, value: Any):
        self.environment[key] = value

    def get_env(self, key: str) -> Any:
        return self.environment.get(key, None)

    def run(self):
        """Loads Configurations and Runs Web Application"""
        host = self.get_env(self.__HOST)
        port = self.get_env(self.__PORT)
        debug = self.get_env(self.__DEBUG)
        with self.app.app_context():
            current_app.logger.debug(f"host={host}, port={port}, debug={debug}")

        # run application with params
        self.app.run(host=host, port=port, debug=debug, load_dotenv=True)

    def create_app(self, config_class: Config = Config, test_mode: bool = False) -> Flask:
        """
        Create an application your application factory pattern.

        With an application factory, your project’s structure becomes more organized.
        It encourages you to separate different parts of your application, like routes, configurations, and initializations,
        into different files later on. This encourages a cleaner and more maintainable codebase.
        """
        # create a new flask application object
        app = Flask(__name__)
        # app = connexion.App(__name__, specification_dir="./")
        # app.add_api("swagger.yml")

        # use custom logger adapter
        app.logger = DefaultLogger(app)
        app.logger.logConfig()

        # app.wsgi_app = ProxyFix(app.wsgi_app, x_for=1, x_proto=1, x_host=1, x_prefix=1)
        app.wsgi_app = ProxyFix(app.wsgi_app)
        # wsgi_app = ProxyFix(app.wsgi_app)
        self.app = app
        self.__load_env(test_mode=test_mode)
        # load app's configs
        app.config.from_object(config_class)
        if test_mode or EnvType.is_testing(EnvType.get_env_type()):
            app.config.update({
                KeyEnum.ENV_TYPE.name: EnvType.TEST.name,
                "TESTING": True,
            })

        # with self.app.app_context():
        #     current_app.logger.debug(f"app.config={app.config}")

        # Check CORS Enabled
        if Config.CORS_ENABLED:
            CORS(app)

        # Initialize/Register Flask Extensions/Components, if any
        # Initialize/Register Default Error Handlers, if any

        @app.errorhandler(404)
        def not_found(error):
            """404 - NotFound Error Handler"""
            current_app.logger.error(f'request={request}, errorClass={type(error)}, error={error}')
            if isinstance(error, NotFound):
                return make_response(jsonify('Not Found!'), 404)
            else:
                return make_response(jsonify(error), 404)

        @app.errorhandler(400)
        def bad_request(error):
            """400 - BadRequest Error Handler"""
            current_app.logger.error(f'request={request}, errorClass={type(error)}, error={error}')
            return make_response(jsonify('Bad Request!'), 400)

        @app.errorhandler(500)
        def app_error(error):
            """500 - InternalServer Error Handler"""
            current_app.logger.error(f'request={request}, errorClass={type(error)}, error={error}')
            return make_response(jsonify('Internal Server Error!'), 500)

        # Register Date & Time Formatter for Jinja Template
        @app.template_filter('strftime')
        def _jinja2_filter_datetime(date_str, datetime_format: str = None):
            """Formats the date_str"""
            date = datetime.strptime(date_str, "%Y-%m-%dT%H:%M:%S.%f")
            native = date.replace(tzinfo=None)
            if datetime_format:
                return native.strftime(datetime_format)
            else:
                return native.strftime("%b %d, %Y at %I:%M%p")

        # Initialize/Register Blueprints, if any

        """
        Create an instance of Blueprint prefixed with '/bp' as named bp.
        Parameters:
            name: represents the name of the blueprint, which Flask’s routing mechanism uses and identifies it in the project.
            __name__: The Blueprint’s import name, which Flask uses to locate the Blueprint’s resources.
            url_prefix: the path to prepend to all of the Blueprint’s URLs.
        """
        # bp = Blueprint("iws", __name__, url_prefix="/posts")
        bp = Blueprint("iws", __name__)

        # register more app's here.
        bp.register_blueprint(api_bp)
        bp.register_blueprint(webapp_bp)

        # Register root blueprint with app that connects an app with other end-points
        app.register_blueprint(bp)

        # Initialize/Register Request's behavior/db connection
        if not test_mode:
            # app.before_request(connector.open_connection())
            # app.teardown_request(connector.close_connection())
            pass

        return app
