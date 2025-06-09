@integration
Feature: Full Flow Integration Test

  Scenario: Successful full flow execution
    Given a trainee "John" with lastname "Doe" date of birth "1995-05-05" and address "123 main St"
    And a trainer "Jane" with lastname "smith" and specialization "STRENGTH"
    When trainee logs in with valid password
    And a training request is created for trainer "Jhon.Doe" by trainee "Jane.Smith" with new year "2025" and month "May" and day "1" and a duration of "60"  with training name "Leg Day" is processed
    Then the training should exist for trainee "John.Doe"
