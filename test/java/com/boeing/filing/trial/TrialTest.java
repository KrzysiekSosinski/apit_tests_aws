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

package com.boeing.filing.trial;

import aero.fixm.base._4.GloballyUniqueFlightIdentifierType;
import com.boeing.filing.base.BaseIntegrationTest;
import com.boeing.filing.base.EurocontrolMock;
import com.boeing.filing.security.SecurityUtil;
import com.boeing.filing.trial.api.TrialDataResponse;
import com.boeing.filing.trial.api.TrialFpcRequest;
import com.boeing.filing.trial.api.TrialReplyViewResponse;
import com.boeing.filing.trial.application.view.TrialReplyView;
import eurocontrol.cfmu.b2b.fficeservices.TrialReply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
class TrialTest extends BaseIntegrationTest {

    @BeforeEach
    void setUp() {
        EurocontrolMock.createStub(eurocontrolServer, "samples/trial/TrialReplyRejected.xml");
    }

    @Test
    void shouldValidateManualFpc() {
        byte[] bytes = fpcRequest();

        ResponseEntity<TrialReply> response = sendManualTrial(bytes);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .ignoringFieldsOfTypes(GloballyUniqueFlightIdentifierType.class) // TODO
                .isEqualTo(TrialSamples.sampleTrialReply(eurocontrolXmlMapper));
    }

    @Test
    void shouldReturnTrialData() {
        UUID transactionId = UUID.fromString("4e9fe778-dc9f-4538-967f-7766e5747062");
        UUID gufi = UUID.fromString("4e9fe778-dc9f-4538-9677-7766e5747066");

        var trialReplyResponse = sendTrialRequest(gufi, transactionId);

        assertThat(trialReplyResponse.getStatusCode().is2xxSuccessful()).isTrue();

        var trialDataResponse = getRawTrialData(gufi, transactionId);

        assertThat(trialDataResponse.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(trialDataResponse.getBody())
                .isNotNull()
                .satisfies(it-> assertThat(it.getTrialReply())
                        .usingRecursiveComparison()
                        .ignoringFieldsOfTypes(GloballyUniqueFlightIdentifierType.class) // TODO
                        .isEqualTo(TrialSamples.sampleTrialReply(eurocontrolXmlMapper)))
                .hasFieldOrProperty("trialRequest");
    }

    @Test
    void shouldDisplayTrialView() {
        UUID transactionId = UUID.fromString("4e9fe778-dc9f-4538-967f-7766e5747062");
        UUID gufi = UUID.fromString("4e9fe778-dc9f-4538-9677-7766e5747066");

        var trialReplyResponse = sendTrialRequest(gufi, transactionId);

        assertThat(trialReplyResponse.getStatusCode().is2xxSuccessful()).isTrue();

        var trialReplyView = getTrialReplyView(gufi, transactionId);

        assertThat(trialReplyView.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(trialReplyView.getBody())
                .isNotNull()
                .hasFieldOrPropertyWithValue("requestId", "B2B_CUR:16146853")
                .hasFieldOrPropertyWithValue("status", TrialReplyView.Status.REJ)
                .hasFieldOrPropertyWithValue("explanation", List.of("(R)EFPM236: ESTIMATED OFF BLOCK DATE AND TIME NOT IN THE ACCEPTABLE RANGE: 112324 TO 171124"))
                .hasFieldOrPropertyWithValue("flightLevel", null)
                .hasFieldOrPropertyWithValue("routeText", null)
                .hasFieldOrPropertyWithValue("totalEstimatedElapsedTime", null);
    }

    @Test
    void shouldReturn404ErrorIfTrialWasNotCommissioned() {
        var randomTransactionId = UUID.randomUUID();
        UUID gufi = UUID.fromString("4e9fe778-dc9f-4538-9677-7766e5747066");

        var persistenceResponse = getRawTrialData(gufi, randomTransactionId);

        assertThat(persistenceResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldGetAndValidateFpc() {
        UUID fpcId = UUID.fromString("4e9fe778-dc9f-4538-967f-7766e5747062");
        UUID gufi = UUID.fromString("4e9fe778-dc9f-4538-9677-7766e5747066");
        var response = sendTrialRequest(gufi, fpcId);

        assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(response.getBody())
                .isNotNull()
                .hasFieldOrPropertyWithValue("requestId", "B2B_CUR:16146853")
                .hasFieldOrPropertyWithValue("status", TrialReplyView.Status.REJ)
                .hasFieldOrPropertyWithValue("explanation", List.of("(R)EFPM236: ESTIMATED OFF BLOCK DATE AND TIME NOT IN THE ACCEPTABLE RANGE: 112324 TO 171124"))
                .hasFieldOrPropertyWithValue("flightLevel", null)
                .hasFieldOrPropertyWithValue("routeText", null)
                .hasFieldOrPropertyWithValue("totalEstimatedElapsedTime", null);
    }

    @Test
    void shouldReturn404IfFpcDoesntExist() {
        UUID fpcId = UUID.nameUUIDFromBytes("NOT EXIST".getBytes(StandardCharsets.UTF_8));
        UUID gufi = UUID.fromString("4e9fe778-dc9f-4538-9677-7766e5747066");
        var response = sendTrialRequest(gufi, fpcId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnClientErrorIfInputIsInvalid() {
        byte[] input = "invalid_input".getBytes();

        ResponseEntity<TrialReply> response = sendManualTrial(input);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void shouldReturnInternalErrorIfCannotRetrieveResponseFromEurocontrol() {
        EurocontrolMock.createStub(eurocontrolServer, HttpStatus.INTERNAL_SERVER_ERROR);
        byte[] bytes = fpcRequest();

        ResponseEntity<TrialReply> response = sendManualTrial(bytes);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private byte[] fpcRequest() {
        ClassPathResource fpcResource = new ClassPathResource("samples/fpc.json");
        try {
            return Files.readAllBytes(fpcResource.getFile().toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private ResponseEntity<TrialReply> sendManualTrial(byte[] bytes) {
        HttpEntity<byte[]> httpEntity = SecurityUtil.authorized(bytes, new HttpHeaders() {{
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }});
        UUID gufi = UUID.fromString("4e9fe778-dc9f-4538-9677-7766e5747069");
        return restTemplate.exchange("/v1/trial/manual?gufi=" + gufi, HttpMethod.POST, httpEntity, TrialReply.class);
    }

    private ResponseEntity<TrialReplyViewResponse> sendTrialRequest(UUID gufi, UUID transactionId) {
        HttpEntity<?> httpEntity = SecurityUtil.authorized(new TrialFpcRequest(gufi, transactionId));
        return restTemplate.exchange("/v1/trial", HttpMethod.POST, httpEntity, TrialReplyViewResponse.class);
    }

    private ResponseEntity<TrialDataResponse> getRawTrialData(UUID gufi, UUID transactionId) {
        HttpEntity<?> httpEntity = SecurityUtil.authorized(null);
        return restTemplate.exchange("/v1/trial/raw?transactionId=" + transactionId, HttpMethod.GET, httpEntity, TrialDataResponse.class);
    }

    private ResponseEntity<TrialReplyViewResponse> getTrialReplyView(UUID gufi, UUID transactionId) {
        HttpEntity<?> httpEntity = SecurityUtil.authorized(null);
        return restTemplate.exchange("/v1/trial?gufi=" + gufi + "&transactionId=" + transactionId, HttpMethod.GET, httpEntity, TrialReplyViewResponse.class);
    }
}
