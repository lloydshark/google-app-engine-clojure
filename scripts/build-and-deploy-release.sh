#!/bin/bash

set -e

# Load & Check Deploy Config...

source app-engine-deploy.properties

if [ "$application_id" = "PLEASE_NAME_ME" ]; then
    printf "Please set the application_id in app-engine-deploy.properties.\n\n"
    exit -1
fi

# Clean Target Directory...
printf "Cleaning the target directory...\n\n"

if [ -d "target" ]; then
  rm -r target
fi

mkdir target

# Perform a release server build...

printf "Performing the release SERVER build...\n\n"
clj -A:server:release-server:build-tools -m "appengine-war" release

# Prepare a release UI build...
printf "Performing the release UI build...\n\n"
clj -A:ui:ui-prod

# Deploy to App Engine...

printf "Setting the Application Id...\n\n"
gcloud config set project $application_id

printf "Deploying to App Engine...\n\n"
gcloud app deploy target/release/war

printf "Opening the Deployed App...\n\n"
gcloud app browse

