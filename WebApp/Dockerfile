# Specifies the Python base image.
#FROM public.ecr.aws/docker/library/python:3.13.1-slim-bullseye
FROM python:3.12-slim

# Arguments
ARG APP_ENV=develop

# Define Env Variables
ENV APP_ENV=$APP_ENV
ENV PYTHONDONTWRITEBYTECODE=1
ENV PYTHONUNBUFFERED=1
ENV APP_NAME=webapp

LABEL maintainer="Rohtash Lakra <work.lakra@gmail.com>"

# Sets the working directory inside the Docker container at `/app`
WORKDIR /app

# Copies data to the `/app` directory of the Docker container
COPY . .

RUN chmod +x runGunicorn.sh

# Installs the Python dependencies specified in the `requirements.txt` file.
RUN pip install --upgrade pip && \
    pip install -r requirements.txt --no-cache-dir

# Set logging directory for gunicorn
ENV ACCESS_LOG="/var/log/gunicorn/access.log"
ENV ERROR_LOG="/var/log/gunicorn/error.log"

# EXPOSE is for documentation only - does not do anything
# 8080 is the default port the server is listening on
EXPOSE 8080

RUN mkdir /var/log/gunicorn
# Run Flask app
# python -m flask --app wsgi run --port 8080 --debug
# If needs override answers Yes, use CMD else ENTRYPOINT
#CMD ["python", "-m", "flask", "--app", "wsgi", "run", "--port=8080", "--host=0.0.0.0"]
#ENTRYPOINT ["python", "-m", "flask", "--app", "wsgi", "run", "--port=8080", "--host=0.0.0.0"]

# Run Gunicorn app
# gunicorn -c gunicorn.conf.py wsgi:app
# gunicorn --name "gunicorn" -c gunicorn.conf.py wsgi:app
# gunicorn --bind "0.0.0.0:8080" -c gunicorn.conf.py wsgi:app
# If needs override answers Yes, use CMD else ENTRYPOINT
CMD ["gunicorn", "-c", "gunicorn.conf.py", "wsgi:app"]
#ENTRYPOINT ["gunicorn", "-c", "gunicorn.conf.py", "wsgi:app"]
#CMD ["gunicorn", "--bind", "0.0.0.0:8080", "-c", "gunicorn.conf.py", "wsgi:app"]
#ENTRYPOINT ["gunicorn", "--bind", "0.0.0.0:8080", "-c", "gunicorn.conf.py", "wsgi:app"]
#ENTRYPOINT ["/bin/sh", "-c", "./runGunicorn.sh"]
