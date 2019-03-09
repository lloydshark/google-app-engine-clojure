#!/bin/bash

set -e

printf "Performing a dev UI build...\n\n"
clj -A:ui:ui-dev
