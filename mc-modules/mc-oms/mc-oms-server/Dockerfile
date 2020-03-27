FROM openjdk:8u212-jre
ADD target/mc-oms-server-1.0-SNAPSHOT.jar /mc-app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/mc-app.jar"]