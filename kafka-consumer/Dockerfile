FROM jamesdbloom/docker-java8-maven

WORKDIR workdir/

COPY . .

RUN apt-get update
RUN apt-get install -y netcat

ENTRYPOINT mvn clean package && while( ! nc -z -v -w5 kafka 9092 ); do sleep 5; echo "waiting"; done && mvn exec:java

