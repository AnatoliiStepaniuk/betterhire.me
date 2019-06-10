#! /bin/bash
mvn clean install -DskipITs && \
docker build -f api/Dockerfile . -t sdehuntdeveloper/sdehunt:latest && \
docker run -v ~/.aws:/.aws -p 8080:8080 --env-file backend.env sdehuntdeveloper/sdehunt:latest