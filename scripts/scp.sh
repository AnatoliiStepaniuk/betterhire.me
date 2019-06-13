#!/bin/bash
host=52.28.237.192
scp -i "last-key-pair.pem" scripts/pull_and_run.sh ec2-user@$host:/home/ec2-user/pull_and_run.sh
scp -i "last-key-pair.pem" scripts/run.sh ec2-user@$host:/home/ec2-user/run.sh
scp -i "last-key-pair.pem" scripts/clean.sh ec2-user@$host:/home/ec2-user/clean.sh
scp -i "last-key-pair.pem" scripts/compose_restart.sh ec2-user@$host:/home/ec2-user/compose_restart.sh
scp -i "last-key-pair.pem" docker-compose.yml ec2-user@$host:/home/ec2-user/docker-compose.yml
# .aws should not be copied. use # aws config on the instance