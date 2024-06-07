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

package com.boeing.filing.file;

import com.boeing.filing.base.BaseIntegrationTest;
import com.boeing.filing.base.EurocontrolMock;
import com.boeing.filing.file.api.FileFpcRequest;
import com.boeing.filing.file.api.FiledFlightPlanReplyViewResponse;
import com.boeing.filing.security.SecurityUtil;
import com.boeing.filing.shared.Altitude;
import com.boeing.filing.shared.UomAltitude;
import eurocontrol.cfmu.b2b.fficeservices.FiledFlightPlanReply;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class FilingTest extends BaseIntegrationTest {

    @BeforeEach
    void setUp() {
        EurocontrolMock.createStub(eurocontrolServer, "samples/file/FiledFlightPlanReplyAcceptable.xml");
    }

    @Test
    void shouldReturnResponseFromEurocontrol() throws IOException {
        ClassPathResource fpcResource = new ClassPathResource("samples/fpc.json");
        byte[] fpcContent = Files.readAllBytes(fpcResource.getFile().toPath());

        ResponseEntity<FiledFlightPlanReply> response = sendManual(fpcContent);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                /*.usingRecursiveComparison()
                .ignoringFieldsOfTypes(GloballyUniqueFlightIdentifierType.class) // TODO
                .isEqualTo(FilingSamples.sampleTrialReply(eurocontrolXmlMapper))*/
        ;
    }

    @Test
    void shouldDisplayFiledFlightPlanReplyView() {
        UUID fpcId = UUID.fromString("4e9fe778-dc9f-4538-967f-7766e5747062");
        UUID gufi = UUID.fromString("4e9fe778-dc9f-4538-9677-7766e5747066");

        ResponseEntity<FiledFlightPlanReplyViewResponse> filedFlightPlanReplyView = getFiledFlightPlanReplyView(gufi, fpcId);

        assertThat(filedFlightPlanReplyView.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(filedFlightPlanReplyView.getBody())
                .hasFieldOrPropertyWithValue("requestId", "B2B_CUR:14702374")
                .hasFieldOrPropertyWithValue("status", FiledFlightPlanReplyViewResponse.Status.ACCEPTABLE)
                .hasFieldOrPropertyWithValue("explanation", null)
                .hasFieldOrPropertyWithValue("flightLevel", new Altitude(new BigDecimal("34000.0"), UomAltitude.FT))
                .hasFieldOrPropertyWithValue("routeText", "VIZAN T673 XERPA DCT LARMA/N0330A110 DCT ELVIX L975 TIDVU")
                .hasFieldOrPropertyWithValue("totalEstimatedElapsedTime", "0h 46min");
    }

    @Test
    void shouldGetAndFileFpc() {
        UUID fpcId = UUID.fromString("4e9fe778-dc9f-4538-967f-7766e5747062");
        UUID gufi = UUID.fromString("4e9fe778-dc9f-4538-9677-7766e5747066");

        var response = fileFpc(gufi, fpcId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .isNotNull()
                .hasFieldOrPropertyWithValue("requestId", "B2B_CUR:14702374")
                .hasFieldOrPropertyWithValue("status", FiledFlightPlanReplyViewResponse.Status.ACCEPTABLE)
                .hasFieldOrPropertyWithValue("explanation", null)
                .hasFieldOrPropertyWithValue("flightLevel", new Altitude(new BigDecimal("34000.0"), UomAltitude.FT))
                .hasFieldOrPropertyWithValue("routeText", "VIZAN T673 XERPA DCT LARMA/N0330A110 DCT ELVIX L975 TIDVU")
                .hasFieldOrPropertyWithValue("totalEstimatedElapsedTime", "0h 46min");
    }

    @Test
    void shouldReturn404IfFpcDoesntExist() {
        UUID fpcId = UUID.nameUUIDFromBytes("NOT EXIST".getBytes(StandardCharsets.UTF_8));
        UUID gufi = UUID.fromString("4e9fe778-dc9f-4538-9677-7766e5747066");

        var response = fileFpc(gufi, fpcId);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void shouldReturnClientErrorIfInputIsInvalid() {
        byte[] input = "invalid_input".getBytes();

        ResponseEntity<FiledFlightPlanReply> response = sendManual(input);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    // @Test
    void shouldReturnUnauthorizedIfAuthorizationTokenIsMissing() {
        byte[] input = "random_input".getBytes();

        ResponseEntity<FiledFlightPlanReply> response = sendManualWithoutAuth(input);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    // @Test
    void shouldReturnUnauthorizedIfAuthorizationTokenIsInvalid() {
        byte[] input = "random_input".getBytes();

        ResponseEntity<FiledFlightPlanReply> response = sendManualWithInvalidToken(input);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    private ResponseEntity<FiledFlightPlanReply> sendManualWithoutAuth(byte[] bytes) {
        HttpEntity<byte[]> httpEntity = new HttpEntity<>(bytes, new HttpHeaders() {{
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }});
        UUID gufi = UUID.fromString("4e9fe778-dc9f-4538-9677-7766e5747066");
        return fileManual(gufi, httpEntity);
    }

    private ResponseEntity<FiledFlightPlanReply> sendManualWithInvalidToken(byte[] bytes) {
        HttpEntity<byte[]> httpEntity = SecurityUtil.authorized(bytes, "xd", new HttpHeaders() {{
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }});
        UUID gufi = UUID.fromString("4e9fe778-dc9f-4538-9677-7766e5747066");
        return fileManual(gufi, httpEntity);
    }

    private ResponseEntity<FiledFlightPlanReply> sendManual(byte[] bytes) {
        HttpEntity<byte[]> httpEntity = SecurityUtil.authorized(bytes, new HttpHeaders() {{
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        }});
        UUID gufi = UUID.fromString("4e9fe778-dc9f-4538-9677-7766e5747066");
        return fileManual(gufi, httpEntity);
    }

    private ResponseEntity<FiledFlightPlanReplyViewResponse> fileFpc(UUID gufi, UUID transactionId) {
        HttpEntity<?> httpEntity = SecurityUtil.authorized(new FileFpcRequest(gufi, transactionId));
        return restTemplate.exchange("/v1/file", HttpMethod.POST, httpEntity, FiledFlightPlanReplyViewResponse.class);
    }

    private ResponseEntity<FiledFlightPlanReplyViewResponse> getFiledFlightPlanReplyView(UUID gufi, UUID transactionId) {
        HttpEntity<?> httpEntity = SecurityUtil.authorized(null);
        return restTemplate.exchange("/v1/file?transactionId=" + transactionId, HttpMethod.GET, httpEntity, FiledFlightPlanReplyViewResponse.class);
    }

    private ResponseEntity<FiledFlightPlanReply> fileManual(UUID gufi, HttpEntity<byte[]> httpEntity) {
        return restTemplate.exchange("/v1/file/manual?gufi=" + gufi, HttpMethod.POST, httpEntity, FiledFlightPlanReply.class);
    }
}
