#!/bin/bash
if docker ps | grep gateway; then
    docker stop gateway
    docker rm gateway
    docker rmi flow/gateway:latest
fi
