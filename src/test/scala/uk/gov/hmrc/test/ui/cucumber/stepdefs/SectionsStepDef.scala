/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.test.ui.cucumber.stepdefs

import java.nio.file.Paths

import org.openqa.selenium.By
import uk.gov.hmrc.test.ui.pages.{AuthLoginStubPage, ImportVoluntaryDisclosureLandingPage}
import uk.gov.hmrc.test.ui.utils.UpscanJson

class SectionsStepDef extends ShutdownStepDef {
  // Variables for upscan functionality
  var callbackUrl: String = ""
  var redirectUrl: String = ""
  var refKey: String = ""


  Given("""^I want to complete section 1: Service Entry""") { () =>
    // Login through Auth
    driver.navigate().to(AuthLoginStubPage.url)
    driver.findElement(By.name("redirectionUrl")).clear()
    driver.findElement(By.name("redirectionUrl")).sendKeys(ImportVoluntaryDisclosureLandingPage.url)
    driver.findElement(By.name("enrolment[0].name")).clear()
    driver.findElement(By.name("enrolment[0].name")).sendKeys("HMRC-CTS-ORG")
    driver.findElement(By.name("enrolment[0].taxIdentifier[0].name")).clear()
    driver.findElement(By.name("enrolment[0].taxIdentifier[0].name")).sendKeys("EORINumber")
    driver.findElement(By.name("enrolment[0].taxIdentifier[0].value")).clear()
    driver.findElement(By.name("enrolment[0].taxIdentifier[0].value")).sendKeys("GB987654321000")
    driver.findElement(By.cssSelector("Input[value='Submit']")).click()
    // Click continue on Confirm EORI
    findBy(By.className("govuk-button")).click()
  }

  And("""^I want to complete section 2: Entry Details as (.*)""") { (userType: String) =>
    // Select user type and click through to Num Entries
    if (userType == "Importer") {
      clickById("value")
      findBy(By.className("govuk-button")).click()
    } else {
      clickById("value-2")
      findBy(By.className("govuk-button")).click()
      findById("fullName").sendKeys("Importers Name")
      findBy(By.className("govuk-button")).click()
      findById("postcode").sendKeys("AA000AA")
      findBy(By.className("govuk-button")).click()
      findBy(By.className("govuk-button")).click()
      clickById("value")
      findBy(By.className("govuk-button")).click()
      findById("importerEORI").sendKeys("GB345834921000")
      findBy(By.className("govuk-button")).click()
    }
    // Select One Entry
    clickById("value")
    findBy(By.className("govuk-button")).click()
    // Enter Entry Details
    findById("epu").sendKeys("123")
    findById("entryNumber").sendKeys("123456Q")
    findById("entryDate.day").sendKeys("01")
    findById("entryDate.month").sendKeys("01")
    findById("entryDate.year").sendKeys("2020")
    findBy(By.className("govuk-button")).click()
    // Acceptance entry date
    clickById("value")
    findBy(By.className("govuk-button")).click()
    // Enter CPC
    findById("cpc").sendKeys("1234A67")
    findBy(By.className("govuk-button")).click()
  }

  And("""^I want to complete section 3: Underpayments with (.*)""") { (underpayments: String) =>
    // Click through guidance page
    findBy(By.className("govuk-button")).click()
    val underpaymentsList = underpayments.split("~").zipWithIndex
    for (underpayment <- underpaymentsList) {
      // Select the underpayment type radio button
      clickById(underpayment._1)
      findBy(By.className("govuk-button")).click()
      // Enter Original and Amended values
      findById("original").clear()
      findById("original").sendKeys("100")
      findById("amended").clear()
      findById("amended").sendKeys("200")
      findBy(By.className("govuk-button")).click()
      // Confirm Summary
      findBy(By.className("govuk-button")).click()
      // Underpayment Summary add another
      if (underpayment._2 < underpaymentsList.size - 1) {
        clickById("value")
      } else {
        clickById("value-no")
      }
      findBy(By.className("govuk-button")).click()
    }
  }


  And("""^I want to complete section 4: Underpayment Reasons""") { () =>
    // Underpayment Reasons Guidance page
    findBy(By.className("govuk-button")).click()
    // Enter Box 33
    findById("value-2").click
    findBy(By.className("govuk-button")).click()
    // Enter Item 1
    findById("itemNumber").clear()
    findById("itemNumber").sendKeys("1")
    findBy(By.className("govuk-button")).click()
    // Enter Original and Amended values
    findById("original").clear()
    findById("original").sendKeys("1234567891ABCD")
    findById("amended").clear()
    findById("amended").sendKeys("1987654321DCBA")
    findBy(By.className("govuk-button")).click()
    // Click through confirmation and select No to adding more
    findBy(By.className("govuk-button")).click()
    clickById("value-no")
    findBy(By.className("govuk-button")).click()
    // Add some extra information
    clickById("value")
    findBy(By.className("govuk-button")).click()
    findById("value").clear()
    findById("value").sendKeys("More Information")
    findBy(By.className("govuk-button")).click()
  }

  And("""^I want to complete section 5: Documents you must upload""") { () =>
    // Supporting Documentation Guidance page
    findBy(By.className("govuk-button")).click()
    // Further Supporting Documents page
    clickById("value-no")
    findBy(By.className("govuk-button")).click()
    // Upload a file and dummy upscan callback
    callbackUrl = driver.findElement(By.name("x-amz-meta-callback-url")).getAttribute("value")
    redirectUrl = driver.findElement(By.name("success_action_redirect")).getAttribute("value")
    refKey = driver.findElement(By.name("key")).getAttribute("value")
    val path = Paths.get("").toAbsolutePath
    findById("file").sendKeys(path + "/src/test/resources/data/TestDocument.pdf")
    driver.navigate().to(redirectUrl)
    requestPOST(callbackUrl, UpscanJson.upscanSuccessCallback(refKey), Map("Content-Type" -> "application/json"))
    // Click to refresh the Upload Progress Page then select not to upload any further docs
    findBy(By.className("govuk-button")).click()
    clickById("value-no")
    findBy(By.className("govuk-button")).click()
  }

  And("""^I want to complete section 6: Contact Details""") { () =>
    // Discloser's Details
    findById("fullName").sendKeys("First last")
    findById("email").sendKeys("email@email.com")
    findById("phoneNumber").sendKeys("0123456789")
    findBy(By.className("govuk-button")).click()
    // Change Address through ALF
    clickById("value-no")
    findBy(By.className("govuk-button")).click()
    findById("postcode").sendKeys("AA000AA")
    findBy(By.className("govuk-button")).click()
    findBy(By.className("govuk-button")).click()
  }

  And("""^I want to complete section 7: Deferment Details as (.*) with (.*)""") { (userType: String, split: String) =>
    // Select payment by deferement
    clickById("value")
    findBy(By.className("govuk-button")).click()
    if (userType == "Importer") {
      clickById("value")
      findBy(By.className("govuk-button")).click()
      // Add DAN and click through to CYA
      findById("value").sendKeys("1234567")
      findBy(By.className("govuk-button")).click()
    } else {
      if(split == "split") {
        // Select to split
        // TODO: Add further pages here
        clickById("value")
        findBy(By.className("govuk-button")).click()
      } else {
        // Select not to split
        clickById("value-2")
        findBy(By.className("govuk-button")).click()
        // Add DAN and click My deferment radio button
        findById("accountNumber").sendKeys("1234567")
        findById("value").click()
        findBy(By.className("govuk-button")).click()
      }
    }

  }

}
