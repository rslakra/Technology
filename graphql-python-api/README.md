# graphql-python-api

---

The ```graphql-python-api``` represents an internal web service for the graphql-python-api project.

## Project Structure

Although this layout is pretty straightforward, it has several drawbacks that arise as the app complexity increases. 
For example, it will be hard for you to reuse the application logic in other projects because all the functionality is 
bundled in ```webapp/__init__.py```. If you split this functionality into modules instead, then you could reuse complete modules 
across different projects.

```
    /
    ├── ews                                 # An external web-service
    ├── iws                                 # An internal web-service
    │    ├── account                        # an account’s rest service.
    │    │    ├── v1                        # contains v1 blueprints/api resources.
    │    │    │    ├── routes               # contains the routes of api resource.
    │    │    ├── v2                        # contains v2 blueprints/api resources.
    │    │    │    ├── routes               # contains the routes of api resource.
    │    │    ├── __init__.py               # The package initializer
    │    │    ├── models.py                 # The model objects
    │    │    ├── README.md                 # Instructions and helpful links
    │    │    └── _routes.py                # The package initializer
    │    ├── api                            # The API service
    │    ├── common                         # contains the definition of the common’s models.
    │    ├── framework                      # contains the definition of the framework’s models.
    │    ├── logger                         # The logger module
    │    ├── tests                          # The unit-tests module of an application
    │    ├── webapp                         # contains the definition of the webapp’s module.
    │    │    ├── static                    # The static contents like css, js etc.
    │    │    │    ├── css                  # contains css files
    │    │    │    ├── images               # contains image files
    │    │    │    ├── js                   # contains JavaScript files
    │    │    ├── templates                 # contains the application’s templates.
    │    │    │    ├── fragments            # contains the reusable fragments of web views.
    │    │    │    │    ├── _base.html      # base/parent contents of html files
    │    │    │    │    ├── _footer.html    # footer's contents
    │    │    │    │    └── _navigation.py  # navigation menu links
    │    │    │    └── __init__.py          # The package initializer
    │    │    ├── __init__.py               # The package initializer
    │    │    ├── app.py                    # The WSGI web application logic.
    │    │    ├── app.py                    # contains the web application logic.
    │    │    ├── routes.py                 # contains the definition of the web’s routes.
    │    ├── __init__.py                    # The package initializer
    │    ├── config.py                      # contains the application configuration parameters.
    │    └── README.md                      # The README file of ews module
    ├── README.md                           # Instructions and helpful links
    └── robots.txt                          # tells which URLs the search engine crawlers can access on your site
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


- Install Flask

**Note**: - Only if you didn't install the 'requirements.txt' file.

```shell
python3 -m pip install Flask
```


### Configuration Setup

Create or update local .env configuration file.

```shell
cp ./iws/default.env .env
OR
touch .env

#
# App Configs
#
FLASK_ENV = development
DEBUG = False
HOST = 127.0.0.1
PORT = 8080
#
# Pool Configs
#
DEFAULT_POOL_SIZE = 1
RDS_POOL_SIZE = 1
#
# Logger Configs
#
LOG_FILE_NAME = 'iws.log'
#
# Database Configs
#
DB_HOSTNAME = 127.0.0.1
DB_PORT =
DB_NAME = posts
DB_USERNAME = posts
DB_PASSWORD = Password
```


### Run IWS Flask Application

**By default**, Flask runs the application on **port 5000**.


```shell
python wsgi.py

OR

#flask --app wsgi run
python -m flask --app wsgi run
# http://127.0.0.1:5000/posts

OR

python -m flask --app wsgi run --port 8080 --debug
# http://127.0.0.1:8080/posts

OR

# Production Mode

# equivalent to 'from app import app'
gunicorn wsgi:app
# gunicorn -w <n> 'wsgi:app'
gunicorn -w 2 'wsgi:app'
# http://127.0.0.1:8000/posts

gunicorn -c gunicorn.conf.py wsgi:app
# http://127.0.0.1:8080/posts

```

**Note**:- You can stop the development server by pressing ```Ctrl+C``` in your terminal.

### Access Flask Application
- [IWS on port 8080](http://127.0.0.1:8080/posts)
- [IWS on port 8000](http://127.0.0.1:8000/posts)
- [IWS on port 5000](http://127.0.0.1:5000/posts)


### Build Project
```shell
python3 -m build
```


### Docker Commands

- Builds the docker container image
```shell
docker build -t posts-iws:latest -f Dockerfile .

OR

docker build -t posts-iws:latest .
```

- Runs the docker container as background service
```shell
docker run --name posts-iws -p 8080:8080 -d posts-iws:latest
OR
docker run --name posts-iws --rm -p 8080:8080 -d posts-iws:latest
```

- Shows the docker container's log
```shell
docker logs -f posts-iws
```

- Executes the 'bash' shell in the container
```shell
docker exec -it posts-iws bash
```

```shell
docker stop posts-iws && docker container rm posts-iws
```


### Save Requirements (Dependencies)
```shell
pip freeze > requirements.txt
```


## Testing


### Unit Tests
```shell
python3 -m unittest
python -m unittest discover -s ./tests -p "test_*.py"
```

### Performance Testing
```shell
# Run this in a separate terminal
# so that the load generation continues and you can carry on with the rest of the steps
kubectl run -i --tty load-generator --rm --image=busybox:1.28 --restart=Never -- /bin/sh -c "while sleep 0.01; do wget -q -O- http://php-apache; done"
```


# Reference

- [Build a Scalable Flask Web Project From Scratch](https://realpython.com/flask-project/)
- [Gunicorn - WSGI server](https://docs.gunicorn.org/en/latest/index.html)
- [Python Packaging User Guide](https://packaging.python.org/en/latest/)
- [The Twelve Factors App](https://12factor.net/)
- [werkzeug examples](https://github.com/pallets/werkzeug/tree/main/examples)
- [Gunicorn Settings](https://docs.gunicorn.org/en/stable/settings.html)

### Logger Guide
- [Logging HOWTO](https://docs.python.org/3/howto/logging.html)

### Documentation
- [Using GraphQL with Python – A Complete Guide](https://www.apollographql.com/blog/complete-api-guide)


### Docker


# Author
- Rohtash Lakra
