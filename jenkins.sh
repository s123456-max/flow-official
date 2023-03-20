#!/bin/bash
if docker ps | grep gateway; then
    docker stop gateway
    docker rm gateway
    docker rmi flow/gateway:latest
fi
if docker ps | grep auth; then
    docker stop auth
    docker rm auth
    docker rmi flow/auth:latest
fi
if docker ps | grep video; then
    docker stop video
    docker rm video
    docker rmi flow/video:latest
fi
if docker ps | grep userinfo; then
    docker stop userinfo
    docker rm userinfo
    docker rmi flow/userinfo:latest
fi
if docker ps | grep comment; then
    docker stop comment
    docker rm comment
    docker rmi flow/comment:latest
fi
