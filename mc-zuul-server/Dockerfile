FROM openjdk:8u212-jre

#存放持久化数据的目录
VOLUME /tmp
#要复制到容器中的问件
ADD target/mc-zuul-server-1.0-SNAPSHOT.jar /mc-zuul-server.jar
#docker容器启动时执行的命令
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mc-zuul-server.jar"]
#启动端口号(容器中的)
EXPOSE 9070