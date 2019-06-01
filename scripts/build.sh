#! /bin/bash
mvn clean install -DskipITs && \
docker build -f api/Dockerfile . -t sdehuntdeveloper/sdehunt:latest