package com.boeing.filing.functionalTests.steps;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public enum TestContext {

  CONTEXT;

  private RequestSpecification request;
  private String token;
  private String endpoint;
  private Response response;
  private Object payload;

  public RequestSpecification getRequest() {
    if (request == null) {
      request = given().log().all();
    }
    return request;
  }

  public void setRequest(RequestSpecification request) {
    this.request = request;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getEndpoint() {
    return endpoint;
  }

  public void setEndpoint(String endpoint) {
    this.endpoint = endpoint;
  }

  public Response getResponse() {
    return response;
  }

  public void setResponse(Response response) {
    this.response = response;
  }

  public Object getPayload() {
    return payload;
  }

  public <T> T getPayload(Class<T> clazz) {
    return clazz.cast(payload);
  }

  public void setPayload(Object payload) {
    this.payload = payload;
  }

  public void reset() {
    request = null;
    token = null;
    endpoint = null;
    response = null;
    payload = null;
  }
}
