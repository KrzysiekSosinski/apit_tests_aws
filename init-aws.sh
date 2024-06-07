#!/bin/bash

# Define the region
REGION=us-east-1


awslocal events create-event-bus --name nfpx --region $REGION


TOPIC_ARN=$(awslocal sns create-topic --name nfpx-topic --query 'TopicArn' --output text --region $REGION)


QUEUE_URL=$(awslocal sqs create-queue --queue-name nfpx-queue --query 'QueueUrl' --output text --region $REGION)


QUEUE_ARN=$(awslocal sqs get-queue-attributes --queue-url $QUEUE_URL --attribute-name QueueArn --query 'Attributes.QueueArn' --output text --region $REGION)


awslocal events put-rule --name nfpx-rule --event-bus-name nfpx --event-pattern '{"source": ["my.custom.source"]}' --region $REGION


awslocal events put-targets --rule nfpx-rule --event-bus-name nfpx --targets "Id"="1","Arn"="$TOPIC_ARN" --region $REGION


awslocal events put-targets --rule nfpx-rule --event-bus-name nfpx --targets "Id"="2","Arn"="$QUEUE_ARN" --region $REGION


echo "EventBus: nfpx"
echo "EventBridge Rule: nfpx-rule"
echo "SNS Topic ARN: $TOPIC_ARN"
echo "SQS Queue URL: $QUEUE_URL"
echo "SQS Queue ARN: $QUEUE_ARN"
