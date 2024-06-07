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

import com.boeing.filing.base.ResourceUtil;
import com.boeing.filing.eurocontrol.EurocontrolXmlMapper;
import com.boeing.filing.file.application.view.FiledFlightPlanReplyView;
import com.boeing.filing.shared.Altitude;
import com.boeing.filing.shared.UomAltitude;
import eurocontrol.cfmu.b2b.fficeservices.FiledFlightPlanReply;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class FiledFlightPlanViewFactoryTest {

    private final EurocontrolXmlMapper eurocontrolXmlMapper = new EurocontrolXmlMapper();

    @Test
    void testAcceptable() {
        FiledFlightPlanReply filedFlightPlanReply = read("FiledFlightPlanReplyAcceptable.xml");

        FiledFlightPlanReplyView filedFlightPlanReplyView = FiledFlightPlanReplyView.of(filedFlightPlanReply);

        assertThat(filedFlightPlanReplyView)
                .hasFieldOrPropertyWithValue("requestId", "B2B_CUR:14702374")
                .hasFieldOrPropertyWithValue("status", FiledFlightPlanReplyView.Status.ACCEPTABLE)
                .hasFieldOrPropertyWithValue("explanation", null)
                .hasFieldOrPropertyWithValue("flightLevel", new Altitude(new BigDecimal("34000.0"), UomAltitude.FT))
                .hasFieldOrPropertyWithValue("routeText", "VIZAN T673 XERPA DCT LARMA/N0330A110 DCT ELVIX L975 TIDVU")
                .hasFieldOrPropertyWithValue("totalEstimatedElapsedTime", "0h 46min");
    }

    @Test
    void testObjectExists() {
        FiledFlightPlanReply filedFlightPlanReply = read("FiledFlightPlanReplyObjectExists.xml");

        FiledFlightPlanReplyView filedFlightPlanReplyView = FiledFlightPlanReplyView.of(filedFlightPlanReply);

        assertThat(filedFlightPlanReplyView)
                .hasFieldOrPropertyWithValue("requestId", "B2B_CUR:34638641")
                .hasFieldOrPropertyWithValue("status", FiledFlightPlanReplyView.Status.OBJECT_EXISTS)
                .hasFieldOrPropertyWithValue("explanation", null)
                .hasFieldOrPropertyWithValue("flightLevel", null)
                .hasFieldOrPropertyWithValue("routeText", null)
                .hasFieldOrPropertyWithValue("totalEstimatedElapsedTime", null);
    }

    @Test
    void testRejected() {
        FiledFlightPlanReply filedFlightPlanReply = read("FiledFlightPlanReplyRejected.xml");

        FiledFlightPlanReplyView filedFlightPlanReplyView = FiledFlightPlanReplyView.of(filedFlightPlanReply);

        assertThat(filedFlightPlanReplyView)
                .hasFieldOrPropertyWithValue("requestId", "B2B_CUR:71429026")
                .hasFieldOrPropertyWithValue("status", FiledFlightPlanReplyView.Status.REJECTED)
                .hasFieldOrPropertyWithValue("explanation", List.of("(R)EFPM236: ESTIMATED OFF BLOCK DATE AND TIME NOT IN THE ACCEPTABLE RANGE: 160507 TO 211707", "(R)EFPM234: ESTIMATED OFF BLOCK DATE AND TIME IS NOT WITHIN ACCEPTABLE RANGE, AFTER FILING TIME. (EOBD)", "(R)EFPM34: AIRAC DATA NOT AVAILABLE (EOBD)"))
                .hasFieldOrPropertyWithValue("flightLevel", null)
                .hasFieldOrPropertyWithValue("routeText", null)
                .hasFieldOrPropertyWithValue("totalEstimatedElapsedTime", null);
    }

    private FiledFlightPlanReply read(String filename) {
        String fileContent = ResourceUtil.readClassPathResource("samples/file/" + filename);
        return eurocontrolXmlMapper.read(FiledFlightPlanReply.class, fileContent);
    }
}
