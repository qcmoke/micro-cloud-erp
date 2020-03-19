FROM openjdk:8u212-jre
#存放持久化数据的目录
VOLUME /tmp
#要复制到容器中的问件
ADD target/mc-pms-server-1.0-SNAPSHOT.jar /mc-pms-server.jar

#指定时区
RUN ln -sf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

#docker容器启动时执行的命令
#-c为清除以前启动的数据

#ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=prod","-jar","/mc-pms-server.jar","-c"]
#这种可以在启动时根据ENTRYPOINT参数写死spring.profiles.active，对应的启动命令如下：
#docker run -d --name mc-pms-server-1  -p 8030:8030 mc-pms-server

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mc-pms-server.jar","-c"]
#这种可以以在启动时指定spring.profiles.active，对应的启动命令如下：
#docker run -d --name mc-pms-server-1 -e "SPRING_PROFILES_ACTIVE=prod"  -p 8030:8030 mc-pms-server
#如果只是想测试可以设置--rm选项，等价于在容器退出后，执行docker rm -v（-v :删除与容器关联的卷）。这样在容器退出时就能删除容器并删除关联的卷。注意：--rm选项不能与-d同时使用
#docker run  --name mc-pms-server-1 -e "SPRING_PROFILES_ACTIVE=prod"  -p 8030:8030  --rm mc-pms-server

#启动端口号(容器中的)
EXPOSE 8030