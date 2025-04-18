# webapp

---

The ```webapp``` contains the python web server.


## Folder Structure Conventions
```
    /
    ├── webapp                      # The web server
    |    ├── static                 # The static contents like css, js etc.
    |    ├── templates              # The web templates like fragments, html pages etc.
    |    ├── tests                  # The tests of the webapp
    |    └── .env                   # The .env file
    |    └── __init__.py            # The webapp initializer
    |    └── app.py                 # The app class
    |    └── config.py              # The webapp's configuration file
    |    └── README.md              # Instructions and helpful links
    |    └── requirements.txt       # The webapp's dependencies/packages
    |    └── routes.py              # Routes of the webapp UI
    └── README.md
```

## Project Setup
```shell
python3 -m pip install virtualenv
python3 -m venv venv
source venv/bin/activate
pip install --upgrade pip
python3 -m pip install -r requirements.txt 
```


## Running Application
```shell
python app.py

OR

python -m flask --app app run --port 8080 --debug

OR

# Production Mode

# equivalent to 'from app import app'
gunicorn app:app
# http://127.0.0.1:8000/webapp/
gunicorn -w 2 'app:app'
gunicorn -w 4 'app:app'
gunicorn -c gunicorn_config.py app:app
# http://0.0.0.0:8080/webapp/
```


# Reference
- [Gunicorn](https://flask.palletsprojects.com/en/3.0.x/deploying/gunicorn/)
- [Python Paste](https://pythonpaste.readthedocs.io/en/latest/index.html)
- [WSGI Servers](https://www.fullstackpython.com/wsgi-servers.html)
- [wsgiref — WSGI Utilities and Reference Implementation](https://docs.python.org/3/library/wsgiref.html)
- [WSGI: The Server-Application Interface for Python](https://www.toptal.com/python/pythons-wsgi-server-application-interface)
- [Flask](https://flask.palletsprojects.com/en/3.0.x/)
- [Python FastAPI vs Flask: A Detailed Comparison](https://www.turing.com/kb/fastapi-vs-flask-a-detailed-comparison)
- 


# Author

---

- Rohtash Lakra
