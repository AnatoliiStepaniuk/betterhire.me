#!/bin/bash
echo REACT_APP_SERVER_URL=$(curl http://169.254.169.254/latest/meta-data/public-hostname):8080 > environment.properties
docker login
docker pull sdehuntdeveloper/sdehunt:latest
docker pull sdehuntdeveloper/sdehunt_client:latest
docker-compose down
docker-compose up -d