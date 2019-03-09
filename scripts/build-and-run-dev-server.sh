#!/bin/bash

set -e

# Clean Target Directory...

printf "Cleaning the target directory...\n\n"
if [ -d "target" ]; then
  rm -r target
fi

mkdir target

# Prepare a Dev build

printf "Performing a dev server build...\n\n"
clj -A:server:dev-server:build-tools -m "appengine-war" dev

# Run the Dev server

printf "\nStarting the dev server...\n\n"
java_dev_appserver.sh target/dev/war

