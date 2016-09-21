/*
 * Copyright 2016 HM Revenue & Customs
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

package controllers

import model.SubscriptionResponse
import org.scalatestplus.play.{OneServerPerSuite, PlaySpec}
import play.api.libs.json.Json
import play.api.test.FakeRequest
import play.api.test.Helpers._

class TAVCSubscriptionControllerSpec extends PlaySpec with OneServerPerSuite {

  "TAVCSubscriptionController" must {
    "createSubscription" must {
      "Subscribe and then respond with OK and the correct response body" in {
        val inputJson = Json.parse( """ {"safeId": "XE0001234567890", "address": { "addressLine1": "100 SuttonStreet", "addressLine2": "Wokingham", "postalCode": "DH14EJ", "countryCode": "GB" }, "contactDetails": { "telephone": "01332752856", "mobile": "07782565326", "fax": "01332754256", "email": "stephen@manncorpone.co.uk" } } """)
        val result = SubscriptionStubController.createSubscription.apply(FakeRequest().withJsonBody(inputJson))
        val processingDate = "2001-12-17T09:30:47Z"
        val tavcRefNumber = "XY1200000100002"
        val formBundleNumber = "123456789012345"
        val successResponse = SubscriptionResponse(processingDate = processingDate, tavcRefNumber = tavcRefNumber, formBundleNumber = formBundleNumber)
        status(result) must be(OK)
        contentAsJson(result).as[SubscriptionResponse] must be(successResponse)
      }
    }
    "getSubscription" must {
      "return current subscription" in {
        val result = SubscriptionStubController.getSubscription.apply(FakeRequest())
        val processingDate = "2001-12-17T09:30:47Z"
        val tavcRefNumber = "XY1200000100002"
        val formBundleNumber = "123456789012345"
        val successResponse = SubscriptionResponse(processingDate = processingDate, tavcRefNumber = tavcRefNumber, formBundleNumber = formBundleNumber)
        status(result) must be(OK)
        contentAsJson(result).as[SubscriptionResponse] must be(successResponse)
      }
    }
  }

}