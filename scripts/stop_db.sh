#! /bin/bash

aws rds stop-db-instance --db-instance-identifier $(aws rds describe-db-instances | jq '.DBInstances' | jq -r '.[0].DBInstanceIdentifier')