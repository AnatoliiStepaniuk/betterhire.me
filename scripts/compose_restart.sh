#!/bin/bash
docker login
docker pull sdehuntdeveloper/sdehunt:latest
docker pull sdehuntdeveloper/sdehunt_client:latest
docker-compose restart
docker-compose up -d