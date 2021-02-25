# LocalStack            https://github.com/localstack/localstack
# LocalStack Client     https://github.com/localstack/localstack-python-client
# boto3                 https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/sqs.html

import localstack_client.session
import time
import json
import random

session = localstack_client.session.Session()
sqs = session.client('sqs')

queue = sqs.create_queue(
    QueueName = 'digester',
    Attributes = {
        'FifoQueue': 'false'
    }
)

counter = 1

types = ['Type1','Type2','Type2']

while True:
    message = {
        'id':  counter,
        'name': 'Xablau',
        'age': 20,
        'type': random.choice(types)
    }


    message_json = json.dumps(message)
    sqs.send_message(
        QueueUrl = queue['QueueUrl'],
        MessageBody = message_json
    )

    print(f'Message sent! {counter}')

    counter += 1
#     time.sleep(1)
