package com.boeing.filing.functionalTests.steps;


import static com.boeing.filing.utils.JwtTokenGeneratorHelper.buildAuthorizationBearerHeader;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalSteps extends AbstractSteps {

    private final String apiVersion = "v1";
    private final String baseUri = String.format("http://localhost:8080/filing/%s", apiVersion);
    private static final Logger LOG = LoggerFactory.getLogger(GlobalSteps.class);

    @Given("the {string} API endpoint is available")
    public void theAPIEndpointIsAvailable(String endpoint) throws Exception {
        String token = buildAuthorizationBearerHeader(null, "regularuser", "password");
        RestAssured.baseURI = baseUri + endpoint;
        testContext().setToken(token);
        LOG.info(String.format("URI: %s, endpoint %s", baseUri, endpoint));
    }

    @Then("^the response status code is (\\d+)$")
    public void theResponseStatusCodeIs(int expectedStatus) {
        final int responseCode = testContext().getResponse().getStatusCode();
        assertEquals(expectedStatus, responseCode, "Invalid returned status code, status code should be: " + expectedStatus);
        LOG.info(String.format("Response code is: %s", responseCode));
    }

    @After
    public void tearDown() {
        LOG.info("------------- TEST CONTEXT TEAR DOWN -------------");
        TestContext.CONTEXT.reset();
    }
}
