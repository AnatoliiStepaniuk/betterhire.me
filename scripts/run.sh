#! /bin/bash
docker run -v ~/.aws:/.aws -p 8080:8080 --env-file backend.env sdehuntdeveloper/sdehunt:latest