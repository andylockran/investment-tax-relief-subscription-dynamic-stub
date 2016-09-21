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

import play.api.Logger
import uk.gov.hmrc.play.microservice.controller.BaseController
import play.api.libs.json._
import model._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._
import scala.concurrent.Future
import scala.util.Random


/**
  * The controller for the Investment Tax Relief Subscription service REST API dynamic stub
  *
  **/

object SubscriptionStubController extends SubscriptionStubController {
}

trait SubscriptionStubController extends BaseController {

  def createSubscription: Action[AnyContent] = Action.async{ implicit request =>
    Logger.info(s"[TAVCSubscriptionController][subscribe]")
    val jsonRequestData = request.body.asJson.get
    Logger.info(s"[TAVCSubscriptionController][subscribe] - " +
      s"Header Environment = ${request.headers.get("Environment")} - jsonRequestData = ${jsonRequestData}")
    val result = SubscriptionResponse("2001-12-17T09:30:47Z", "XY1200000100002")
    Future.successful(Ok(Json.toJson(result)))
  }

  def getSubscription: Action[AnyContent] = Action.async{ implicit request =>
    val result = SubscriptionResponse("2001-12-17T09:30:47Z", "XY1200000100002")
    Future.successful(Ok(Json.toJson(result)))
  }

}

object ControllerHelper {
  /*
 * Checks that the standard extra headers required for NPS requests are present in a request
 * @param headers a simple map of all request headers
 * @param the result of validating the request body
 * @rreturn the overall validation result, of non-success then will include both body and  header validation errors
 */
  def addExtraRequestHeaderChecks[T](headers: Map[String, String], bodyValidationResultJs: JsResult[T]): JsResult[T] = {
    val environment = headers.get("Environment")
    val token = headers.get("Authorization")
    val notSet = "<NOT SET>"
    play.Logger.info("Request headers: environment =" + environment.getOrElse(notSet) + ", authorisation=" + token.getOrElse(notSet))

    //  Ensure any header validation errors are accumulated with any body validation errors into a single JsError
    //  (the below code is not so nice, could be a good use case for scalaz validation)
    val noAuthHeaderErr = JsError("required header 'Authorisation' not set in NPS request")
    val noEnvHeaderErr = JsError("required header 'Environment' not set in NPS request")
    // 1. accumlate any header errors
    def headerNotPresentErrors: Option[JsError] = (environment, token) match {
      case (Some(_), Some(_)) => None
      case (Some(_), None) => Some(noAuthHeaderErr)
      case (None, Some(_)) => Some(noEnvHeaderErr)
      case (None, None) => Some(noAuthHeaderErr ++ noEnvHeaderErr)
    }
    // 2. accumulate any header + any body errors
    (bodyValidationResultJs, headerNotPresentErrors) match {
      case (e1: JsError, e2: Some[JsError]) => e1 ++ e2.get
      case (e1: JsError, _) => e1
      case (_, e2: Some[JsError]) => e2.get
      case _ => bodyValidationResultJs // success case
    }
  }
}
