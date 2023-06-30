#!/bin/sh
curl -X POST --location "http://kafka-connect:8083/connectors" \
    -H "Content-Type: application/json" -H "Accept: application/json" \
    -d @connector-config.json
