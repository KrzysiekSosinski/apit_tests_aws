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

import com.github.tomakehurst.wiremock.WireMockServer;
import org.apache.commons.io.Charsets;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static com.boeing.filing.base.ResourceUtil.readClassPathResource;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;

public class EurocontrolMock {

    private static final String ENDPOINT = "/B2B_PREOPS/gateway/spec/27.0.0";

    public static void createStub(WireMockServer wireMockServer, String responseBodyPath) {
        wireMockServer
            .stubFor(post(ENDPOINT)
                .willReturn(
                    aResponse()
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE)
                        .withBody(readClassPathResource(responseBodyPath))
                ));
    }

    public static void createStub(WireMockServer wireMockServer, HttpStatusCode httpStatusCode) {
        wireMockServer
            .stubFor(post(ENDPOINT)
                .willReturn(
                    aResponse()
                        .withStatus(httpStatusCode.value())
                        .withBody(":(")
                ));
    }
}
