Feature: Test suites
  Test suites must be available to be invoked

  Scenario: Listing test suites
    Given the testing application is alive
    When the list of test suites is requested
    Then more than 1 test suites are returned

