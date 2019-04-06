#!/bin/bash
mvn clean package
docker build . -t sdehuntdeveloper/sdehunt:latest
docker push sdehuntdeveloper/sdehunt:latest
