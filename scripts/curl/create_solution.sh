#! /bin/bash
curl -X POST -d '{"taskId":"123", "userId":"456", "score":555}' -H 'Content-Type: application/json' http://ec2-3-122-252-109.eu-central-1.compute.amazonaws.com/tasks/123/solutions