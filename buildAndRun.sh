#!/bin/sh
mvn clean package && docker build -t it.tss/pw-secondi .
docker rm -f pw-secondi || true && docker run -d -p 8080:8080 -p 4848:4848 --name pw-secondi it.tss/pw-secondi 
