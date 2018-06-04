# Functionality of modules

-----------

### Project consist of 3 components

* Producer

* Consumer

* Visualization

-----------

## Producer

* reads data from file

* sends messages using `KafkaAvroSerializer` 
to the `FlightData` topic on Kafka

-----------

## Consumer

* reads data from `FlightData` topic

* uses `KafkaAvroDeserializer`

* then converts data into a GeoJSON

* sends it to `Visualizaion` topic on Kafka

-----------

## Visualization

* **consumer** - reads data from `Visualization` topic
 and sends data to browser with socket
   
* **flight visualization** - recives data from socket
 and provides live visualization on map

-----------

![flights map](images/map_screen.png)

-----------

![flights details](images/map_details_screen.png)

-----------

## Charts 

* visualizes flight delays for airlines

-----------

Every module has its definied environment - a seperate docker image defined with Dockerfile, which creates container. 

-----------

Docker-compose allows to build and run all the services together.

This solution provides containers with all required dependencies installed.
