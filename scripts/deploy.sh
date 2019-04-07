#!/bin/bash
cd api
mvn clean package
cd ..
docker build -f api/Dockerfile . -t sdehuntdeveloper/sdehunt:latest
docker push sdehuntdeveloper/sdehunt:latest
