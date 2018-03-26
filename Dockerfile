FROM openjdk:8-jre
FROM maven

WORKDIR workdir/

COPY ./kafka-consumer/. .
COPY ./kafka-consumer/pom.xml /tmp/pom.xml

RUN mvn -B -f /tmp/pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve
ENTRYPOINT mvn clean package && mvn exec:java

