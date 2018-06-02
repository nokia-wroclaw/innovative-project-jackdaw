# innovative-project-jackdaw

[![Build Status](https://travis-ci.org/nokia-wroclaw/innovative-project-jackdaw.svg?branch=master)](https://travis-ci.org/nokia-wroclaw/innovative-project-jackdaw)


## Table of contents
1. Project goals
2. Technologies
3. Functionality of modules
4. Installation
5. Contributing


## 1. Project goals
The goal is to create PoC of platform for streaming calculation using Kafka and Docker in microservices architecture. This platform will allow to perform Big Data Calculation in Streaming mode. As a final product there will be created a web application responsible for data visualization.

As an example of dataset we used one that represents Flights in Brasil from www.kaggle.com.


## 2. Technologies
  * Java
  * Docker
  * JavaScript
  	* Leaflet - https://leafletjs.com
  	* jQuery - https://jquery.com
  	* socket.io - https://socket.io
  * Kafka
  	* Confluent Schemat Registry - https://docs.confluent.io


## 3. Functionality of modules
This project consist of three main modules:
* Producer - reads data from file and sends messages using KafkaAvroSerializer to the 'FlightData' topic on Kafka
* Consumer - reads data from 'FlightData' topic, uses KafkaAvroDeserializer, then converts data into a geoJSON and sends it to 'Visualizaion' topic on Kafka
* Visualization
	* consumer - reads data from 'Visualization' topic and constantly sends data with socket
	* flight visualization - recives data with socket and provides live visualization on map
* Charts - visualizes flights' delay for airlines


## 4. Installation
### Preperaing Docker environment
First you have to have Docker installed on your system - https://docs.docker.com/install/
Then run docker or create and start docker machine.

### Clone repository
Open a command prompt and use the following commands to clone repository and navigate to project directory:
```
git clone https://github.com/nokia-wroclaw/innovative-project-jackdaw.git
cd innovative-project-jackdaw
```
### Run
Run docker-compose
```
docker-compose up
```
or 
```
./run.sh
```
All needed dependencies will be installed automatically through docker-compose.

## Stop
```
docker-compose down
```

or in order to clear Jackdaw images:

```
./clear.sh
```

If everything went right you can access:
* map visualization at localhost:3001
* pie charts visualization at localhost:3000


## 5. Contributing
Students
* Anna Antończak
* Kacper Bieganek
* Krzysztof Buczak
* Hanna Grodzicka
* Mateusz Najda

Nokia Supervisors
* Paweł Ślawski
* Dawid Rutowicz
* Damian Czaja
* Ewa Kaczmarek
