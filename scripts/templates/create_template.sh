#! /bin/bash
aws ses create-template --cli-input-json file://scripts/templates/task-application-internal.json --region eu-west-1