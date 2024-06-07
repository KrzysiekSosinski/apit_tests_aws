package com.boeing.filing.functionalTests.steps;

import static io.restassured.RestAssured.given;

import com.boeing.filing.functionalTests.steps.helpers.FileHelper;
import io.cucumber.java.en.When;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;

public class FilingPostSteps extends AbstractSteps {

  private Response response;
  private Logger LOG = LoggerFactory.getLogger(GlobalSteps.class);

  @When("POST file request is made to the endpoint")
  public void postFileRequestIsMadeToTheEndpoint() {
    final String filePath = "src/test/resources/testData/fpc_request.json";
    String request = FileHelper.fetchJsonFileContent(filePath);

    response = given()
        .header(HttpHeaders.AUTHORIZATION, testContext().getToken())
        .contentType(ContentType.JSON)
            .queryParam("gufi", "4e9fe778-dc9f-4538-9677-7766e5747066")
        .log()
        .all()
        .body(request)
        .when().post();

    LOG.info(response.asString());
    testContext().setResponse(response);
  }
}
