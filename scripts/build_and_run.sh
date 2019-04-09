#! /bin/bash
mvn clean install -DskipITs
docker build -f api/Dockerfile . -t sdehunt
docker run -v ~/.aws:/.aws -p 80:8080 sdehunt