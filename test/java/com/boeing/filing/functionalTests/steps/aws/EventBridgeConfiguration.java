package com.boeing.filing.functionalTests.steps.aws;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.net.URI;

public class EventBridgeConfiguration {

    public static EventBridgeClient createLocalstackEventBridgeClient() {
        return EventBridgeClient
                .builder()
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("accesskey", "pass")))
                .endpointOverride(URI.create("http://localstack:4566"))
                .region(Region.US_EAST_1)
                .build();
    }

    public static SqsClient createSqsClient() {
        return SqsClient.builder()
                .endpointOverride(URI.create("http://localhost:4566"))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("accesskey", "secretkey")))
                .region(Region.US_EAST_1)
                .build();
    }

}