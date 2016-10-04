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

package auth

import play.api.mvc.Results
import uk.gov.hmrc.play.test.UnitSpec
import play.api.test.Helpers._
import helpers.AuthHelpers._
import play.api.mvc.Request

import scala.concurrent.Future

class AuthorisationSpec extends UnitSpec {



  object TestAuthorisation extends Authorisation

  private def authorised(implicit request: Request[Any]) = TestAuthorisation.authorised {
    case Authorised => Future.successful(Results.Ok)
    case NotAuthorised(error) => Future.successful(Results.Forbidden(error))
  }

  "Authorisation.authorised" when {

    "If the request contains a bearer token and environment in the header carrier" should {
      val result = authorised(validRequest)

      "Return Authorised" in {
        status(result) shouldBe OK
      }

    }

    "If the request contains no bearer token in the header carrier" should {
      val result = authorised(noAuthRequest)

      "Return NotAuthorised" in {
        status(result) shouldBe FORBIDDEN
      }

      "Return the noAuthHeaderError to be used by the controller" in {
        contentAsString(result) shouldBe TestAuthorisation.noAuthHeaderError
      }

    }

    "If the request contains no environment in the header carrier" should {
      val result = authorised(noEnvRequest)

      "Return NotAuthorised" in {
        status(result) shouldBe FORBIDDEN
      }

      "Return the noEnvHeaderError to be used by the controller" in {
        contentAsString(result) shouldBe TestAuthorisation.noEnvHeaderError
      }

    }

    "If the request contains no bearer token or environment in the header carrier" should {
      val result = authorised(emptyRequest)

      "Return NotAuthorised" in {
        status(result) shouldBe FORBIDDEN
      }

      "Return the combinedError to be used by the controller" in {
        contentAsString(result) shouldBe TestAuthorisation.combinedError
      }

    }

  }

}
