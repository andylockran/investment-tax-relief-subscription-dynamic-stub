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

import auth.{NotAuthenticated, Authenticated, Authentication}
import play.api.Logger
import uk.gov.hmrc.play.microservice.controller.BaseController
import play.api.libs.json._
import model._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import scala.concurrent.Future

/**
  * The controller for the Investment Tax Relief Subscription service REST API dynamic stub
  *
  **/


object SubscriptionStubController extends SubscriptionStubController {
}

trait SubscriptionStubController extends BaseController with Authentication {

  val response = (message: String) => s"""{"reason" : "$message"}"""

  def createSubscription(safeId: String): Action[JsValue] = Action.async(BodyParsers.parse.json) { implicit request =>
    authenticated {
      case Authenticated => {
        Logger.info(s"[TAVCSubscriptionController][subscribe]")
        val subscriptionApplicationBodyJs = request.body.validate[SubscriptionRequest]

        checkApplicationBody(safeId,subscriptionApplicationBodyJs)
      }
      case NotAuthenticated(error) => Future.successful(Unauthorized(error))
    }
  }

  private def checkApplicationBody(safeId: String, subscriptionApplicationBodyJs: JsResult[SubscriptionRequest]) =
    subscriptionApplicationBodyJs.fold(
      errors => Future.successful(BadRequest("""{"reason" : "Invalid JSON message received"}""")),
      submitRequest => {
        (safeIdValidationCheck(safeId), submitRequest.acknowledgementReference) match {
          case (true, "notfound") => Future.successful(NotFound)
          case (true, "duplicate") => Future.successful(BadRequest(response("Error 400")))
          case (true, "servererror") => Future.successful(InternalServerError(response("Server error")))
          case (true, "serviceunavailable") => Future.successful(ServiceUnavailable(response("Service Unavailable")))
          case (true, "missingregime") => Future.successful(InternalServerError(response("Error 500")))
          case (true, "sapnumbermissing") => Future.successful(InternalServerError(response("Error 500")))
          case (true, "notprocessed") => Future.successful(ServiceUnavailable(response("Error 503")))
          case (true, _) => Future.successful(Created(Json.toJson(SubscriptionResponse("2014-12-17T09:30:47Z", "YAA1234567890"))))
          case (false, _) => Future.successful(BadRequest(response("Your submission contains one or more errors")))
        }
      }
    )

  private def safeIdValidationCheck(safeId: String): Boolean = {
    val pattern = """^X[A-Z]000[0-9]{10}$""".r
    safeId match {
      case pattern() => true
      case _ => false
    }
  }
}
