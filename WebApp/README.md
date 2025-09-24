# WebApp

---

The ```WebApp``` contains the python web server.


## Project Structure

```
    /
    ├── <modules>                   # The name of the module
    ├── api                         # The web server
    │    ├── crawler                # crawler
    │    ├── indexer                # indexer
    │    ├── searcher               # searcher
    │    ├── v1                     # v1 blueprints/endpoints
    │    └── __init__.py            # The package initializer
    ├── resources                   # The service resources
    │    ├── images                 # images
    │    │    ├── __init__.py       # The package initializer
    │    │    └── __init__.py       # The package initializer
    │    └── __init__.py            # The package initializer
    ├── tests                       # The service test-cases
    │    ├── data                   # data
    │    ├── __init__.py            # The package initializer
    │    └── __init__.py            # The package initializer
    ├── webapp                      # The web server
    │    ├── static                 # The static contents like css, js etc.
    │    │    ├── css               # css files
    │    │    ├── images            # image files
    │    │    ├── js                # JavaScript files
    │    │    └── __init__.py       # The package initializer
    │    ├── templates              # The web templates like fragments, html pages etc.
    │    │    ├── fragments         # The HTML views/pages
    │    │    └── __init__.py       # The package initializer
    │    ├── __init__.py            # The package initializer
    │    ├── config.py              # The webapp's configuration file
    │    └── routes.py              # Routes of the webapp UI
    ├── .env                        # The .env file
    ├── .gitignore                  # The .gitignore file
    ├── default.env                 # The default .env file
    ├── gunicorn.conf.py            # The gunicorn configurations
    ├── README.md                   # Instructions and helpful links
    ├── requirements.txt            # The webapp's dependencies/packages
    ├── robots.txt                  # tells which URLs the search engine crawlers can access on your site
    └── wsgi.py                     # the WSGI app
```

## Local Development

### Check python settings

```shell
python3 --version
python3 -m pip --version
python3 -m ensurepip --default-pip
```

### Setup a virtual environment

```
python3 -m pip install virtualenv
python3 -m venv venv
source deactivate
source venv/bin/activate
```

**Note: -**
```source``` is Linux/MAC-OS command and doesn't work in Windows.

- Windows

```shell
venv\Scripts\activate
```

**Note: -**
The parenthesized (venv) in front of the prompt indicates that you’ve successfully activated the virtual environment.

### Install Requirements (Dependencies)

```
pip install --upgrade pip
python3 -m pip install -r requirements.txt
```

### Configuration Setup

Create or update local .env configuration file.

```shell
cp ./default.env .env
OR
touch .env

#
# App Configs
#
FLASK_ENV = development
DEBUG = False
HOST = 0.0.0.0
PORT = 8080
#
# Pool Configs
#
DEFAULT_POOL_SIZE = 1
RDS_POOL_SIZE = 1
```

### Run WebApp Application

**By default**, Flask runs the application on **port 5000**.

```shell
python wsgi.py

OR

python -m flask --app wsgi run
# http://127.0.0.1:5000/webapp

OR

python -m flask --app wsgi run --port 8080 --debug
# http://127.0.0.1:8080/webapp

OR

# Production Mode

# equivalent to 'from app import app'
gunicorn wsgi:app
# http://127.0.0.1:8000/webapp

gunicorn -w 2 'wsgi:app'
gunicorn -c gunicorn.conf.py wsgi:app
# http://127.0.0.1:8080/webapp
```

**Note**:- You can stop the development server by pressing ```Ctrl+C``` in your terminal.

### Access Flask Application

- [EWS on port 8080](http://127.0.0.1:8080/posts)
- [EWS on port 5000](http://127.0.0.1:5000/posts)

### Build Project
```shell
python3 -m build
```

### Docker Commands

- Builds the docker container image
```shell
docker build -t web-app:latest -f Dockerfile .

OR

docker build -t web-app:latest .
```

- Runs the docker container as background service
```shell
docker run --name web-app -p 8080:8080 -d web-app:latest
OR
docker run --name web-app --rm -p 8080:8080 -d web-app:latest
```

- Shows the docker container's log
```shell
docker logs -f web-app
```

- Executes the 'bash' shell in the container
```shell
docker exec -it web-app bash
```

```shell
docker stop web-app && docker container rm web-app
```

## Docker Compose

### Deploy with ```docker compose```
```shell
docker compose up -d
```

### Stop and remove the containers
```shell
docker compose down
```

### Save Requirements (Dependencies)
```shell
pip freeze > requirements.txt
```

## Testing

### Unit Tests

- How to run unit-tests?

```shell
python3 -m unittest
python -m unittest discover -s ./tests -p "test_*.py"
```


# Reference

---

- [Gunicorn](https://flask.palletsprojects.com/en/3.0.x/deploying/gunicorn/)
- [Gunicorn - WSGI server](https://docs.gunicorn.org/en/latest/index.html)
- [wsgiref — WSGI Utilities and Reference Implementation](https://docs.python.org/3/library/wsgiref.html)
- [Python Packaging User Guide](https://packaging.python.org/en/latest/)
- [Flask](https://flask.palletsprojects.com/en/3.0.x/)
- [The best Python HTTP clients](https://www.scrapingbee.com/blog/best-python-http-clients/)
- [Python Paste](https://pythonpaste.readthedocs.io/en/latest/index.html)
- [WSGI Servers](https://www.fullstackpython.com/wsgi-servers.html)
- [WSGI: The Server-Application Interface for Python](https://www.toptal.com/python/pythons-wsgi-server-application-interface)
- [Python FastAPI vs Flask: A Detailed Comparison](https://www.turing.com/kb/fastapi-vs-flask-a-detailed-comparison)



# Author

---

- Rohtash Lakra
