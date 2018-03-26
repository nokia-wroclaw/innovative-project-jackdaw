FROM openjdk:8-jre
FROM maven

WORKDIR workdir/

COPY . .
COPY ./pom.xml /tmp/pom.xml

RUN mvn -B -f /tmp/pom.xml -s /usr/share/maven/ref/settings-docker.xml dependency:resolve
RUN apt-get update
RUN apt-get install -y netcat

ENTRYPOINT mvn clean package && while( ! nc -z -v -w5 kafka 9092 ); do sleep 5; echo "waiting"; done && mvn exec:java


