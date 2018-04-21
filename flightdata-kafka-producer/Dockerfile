FROM maven:3.5.3-jdk-8-slim as BUILD
WORKDIR /build
COPY ./pom.xml /build/pom.xml
COPY ./src/ /build/src/
RUN mvn -f /build/pom.xml clean package

FROM anapsix/alpine-java:latest
WORKDIR /workdir/
RUN apk update && apk add --update netcat-openbsd
COPY --from=BUILD /build/target/flightdata-kafka-producer-1.0-jar-with-dependencies.jar .
COPY ./docker-entrypoint.sh /workdir/docker-entrypoint.sh
ENTRYPOINT ["./docker-entrypoint.sh"]
CMD ["java", "-jar" ,"./flightdata-kafka-producer-1.0-jar-with-dependencies.jar"]
