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

import com.boeing.filing.base.ResourceUtil;
import com.boeing.filing.eurocontrol.EurocontrolXmlMapper;
import com.boeing.filing.shared.Altitude;
import com.boeing.filing.shared.UomAltitude;
import com.boeing.filing.trial.application.view.TrialReplyView;
import eurocontrol.cfmu.b2b.fficeservices.TrialReply;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.boeing.filing.trial.application.view.TrialReplyView.*;
import static org.assertj.core.api.Assertions.assertThat;

class TrialReplyViewTest {

    private final EurocontrolXmlMapper eurocontrolXmlMapper = new EurocontrolXmlMapper();

    @Test
    void testConcur() {
        TrialReply trialReply = read("TrialReplyConcur.xml");

        TrialReplyView trialReplyView = TrialReplyView.of(trialReply);

        assertThat(trialReplyView)
                .hasFieldOrPropertyWithValue("requestId", "B2B_CUR:28132174")
                .hasFieldOrPropertyWithValue("status", Status.CONCUR)
                .hasFieldOrPropertyWithValue("explanation", null)
                .hasFieldOrPropertyWithValue("flightLevel", null)
                .hasFieldOrPropertyWithValue("routeText", null)
                .hasFieldOrPropertyWithValue("totalEstimatedElapsedTime", null);
    }

    @Test
    void testNegotiate() {
        TrialReply trialReply = read("TrialReplyNegotiate.xml");

        TrialReplyView trialReplyView = TrialReplyView.of(trialReply);

        assertThat(trialReplyView)
                .hasFieldOrPropertyWithValue("requestId", "B2B_CUR:3606460")
                .hasFieldOrPropertyWithValue("status", Status.NEGOTIATE)
                .hasFieldOrPropertyWithValue("explanation", null)
                .hasFieldOrPropertyWithValue("flightLevel", new Altitude(BigDecimal.valueOf(31000.0), UomAltitude.FT))
                .hasFieldOrPropertyWithValue("routeText", "IDRID L980 MANGO L620 SAM M195 LORKU DCT ERWAN DCT BALDA DCT ABUPI DCT XAMAX")
                .hasFieldOrPropertyWithValue("totalEstimatedElapsedTime", "2h 34min");
    }

    @Test
    void testNonConcur() {
        TrialReply trialReply = read("TrialReplyNonConcur.xml");

        TrialReplyView trialReplyView = TrialReplyView.of(trialReply);

        assertThat(trialReplyView)
                .hasFieldOrPropertyWithValue("requestId", "B2B_CUR:2735339")
                .hasFieldOrPropertyWithValue("status", Status.NON_CONCUR)
                .hasFieldOrPropertyWithValue("explanation", List.of(
                        "PROF323: TRAJECTORY INFO ERROR (DERIVED TAXI TIME 00:00:00 IS NOT IN RANGE 00:01:00 TO 01:30:00)",
                        "(R)PROF205: RS: TRAFFIC VIA EDDB IS OFF MANDATORY ROUTE REF:[YX2019A] EDYYUTA",
                        "(R)PROF204: RS: TRAFFIC VIA MIMVA IS ON FORBIDDEN ROUTE REF:[EG2056A] MIMVA",
                        "(R)PROF204: RS: TRAFFIC VIA BABRA OGTUG RAMOX UNDUX IS ON FORBIDDEN ROUTE REF:[EG2817A] PENIL M144 BAGSO ONLY AVAILABLE",
                        "(R)ROUTE135: THE SID LIMIT IS EXCEEDED FOR AERODROME EDDB [EDDB5D] CONNECTING TO GENTI.",
                        "(R)ROUTE134: THE STAR LIMIT IS EXCEEDED FOR AERODROME EIDW [EIDW5A] CONNECTING TO SOPAX."
                ))
                .hasFieldOrPropertyWithValue("flightLevel", new Altitude(BigDecimal.valueOf(43000.0), UomAltitude.FT))
                .hasFieldOrPropertyWithValue("routeText", "DCT GENTI P203 HLZ L980 RKN L602 NALAX L46 LIBSO UL975 SONEX L60 PENIL M144 UNDUX L70 SOPAX DCT")
                .hasFieldOrPropertyWithValue("totalEstimatedElapsedTime", "1h 46min");
    }

    @Test
    void testRejection() {
        TrialReply trialReply = read("TrialReplyRejected.xml");

        TrialReplyView trialReplyView = TrialReplyView.of(trialReply);

        assertThat(trialReplyView)
                .hasFieldOrPropertyWithValue("requestId", "B2B_CUR:16146853")
                .hasFieldOrPropertyWithValue("status", Status.REJ)
                .hasFieldOrPropertyWithValue("explanation", List.of("(R)EFPM236: ESTIMATED OFF BLOCK DATE AND TIME NOT IN THE ACCEPTABLE RANGE: 112324 TO 171124"))
                .hasFieldOrPropertyWithValue("flightLevel", null)
                .hasFieldOrPropertyWithValue("routeText", null)
                .hasFieldOrPropertyWithValue("totalEstimatedElapsedTime", null);
    }

    private TrialReply read(String filename) {
        String fileContent = ResourceUtil.readClassPathResource("samples/trial/" + filename);
        return eurocontrolXmlMapper.read(TrialReply.class, fileContent);
    }
}
