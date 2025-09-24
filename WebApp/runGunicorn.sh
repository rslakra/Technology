#!/bin/bash
#Author: Rohtash Lakra
echo
gunicorn -c gunicorn.conf.py wsgi:app
#exec gunicorn --name "web-app" -c "gunicorn.conf.py" "wsgi:app"
echo
