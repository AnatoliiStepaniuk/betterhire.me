#!/bin/bash
scp -i "last-key-pair.pem" scripts/pull_and_run.sh ec2-user@ec2-3-122-252-110.eu-central-1.compute.amazonaws.com:/home/ec2-user/pull_and_run.sh
scp -i "last-key-pair.pem" scripts/run.sh ec2-user@ec2-3-122-252-110.eu-central-1.compute.amazonaws.com:/home/ec2-user/run.sh
# .aws should not be copied. use # aws config on the instance