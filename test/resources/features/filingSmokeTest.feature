@SmokeTest
Feature: Smoke testing the filing endpoints

  As a user of the filing service
  I want to verify that the endpoints return a HTTP status code of 200
  So that I can ensure the endpoint is accessible and functioning correctly


  Scenario: Verify HTTP status code for file endpoint
    Given the "/file/manual" API endpoint is available
    When POST file request is made to the endpoint
    Then the response status code is 200


  Scenario: Verify HTTP status code for file endpoint
    Given the "/file/manual" API endpoint is available
    When POST file request is made to the endpoint
    Then Event is received by Event Bridge