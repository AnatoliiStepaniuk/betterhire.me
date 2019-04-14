#!/bin/bash
scp -i "last-key-pair.pem" scripts/pull_and_run.sh ec2-user@ec2-52-28-209-82.eu-central-1.compute.amazonaws.com:/home/ec2-user/pull_and_run.sh
# .aws should not be copied. use # aws config on the instance