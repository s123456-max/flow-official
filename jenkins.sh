echo "清理已有容器及镜像资源"
    for container in {"gateway"}
do
    if docker ps | grep ${container} ;then
        docker stop ${container}
    fi
    if docker ps -a | grep ${container};then
        docker rm ${container}
    fi
    if docker images | grep flow/${image};then
        docker rmi flow/${image}
    fi
done