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

import aero.fixm.app.ffice._1.FficeTRQFficeMessageType;
import aero.fixm.base._4.UniversallyUniqueIdentifierType;
import com.boeing.filing.eurocontrol.EurocontrolXmlMapper;
import eurocontrol.cfmu.b2b.fficeservices.TrialReply;
import eurocontrol.cfmu.b2b.fficeservices.TrialRequest;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;

final class TrialSamples {

    static TrialRequest sampleRequest() {
        TrialRequest trialRequest = new TrialRequest();
        FficeTRQFficeMessageType message = new FficeTRQFficeMessageType();
        UniversallyUniqueIdentifierType universallyUniqueIdentifier = new UniversallyUniqueIdentifierType();
        universallyUniqueIdentifier.setValue("1");
        message.setUniqueMessageIdentifier(universallyUniqueIdentifier);
        trialRequest.setTrialRequestV11(message);
        return trialRequest;
    }

    static String sampleTrialReplyAsString() {
        ClassPathResource resource = new ClassPathResource("samples/trial/TrialReplyRejected.xml");
        try {
            return resource.getContentAsString(Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static TrialReply sampleTrialReply(EurocontrolXmlMapper eurocontrolXmlMapper) {
        return eurocontrolXmlMapper.read(TrialReply.class, sampleTrialReplyAsString());
    }
}
