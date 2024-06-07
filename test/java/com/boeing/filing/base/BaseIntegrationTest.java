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

import com.boeing.filing.eurocontrol.EurocontrolXmlMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = IntegrationTestConfiguration.class)
@ActiveProfiles("test")
public abstract class BaseIntegrationTest {

    @Autowired
    protected WireMockServer eurocontrolServer;

    @Autowired
    protected TestRestTemplate restTemplate;

    @Autowired
    protected EurocontrolXmlMapper eurocontrolXmlMapper;

    @AfterEach
    void cleanup() {
        eurocontrolServer.resetAll();
    }
}
