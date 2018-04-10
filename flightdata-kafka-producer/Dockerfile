FROM maven:3.5.3-jdk-8-slim as BUILD

WORKDIR /build
COPY . /build
RUN mvn -f /build/pom.xml clean package



FROM openjdk:8u162-jre-slim 

WORKDIR /workdir/
RUN apt-get update
RUN apt-get install -y netcat
COPY --from=0 /build/target .

ENTRYPOINT while( ! nc -z -v -w5 kafka 9092 ); do sleep 5; echo "waiting"; done && while( ! nc -z -v -w5 kafka 9092 ); do sleep 5; echo "waiting"; done && java -jar flightdata-kafka-producer-1.0-jar-with-dependencies.jar
