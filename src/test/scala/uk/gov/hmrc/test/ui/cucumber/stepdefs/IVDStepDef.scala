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


class IVDStepDef extends ShutdownStepDef {

  Given("""^a user logs in to access the Import Voluntary Disclosure Service""") { () =>
    driver.navigate().to(AuthLoginStubPage.url)
    driver.findElement(By.name("redirectionUrl")).clear()
    driver.findElement(By.name("redirectionUrl")).sendKeys(ImportVoluntaryDisclosureLandingPage.url)
    driver.findElement(By.cssSelector("Input[value='Submit']")).click()
  }

  Then("""^the user should be on the '(.*)' page$""") { (page: String) =>
    val actualPage: String = driver.findElement(By.tagName("h1")).getText
    assertResult(page)(actualPage)
  }

  Then("""^the user navigates the '(.*)' page$""") { (page: String) =>
    page match {
      case "supporting-documentation" => driver.navigate().to(ImportVoluntaryDisclosureLandingPage.url + "/disclosure/supporting-documentation-format")
      case _ => fail(s"$page is not a directly navigable page")
    }
  }

  Then("""^the page should contain (.*) input$""") { name: String =>
    val input = driver.findElement(By.name(name))
    assertResult("Callback")(input.getAttribute("value"))
  }

  Then("""^the page should be printed$""") { () =>
    println(driver.getPageSource)
  }

  And("""^the user should be either waiting for file upload or completed upload$""") { () =>

    def sleep(millis: Int = 1000) = Thread.sleep(millis)

    def comparisonCheck(count: Int): Boolean = {
      val actualPage = driver.findElement(By.cssSelector("h1")).getText
      if (actualPage != "You have uploaded 1 file" && count < 60) {
        sleep(1500)
        if (findBy(By.className("govuk-button")).isDisplayed) {
          findBy(By.className("govuk-button")).click()
          comparisonCheck(count + 1)
        } else {
          val actualPage = driver.findElement(By.cssSelector("h1")).getText
          println(actualPage)
          driver.navigate().to("http://localhost:7950/disclose-import-taxes-underpayment/disclosure/upload-file/polling")
          comparisonCheck(count + 1)
        }
      } else {
        if (actualPage == "You have uploaded 1 file") true
        else {
          fail("Failed to redirect to the 'The file has been uploaded successfully' page")
        }
      }
    }

    assert(comparisonCheck(0))

  }

  And("""^the user selects the (.*) radio button$""") { button: String =>
    button match {
      case "Importer" | "One Entry" | "Yes" => clickById("value")
      case "Representative" => clickById("value-2")
      case "No" => clickById("value-no")
      case _ => fail(s"$button is not a valid radio button")
    }
  }

  And("""^the user enters (.*) into the (.*) input field$""") { (value: String, field: String) =>
    field match {
      case "EPU number" => findById("epu").sendKeys(value)
      case "Entry number" => findById("entryNumber").sendKeys(value)
      case "Entry date day" => findById("entryDate.day").sendKeys(value)
      case "Entry date month" => findById("entryDate.month").sendKeys(value)
      case "Entry date year" => findById("entryDate.year").sendKeys(value)
      case "Original amount" =>
        findById("original").clear()
        findById("original").sendKeys(value)
      case "Amended amount" =>
        findById("amended").clear()
        findById("amended").sendKeys(value)
      case "Customs procedure code" =>
        findById("cpc").sendKeys(value)
      case "Name" => findById("fullName").sendKeys(value)
      case "Email address" => findById("email").sendKeys(value)
      case "UK telephone number" => findById("phoneNumber").sendKeys(value)
      case "Upload document" => {
        val path = Paths.get("").toAbsolutePath
        findById("file").sendKeys(path + value)
      }
      case _ => fail(s"$field is not a valid input field")
    }

  }

  And("""^clicks the (.*) button$""") { button: String =>
    button match {
      case "Save and continue" | "Continue" | "Confirm" | "Accept and continue" => findBy(By.className("govuk-button")).click()
      case _ => fail(s"$button is not a valid button")
    }
  }

  And("""^wait for (.*) seconds$""") { wait: Int =>
    Thread.sleep(wait * 1000)
  }

  And("""^they select the (.*) checkbox$""") { checkbox: String=>
    checkbox match{
      case ("Customs Duty") => clickById("value")
      case ("Import VAT") => clickById("value-2")
      case ("Excise Duty") => clickById("value-3")
      case _ => fail(s"$checkbox is not a valid checkbox")
    }
  }

  And("""^the user clicks the (.*) (.*) link for (.*) on the Underpayment amount summary page$""") { (linkPos: String, link: String, field: String) =>
    link match {
      case "change" =>
        linkPos match {
          case "1st" => findBy(By.cssSelector("#main-content > div > div > dl:nth-child(3) > div.govuk-summary-list__row.govuk-summary-list__row--no-border > dd.govuk-summary-list__actions.govuk-\\!-padding-bottom-0 > a")).click()
          case "2nd" => findBy(By.cssSelector("#main-content > div > div > dl:nth-child(5) > div.govuk-summary-list__row.govuk-summary-list__row--no-border > dd.govuk-summary-list__actions.govuk-\\!-padding-bottom-0 > a")).click()
          case "3rd" => findBy(By.cssSelector("#main-content > div > div > dl:nth-child(7) > div.govuk-summary-list__row.govuk-summary-list__row--no-border > dd.govuk-summary-list__actions.govuk-\\!-padding-bottom-0 > a")).click()
          case _ => fail(s"$link is not a valid link")
        }
      case _ => fail(s"$link is not a valid link")
    }
  }

  And("""^the (.*) is (.*) on the Underpayment amount summary page$""") { (field: String, expectedAmount: String) =>
    field match {
      case "Customs duty original amount" =>
        val actualAmount: String = findBy(By.cssSelector("#main-content > div > div > dl:nth-child(3) > div.govuk-summary-list__row.govuk-summary-list__row--no-border > dd.govuk-summary-list__value.govuk-\\!-padding-bottom-0")).getText
        assertResult(expectedAmount)(actualAmount)
      case "Customs duty amended amount" =>
        val actualAmount: String = findBy(By.cssSelector("#main-content > div > div > dl:nth-child(3) > div:nth-child(2) > dd")).getText
        assertResult(expectedAmount)(actualAmount)
      case "Customs duty due to HMRC amount" =>
        val actualAmount: String = findBy(By.cssSelector("#main-content > div > div > dl:nth-child(3) > div:nth-child(3) > dd")).getText
        assertResult(expectedAmount)(actualAmount)
      case "Import VAT original amount" =>
        val actualAmount: String = findBy(By.cssSelector("#main-content > div > div > dl:nth-child(5) > div.govuk-summary-list__row.govuk-summary-list__row--no-border > dd.govuk-summary-list__value.govuk-\\!-padding-bottom-0")).getText
        assertResult(expectedAmount)(actualAmount)
      case "Import VAT amended amount" =>
        val actualAmount: String = findBy(By.cssSelector("#main-content > div > div > dl:nth-child(5) > div:nth-child(2) > dd")).getText
        assertResult(expectedAmount)(actualAmount)
      case "Import VAT due to HMRC amount" =>
        val actualAmount: String = findBy(By.cssSelector("#main-content > div > div > dl:nth-child(5) > div:nth-child(3) > dd")).getText
        assertResult(expectedAmount)(actualAmount)
      case "Excise duty original amount" =>
        val actualAmount: String = findBy(By.cssSelector("#main-content > div > div > dl:nth-child(7) > div.govuk-summary-list__row.govuk-summary-list__row--no-border > dd.govuk-summary-list__value.govuk-\\!-padding-bottom-0")).getText
        assertResult(expectedAmount)(actualAmount)
      case "Excise duty amended amount" =>
        val actualAmount: String = findBy(By.cssSelector("#main-content > div > div > dl:nth-child(7) > div:nth-child(2) > dd")).getText
        assertResult(expectedAmount)(actualAmount)
      case "Excise duty due to HMRC amount" =>
        val actualAmount: String = findBy(By.cssSelector("#main-content > div > div > dl:nth-child(7) > div:nth-child(3) > dd")).getText
        assertResult(expectedAmount)(actualAmount)

      case _ => fail(s"$field is not a valid field")
    }
  }

  And("""^the user clicks the back link$""") {
    findById("back-link").click()
  }

  And("""^the user clicks browser back""") {
    driver.navigate().back()
  }
}
