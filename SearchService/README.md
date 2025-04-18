# Search Service (explore)

Search engines, including web search engines, selection-based search engines, metasearch engines, desktop search tools, and web portals and vertical market websites have a search facility for online databases.

To build a simple web crawler in Python we need at least one library to download the HTML from a URL and another one to 
extract links. Python provides the standard libraries urllib for performing HTTP requests and ```html.parser``` for 
parsing HTML.

There are more popular libraries, such as ```Requests``` and ```Beautiful Soup```, which may provide an improved 
developer experience when composing HTTP requests and handling HTML documents.

## Project Structure
```
    /
    ├── <modules>                   # The name of the module
    ├── api                         # The API module
    │    ├── crawler                # crawler
    │    ├── indexer                # indexer
    │    ├── searcher               # searcher
    │    ├── v1                     # v1 blueprints/endpoints
    │    ├── __init__.py            # The package initializer
    │    └── routes.py              # The API routes (blueprints/endpoints)
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
    ├── .env                    # The .env file
    ├── .gitignore             # The .gitignore file
    ├── default.env            # The default .env file
    ├── gunicorn.conf.py       # The gunicorn configurations
    ├── README.md              # Instructions and helpful links
    ├── requirements.txt       # The webapp's dependencies/packages
    ├── robots.txt             # tells which URLs the search engine crawlers can access on your site
    └── wsgi.py                # the WSGI app
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
```source``` is Linux/Mac-OS command and doesn't work in Windows.

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

# Local Variables
HOST = 127.0.0.1
PORT = 8080
DEBUG = True
DEFAULT_POOL_SIZE = 1
RDS_POOL_SIZE = 1
```


### Run WebCrawler Application

**By default**, Flask runs the application on **port 5000**.


```shell
python wsgi.py

OR

python -m flask --app wsgi run
# http://127.0.0.1:5000/explore

OR

python -m flask --app wsgi run --port 8080 --debug
# http://127.0.0.1:8080/explore

OR

# Production Mode

# equivalent to 'from app import app'
gunicorn wsgi:app
# http://127.0.0.1:8000/explore

gunicorn -w 2 'wsgi:app'
gunicorn -c gunicorn.conf.py wsgi:app
# http://127.0.0.1:8080/explore
```

**Note**:- You can stop the development server by pressing ```Ctrl+C``` in your terminal.

### Access Flask Application
- [EWS on port 8080](http://127.0.0.1:8080/posts)
- [EWS on port 5000](http://127.0.0.1:5000/posts)


### Build Project
```shell
python3 -m build
```

### Save Requirements (Dependencies)
```shell
pip freeze > requirements.txt
```


## Unit Tests
```shell
python3 -m unittest
python -m unittest discover -s ./tests -p "test_*.py"
```

# Reference

#### Gunicorn

- [Gunicorn](https://flask.palletsprojects.com/en/3.0.x/deploying/gunicorn/)
- [Gunicorn - WSGI server](https://docs.gunicorn.org/en/latest/index.html)
- [wsgiref — WSGI Utilities and Reference Implementation](https://docs.python.org/3/library/wsgiref.html)
- [Python Packaging User Guide](https://packaging.python.org/en/latest/)
- [Flask](https://flask.palletsprojects.com/en/3.0.x/)
- [The best Python HTTP clients](https://www.scrapingbee.com/blog/best-python-http-clients/)
- [Web Crawler in Python: Step-by-Step Tutorial](https://www.zenrows.com/blog/explore-python)

#### WSGI Servers

- [Python Paste](https://pythonpaste.readthedocs.io/en/latest/index.html)
- [WSGI Servers](https://www.fullstackpython.com/wsgi-servers.html)
- [WSGI: The Server-Application Interface for Python](https://www.toptal.com/python/pythons-wsgi-server-application-interface)
- [Python FastAPI vs Flask: A Detailed Comparison](https://www.turing.com/kb/fastapi-vs-flask-a-detailed-comparison)

#### How to Write Unit Tests in Python

- [Part 1: Fizz Buzz](https://blog.miguelgrinberg.com/post/how-to-write-unit-tests-in-python-part-1-fizz-buzz)
- [Part 2: Game of Life](https://blog.miguelgrinberg.com/post/how-to-write-unit-tests-in-python-part-2-game-of-life)
- [Part 3: Web Applications](https://blog.miguelgrinberg.com/post/how-to-write-unit-tests-in-python-part-3-web-applications)


# Author
- Rohtash Lakra
