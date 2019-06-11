#!/bin/bash
set -e
mvn clean -DskipITs install
docker build -f api/Dockerfile . -t sdehuntdeveloper/sdehunt:latest
docker push sdehuntdeveloper/sdehunt:latest
