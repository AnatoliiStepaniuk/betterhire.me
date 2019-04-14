#! /bin/bash

# TODO truncate all tables before tests!!!

# Build
mvn clean install -DskipITs && docker build -f api/Dockerfile . -t sdehunt

# Wait for application to start
containerId=$(docker run -d -v ~/.aws:/.aws -p 80:8080 sdehunt)
until grep -q 'Started Application' <<< $(docker logs $containerId); do sleep 2; done

# Tests
mvn verify

# Clean up
docker stop $(docker ps -q)
