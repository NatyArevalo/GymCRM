@component
Feature: Delete Trainee

  Background:
    Given a trainee with name "Jhon" and lastname "Doe"
    And the date of birth is "1995-05-10" and the address is "123 Main Street"

  Scenario: Delete trainee with valid username and password
    When the trainee with username "Jhon.Doe" and valid password is deleted
    Then the trainee should be deleted successfully

  Scenario: Attempt to delete non-existing trainee
    When an attempt is made to delete a trainee with username "Jhon.Doe" and password "wrongPassword"
    Then the trainee should not be deleted
