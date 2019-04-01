#!/bin/bash
mvn clean package
docker build . -t sdehunt:latest
docker push sdehuntdeveloper/sdehunt:latest
