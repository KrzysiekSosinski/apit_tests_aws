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

package com.boeing.filing.event;

import com.boeing.filing.shared.event.AwsEventController;
import com.boeing.ngfp.schema.events.FlightPlanEvent;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.services.eventbridge.EventBridgeClient;

import java.util.UUID;

import static org.mockito.Mockito.mock;

class AwsEventControllerTest {

    public static final String OPERATOR = "BOE";
    private final EventBridgeClient eventBridgeClient = mock(EventBridgeClient.class);

    @Test
    void flightPlanFiled() {

        AwsEventController awsEventController = new AwsEventController(eventBridgeClient, "nfpx");
        UUID uuid = UUID.randomUUID();
        awsEventController.flightPlanFiled(uuid, uuid, uuid.toString(), uuid, OPERATOR, "url", FlightPlanEvent.Status.COMPUTED_SUCCESSFULLY);
    }

    @Test
    void flightPlanTrialed() {
        AwsEventController awsEventController = new AwsEventController(eventBridgeClient, "nfpx");
        UUID uuid = UUID.randomUUID();
        awsEventController.flightPlanTrialed(uuid, uuid, uuid.toString(), uuid, OPERATOR, "url", FlightPlanEvent.Status.CONCUR);
    }
}