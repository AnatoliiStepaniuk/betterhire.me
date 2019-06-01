#!/bin/bash
docker stop $(docker container ls -q)
docker container rm $(docker container ls a -q)
docker image rm -f $(docker image ls -q)