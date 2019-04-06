#!/bin/bash
docker container rm $(docker container ls -la -q)
docker image rm -f $(docker image ls -q)
docker login
docker pull sdehuntdeveloper/sdehunt:latest
docker run -v ~/.aws:/.aws -p 80:8080 sdehuntdeveloper/sdehunt