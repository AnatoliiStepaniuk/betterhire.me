#!/bin/bash
docker login
docker pull sdehuntdeveloper/sdehunt:latest
docker pull sdehuntdeveloper/sdehunt_client:latest
docker-compose up -d