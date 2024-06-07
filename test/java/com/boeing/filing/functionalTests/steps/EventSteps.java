package com.boeing.filing.functionalTests.steps;

import com.boeing.filing.functionalTests.steps.aws.EventBridgeConfiguration;
import io.cucumber.java.en.Then;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

public class EventSteps extends AbstractSteps{

//    private EventBridgeClient eventBridgeClient;

    @Then("Event is received by Event Bridge")
    public void eventIsReceivedByEventBridge() {
//        eventBridgeClient = new EventBridgeConfiguration().createLocalstackEventBridgeClient();
        ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                .queueUrl("http://localhost:4566/000000000000/nfpx-queue")
                .maxNumberOfMessages(10)
                .build();

        SqsClient sqsClient = EventBridgeConfiguration.createSqsClient();

        List<Message> messages = sqsClient.receiveMessage(receiveMessageRequest).messages();
    }
}
