# charts

[![Build Status](https://travis-ci.org/nokia-wroclaw/innovative-project-jackdaw.svg?branch=master)](https://travis-ci.org/nokia-wroclaw/innovative-project-jackdaw)


Kafka consumer that visualizes flights' delay for airlines in Brazil.


### Run

Start the server: `npm start`.


### Configuration

Install all needed dependencies: `npm install`.


### Data

Assumes it will receive JSONs similar to:
```json
{
	"key": "1",
	"value": [
		"1.34",
		"Airline"
	]
}
```

Values: delay in minutes, airline/airport/airplane number/name

Key does not matter – it's not being taken into account. 
