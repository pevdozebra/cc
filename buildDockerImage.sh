#!/usr/bin/env bash

set -eo pipefail

modules=( registry-service gateway-service logging-service market-service user-service )

for module in "${modules[@]}"; do
    docker build -t "celebrity-chat/${module}:latest" ${module}
done
