/*
 * The Boeing Company. Confidential & Proprietary
 * This work contains valuable confidential and proprietary information.
 * Disclosure, use or reproduction outside of The Boeing Company
 * is prohibited except as authorized in writing.  This unpublished work
 * is protected by the laws of the United States and other countries.
 * In the event of publication, the following notice shall apply:
 *
 * Copyright (c) 2024 The Boeing Company. All Rights Reserved.
 */

package com.boeing.filing.base;

import com.boeing.filing.fpc.FpcStorage;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

@TestConfiguration(proxyBeanMethods = false)
class IntegrationTestConfiguration {

    @Bean(destroyMethod = "stop")
    WireMockServer wireMockServer() {
        WireMockServer wireMockServer = new WireMockServer(1234);
        wireMockServer.start();
        return wireMockServer;
    }

    @Bean
    Clock clock() {
        return Clock.fixed(Instant.parse("2020-12-29T12:00:00.000Z"), ZoneId.of("UTC"));
    }

    @Bean
    FpcStorage fpcStorage() {
        return new ClassPathFpcStorage();
    }

    @Bean
    AwsCredentialsProvider awsCredentialsProvider() {
        return () -> null;
    }
}
