docker build -t kafka-consumer ../kafka-consumer/.

docker run -it --name kafka-consumer-container kafka-consumer
