#!/bin/bash

## create queue
queue=$(aws --endpoint-url=http://localhost:4566 sqs create-queue --queue-name teste)

## get queueUrl
queueUrl=$(jq -r '.QueueUrl' <<< ${queue})
echo "Fila criada com a url: ${queueUrl}"


counter=1

while true
do
    aws --endpoint-url=http://localhost:4566 sqs send-message --queue-url "$queueUrl" --message-body "${counter}, Uma mensagem legal"

    sleep 1
    counter=$((counter + 1))

done