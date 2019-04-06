#! /bin/bash
mvn clean package && docker build . -t sdehunt && docker run -v ~/.aws:/.aws -p 80:8080 sdehunt