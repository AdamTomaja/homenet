#!/bin/sh
java -jar target/homenet-server.jar & echo $! > ./pid.file &