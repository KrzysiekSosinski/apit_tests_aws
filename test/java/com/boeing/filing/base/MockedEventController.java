package com.boeing.filing.base;

import com.boeing.filing.shared.event.EventController;
import com.boeing.ngfp.schema.events.FlightPlanEvent;

import java.util.UUID;

public class MockedEventController implements EventController {
    @Override
    public void flightPlanFiled(UUID gufi, UUID flightPlanId, String ccid, UUID originalFlightPlanId, String operator, String flightPlanUrl, FlightPlanEvent.Status status) {

    }

    @Override
    public void flightPlanTrialed(UUID gufi, UUID flightPlanId, String ccid, UUID originalFlightPlanId, String operator, String flightPlanUrl, FlightPlanEvent.Status status) {

    }
}
