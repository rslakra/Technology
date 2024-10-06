#!/bin/bash
#Author: Rohtash Lakra
clear
set -euo pipefail
IFS=$'\n\t'
echo
echo "${JAVA_HOME}"
echo
#./mvnw clean install
#java -jar target/yatra-webapp.jar
#mvn clean package -DskipTests
mvn clean spring-boot:run
#./mvnw spring-boot:run
