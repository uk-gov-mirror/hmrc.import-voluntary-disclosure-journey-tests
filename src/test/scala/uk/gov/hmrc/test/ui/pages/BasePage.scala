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

package uk.gov.hmrc.test.ui.pages

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.openqa.selenium.{By, WebElement}
import org.scalatest.matchers.should.Matchers
import play.api.libs.ws.ahc.StandaloneAhcWSClient
import play.api.libs.ws.DefaultBodyWritables._
import uk.gov.hmrc.test.ui.driver.BrowserDriver
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.duration._
import scala.concurrent.Await

trait BasePage extends Matchers with BrowserDriver {

  // ID Wrappers
  def findById(id: String) = findBy(By.id(id))
  def clickById(id: String) = findById(id).click()
  def clickBySelector(selector: String) = findBy(By.cssSelector(selector)).click()

  // Find By Syntax Sugar
  def findBy(by: By) = driver.findElement(by)
  def findElementsBy(by: By) = driver.findElements(by)
  def findElementById(id: String): WebElement = driver.findElement(By.id(id))
  def findElementByName(name: String): WebElement = driver.findElement(By.name(name))

  def asyncClient: StandaloneAhcWSClient = {
    implicit val system: ActorSystem = ActorSystem()
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    StandaloneAhcWSClient()
  }

  def requestPOST(url: String,
                  body: String,
                  headers: Map[String, String]
                 ): Int = {

    val client = asyncClient.url(url)

    Await.result(client
      .addHttpHeaders(headers.toList: _*)
      .withRequestTimeout(60 seconds)
      .post(body)
      .map(_.status), 60 seconds)
  }

}
