#!/bin/bash
bash clean.sh
docker login
docker pull sdehuntdeveloper/sdehunt:latest
docker run -v ~/.aws:/.aws -p 80:8080 sdehuntdeveloper/sdehunt:latest