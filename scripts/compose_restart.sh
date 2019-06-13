#!/bin/bash
docker login
docker pull sdehuntdeveloper/sdehunt:latest
docker pull sdehuntdeveloper/sdehunt_client:latest
docker-compose down
docker-compose up -d