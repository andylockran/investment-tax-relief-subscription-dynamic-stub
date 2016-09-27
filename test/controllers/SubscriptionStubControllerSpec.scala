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

import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import helpers.AuthHelpers._
import helpers.SubmissionHelpers._
import play.api.test.Helpers._
import play.api.mvc.Result

import scala.concurrent.Future

class SubscriptionStubControllerSpec extends UnitSpec with WithFakeApplication {
  object TestController extends SubscriptionStubController {
  }


  "SubscriptionStubController.createSubscription with an authorised request and a valid safe ID" when {

    "valid json is sent" should {
      val result: Future[Result] = TestController.createSubscription(validSafeId)
        .apply(validRequest.withBody(subscriptionRequest(validSafeId)))

      "return a subscription response" in {
        contentAsString(result) shouldBe """{"processingDate":"2014-12-17T09:30:47Z","tavcRefNumber":"YAA1234567890"}"""
      }

      "return CREATED" in {
        status(result) shouldBe CREATED
      }

    }

    "json with notfound as the acknowledgement reference" should {
      val result: Future[Result] = TestController.createSubscription(validSafeId)
        .apply(validRequest.withBody(subscriptionRequest("notfound")))

      "return an empty response" in {
        contentAsString(result) shouldBe ""
      }

      "return NOT FOUND" in {
        status(result) shouldBe NOT_FOUND
      }

    }

    "json with duplicate as the acknowledgement reference" should {
      val result: Future[Result] = TestController.createSubscription(validSafeId)
        .apply(validRequest.withBody(subscriptionRequest("duplicate")))

      "return a bad request response" in {
        contentAsString(result) shouldBe """{"reason" : "Error 400"}"""
      }

      "return BAD REQUEST" in {
        status(result) shouldBe BAD_REQUEST
      }

    }

    "json with servererror as the acknowledgement reference" should {
      val result: Future[Result] = TestController.createSubscription(validSafeId)
        .apply(validRequest.withBody(subscriptionRequest("servererror")))

      "return a server error response" in {
        contentAsString(result) shouldBe """{"reason" : "Server error"}"""
      }

      "return INTERNAL SERVER ERROR" in {
        status(result) shouldBe INTERNAL_SERVER_ERROR
      }

    }

    "json with serviceunavailable as the acknowledgement reference" should {
      val result: Future[Result] = TestController.createSubscription(validSafeId)
        .apply(validRequest.withBody(subscriptionRequest("serviceunavailable")))

      "return a service unavailable response" in {
        contentAsString(result) shouldBe """{"reason" : "Service Unavailable"}"""
      }

      "return SERVICE UNAVAILABLE" in {
        status(result) shouldBe SERVICE_UNAVAILABLE
      }

    }

    "json with missingregime as the acknowledgement reference" should {
      val result: Future[Result] = TestController.createSubscription(validSafeId)
        .apply(validRequest.withBody(subscriptionRequest("missingregime")))

      "return an error 500 response" in {
        contentAsString(result) shouldBe """{"reason" : "Error 500"}"""
      }

      "return INTERNAL SERVER ERROR" in {
        status(result) shouldBe INTERNAL_SERVER_ERROR
      }

    }

    "json with sapnumbermissing as the acknowledgement reference" should {
      val result: Future[Result] = TestController.createSubscription(validSafeId)
        .apply(validRequest.withBody(subscriptionRequest("sapnumbermissing")))

      "return an error 500 response" in {
        contentAsString(result) shouldBe """{"reason" : "Error 500"}"""
      }

      "return INTERNAL SERVER ERROR" in {
        status(result) shouldBe INTERNAL_SERVER_ERROR
      }

    }

    "json with notprocessed as the acknowledgement reference" should {
      val result: Future[Result] = TestController.createSubscription(validSafeId)
        .apply(validRequest.withBody(subscriptionRequest("notprocessed")))

      "return an error 503 response" in {
        contentAsString(result) shouldBe """{"reason" : "Error 503"}"""
      }

      "return SERVICE UNAVAILABLE" in {
        status(result) shouldBe SERVICE_UNAVAILABLE
      }

    }

  }

  "SubscriptionStubController.createSubscription with an authorised request and an invalid safe ID" when {

    "valid json is sent" should {
      val result: Future[Result] = TestController.createSubscription(invalidSafeId)
        .apply(validRequest.withBody(subscriptionRequest(validSafeId)))

      "return a subscription response" in {
        contentAsString(result) shouldBe """{"reason" : "Your submission contains one or more errors"}"""
      }

      "return CREATED" in {
        status(result) shouldBe BAD_REQUEST
      }

    }

  }

  "SubscriptionStubController.createSubscription with an unauthorised request" when {

    "valid json is sent" should {
      val result: Future[Result] = TestController.createSubscription(validSafeId)
        .apply(noEnvRequest.withBody(subscriptionRequest(validSafeId)))

      "return a subscription response" in {
        contentAsString(result) shouldBe "required header 'Environment' not set in ETMP request"
      }

      "return CREATED" in {
        status(result) shouldBe UNAUTHORIZED
      }

    }

  }

}
