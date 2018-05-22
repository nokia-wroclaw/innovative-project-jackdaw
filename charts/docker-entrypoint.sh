#!/bin/bash

set -e

while( ! nc -z -v -w5 kafka 9092 )
do
    sleep 5
	echo "waiting for kafka"
done

while( ! nc -z -v -w5 schema-registry 8081 )
do
    sleep 5
	echo "waiting for schema-registry"
done

sleep 15

exec "$@"