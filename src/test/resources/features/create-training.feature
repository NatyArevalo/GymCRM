@component
Feature: Create new training

  Background:
    Given a trainer with name "Jhon" and lastname "Doe"
    And the specialization is "FUNCTIONAL"
    Then the trainer should be created

    Given a trainee with name "Jane" and lastname "Smith"
    And the date of birth is "1995-05-10" and the address is "123 Main Street"
    Then the trainee is created successfully

  Scenario: Creating training for a trainer successfully
    When a training request for trainer "Jhon.Doe" by trainee "Jane.Smith" with new year "2025" and month "May" and day "1" and a duration of "60"  with training name "Leg Day" is processed
    Then a new training request should be created
    And the training should be saved and returned successfully