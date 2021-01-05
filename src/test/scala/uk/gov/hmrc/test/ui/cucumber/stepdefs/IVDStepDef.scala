/*
 * Copyright 2020 HM Revenue & Customs
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

import org.openqa.selenium.By
import uk.gov.hmrc.test.ui.pages.{AuthLoginStubPage, ImportVoluntaryDisclsureLandingPage}

class IVDStepDef extends BaseStepDef {

  Given("""^a user logs in to access the Import Voluntary Disclosure Service""") { () =>
    driver.navigate().to(AuthLoginStubPage.url)

    driver.findElement(By.name("redirectionUrl")).clear()
    driver.findElement(By.name("redirectionUrl")).sendKeys(ImportVoluntaryDisclsureLandingPage.url)
    driver.findElement(By.cssSelector("Input[value='Submit']")).click()
  }

  Then("""^the user should be on the '(.*)' page$""") { (page: String) =>
    val actualPage: String = driver.findElement(By.tagName("h1")).getText
    assertResult(page)(actualPage)
  }

}