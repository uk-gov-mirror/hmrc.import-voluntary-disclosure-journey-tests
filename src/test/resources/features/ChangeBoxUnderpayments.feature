@all @box_underpayments

@ZAP
Feature: Check the change flow of the Box amendments reasons journey

  Scenario: A logged in user is able to walk through the Box 22 entry level underpayment flow, opt to change to box 33,
    and then opt to change back to box 22
    Given I want to complete section 1: Service Entry
    And I want to complete section 2: Entry Details as Importer
    And I want to complete section 3: Underpayments with Customs Duty~Import VAT~Excise Duty
    Then the user should be on the 'You must tell us the reason for the underpayment' page
    When clicks the Continue button
    Then the user should be on the 'What is the box number?' page
    And the user enters 22 into the Box Number input field
    When clicks the Continue button
    Then the user should be on the 'Box 22 invoice currency and total amount invoiced amendment' page
    And the user enters EUR125.00 into the Original value input field
    And the user enters GBP190.50 into the Amended value input field
    When clicks the Continue button
    Then the user should be on the 'Confirm the details of the reason for underpayment' page
    And I check that within the Underpayment box summary that the Entry level value of Box number is 22
    And I check that within the Underpayment box summary that the Entry level value of Original value is EUR125.00
    And I check that within the Underpayment box summary that the Entry level value of Amended value is GBP190.50
    When the user clicks the 1st change link for Box number on the Underpayment box summary page
    Then the user should be on the 'What is the box number?' page
    And the user enters 33 into the Box Number input field
    When clicks the Continue button
    Then the user should be on the 'What is the item number?' page
    And the user enters 1 into the Item Number input field
    When clicks the Continue button
    Then the user should be on the 'Box 33 commodity code amendment for item 1' page
    And the user enters 1234567891ABCD into the Original value input field
    And the user enters 1987654321DCBA into the Amended value input field
    When clicks the Continue button
    Then the user should be on the 'Confirm the details of the reason for underpayment' page
    And I check that within the Underpayment box summary that the Item level value of Box number is 33
    And I check that within the Underpayment box summary that the Item level value of Item number is 1
    And I check that within the Underpayment box summary that the Item level value of Original value is 1234567891ABCD
    And I check that within the Underpayment box summary that the Item level value of Amended value is 1987654321DCBA
    When the user clicks the 2nd change link for Item number on the Underpayment box summary page
    Then the user should be on the 'What is the item number?' page
    When the user clicks browser back
    Then the user should be on the 'Confirm the details of the reason for underpayment' page
    When the user clicks the 3rd change link for Original value on the Underpayment box summary page
    Then the user should be on the 'Box 33 commodity code amendment for item 1' page
    When the user clicks browser back
    Then the user should be on the 'Confirm the details of the reason for underpayment' page
    When the user clicks the 3rd change link for Amended value on the Underpayment box summary page
    Then the user should be on the 'Box 33 commodity code amendment for item 1' page
    When the user clicks browser back
    Then the user should be on the 'Confirm the details of the reason for underpayment' page
    When the user clicks the 1st change link for Box number on the Underpayment box summary page
    Then the user should be on the 'What is the box number?' page
    And the user enters 22 into the Box Number input field
    When clicks the Continue button
    Then the user should be on the 'Box 22 invoice currency and total amount invoiced amendment' page
    And the user enters EUR125.00 into the Original value input field
    And the user enters GBP190.50 into the Amended value input field
    When clicks the Continue button
    Then the user should be on the 'Confirm the details of the reason for underpayment' page
    And I check that within the Underpayment box summary that the Entry level value of Box number is 22
    And I check that within the Underpayment box summary that the Entry level value of Original value is EUR125.00
    And I check that within the Underpayment box summary that the Entry level value of Amended value is GBP190.50
    When clicks the Continue button
    Then the user should be on the 'You have added 1 reason for the underpayment' page
    And the user selects the No radio button
    When clicks the Continue button
    Then the user should be on the 'Do you want to tell us anything else about the underpayment?' page
    And the user selects the No radio button
    When clicks the Continue button
    Then the user should be on the 'Supporting documentation' page
    When clicks the Continue button
    Then the user should be on the 'Upload supporting documentation' page
    And I get the data from the page
    And the user selects file /src/test/resources/data/TestDocument.pdf in the file input field
    And I call the success redirect
    Then the user should be on the 'Upload progress' page
    And I call the upscan callback handler and get response 204
    And clicks the Refresh button
    Then the user should be on the 'You have uploaded 1 file' page
    And there should be '1' files on the page
    And the user selects the No radio button
    And clicks the Continue button
    Then the user should be on the 'Who should we contact if we have questions about this disclosure?' page
    And the user enters First last into the Name input field
    And the user enters email@email.com into the Email address input field
    And the user enters 0123456789 into the UK telephone number input field
    When clicks the Continue button
    Then the user should be on the 'Is this the correct address to send the demand for payment to?' page
    And the user selects the Yes radio button
    When clicks the Continue button
    Then the user should be on the 'How will you pay for the import VAT owed?' page
    And the user selects the By duty deferment account radio button
    When clicks the Continue button
    Then the user should be on the 'What is the deferment account number?' page
    And the user enters 0123456 into the Deferment account number input field
    When clicks the Continue button
    Then the user should be on the 'Check your answers before sending your disclosure' page
    When clicks the Accept and send button
    Then the user should be on the 'Disclosure complete' page