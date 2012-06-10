Feature: Hello World

  Scenario: Say Hello to the world
    Given I have a skeleton web application
    When I open the home page
    Then the page message should be "Click here to navigate to the restricted area. Username: user, password: demo."
