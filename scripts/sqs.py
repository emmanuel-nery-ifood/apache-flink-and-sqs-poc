# LocalStack            https://github.com/localstack/localstack
# LocalStack Client     https://github.com/localstack/localstack-python-client
# boto3                 https://boto3.amazonaws.com/v1/documentation/api/latest/reference/services/sqs.html

import localstack_client.session
import time
import json

session = localstack_client.session.Session()
sqs = session.client('sqs')

queue = sqs.create_queue(
    QueueName = 'digester',
    Attributes = {
        'FifoQueue': 'false'
    }
)

counter = 1

while True:
    message = {
        'id':  counter,
        'name': 'Xablau',
        'age': 20
    }


    message_json = json.dumps(message)
    sqs.send_message(
        QueueUrl = queue['QueueUrl'],
        MessageBody = message_json
    )

    print('Message sent!')

    counter += 1
    time.sleep(1)
