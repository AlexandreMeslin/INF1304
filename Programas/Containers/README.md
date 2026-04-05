Comandos interessantes:

```bash
$ docker ps –a
$ docker stop prog_web
$ docker rm prog_web
$ docker exec -it prog_web /bin/bash
$ docker logs -f consumer_service_1
$ docker inspect chat-app_consumer-service_1
$ docker volume ls
$ docker volume inspect chat-app-cluster_kafka3_logs
$ docker image prune -a
$ docker images
$ docker rmi static-site_web
$ docker rmi `docker images -q`
```
