#! /bin/bash
mvn clean install -DskipITs && \
docker build -f api/Dockerfile . -t sdehuntdeveloper/sdehunt:latest && \
docker run -v ~/.aws:/.aws -p 80:8080 sdehuntdeveloper/sdehunt:latest