#!/bin/bash
# Author: Rohtash Lakra
NAME=${$1:docker-service}
clear
echo
docker exec -it $NAME
echo

