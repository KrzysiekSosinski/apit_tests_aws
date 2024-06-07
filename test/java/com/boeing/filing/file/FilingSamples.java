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

import com.boeing.filing.eurocontrol.EurocontrolXmlMapper;
import eurocontrol.cfmu.b2b.fficeservices.FiledFlightPlanReply;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.Charset;

class FilingSamples {

    static String sampleFiledFlightPlanReplyAsString() {
        ClassPathResource resource = new ClassPathResource("samples/file/FiledFlightPlanReplyAcceptable.xml");
        try {
            return resource.getContentAsString(Charset.defaultCharset());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static FiledFlightPlanReply sampleTrialReply(EurocontrolXmlMapper eurocontrolXmlMapper) {
        return eurocontrolXmlMapper.read(FiledFlightPlanReply.class, sampleFiledFlightPlanReplyAsString());
    }
}
