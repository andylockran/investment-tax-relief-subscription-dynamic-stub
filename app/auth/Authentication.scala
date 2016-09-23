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

import play.api.mvc.Result
import uk.gov.hmrc.play.http.HeaderCarrier

import scala.concurrent.Future

trait Authentication {

  def authenticated(f: => AuthResponse => Future[Result])(implicit hc: HeaderCarrier): Future[Result] = {
    val environment =hc.headers.toMap.get("Environment")
    val token = hc.headers.toMap.get("Authorization")
    play.Logger.info(s"""Request headers: environment = ${environment.getOrElse("<NOT SET>")}, authorisation=" + ${token.getOrElse("<NOT SET>")})""")
    val noAuthHeaderErr = "required header 'Authorisation' not set in ETMP request"
    val noEnvHeaderErr = "required header 'Environment' not set in ETMP request"
    (environment, token) match {
      case (Some(_), Some(_)) => f(Authenticated)
      case (Some(_), None) => f(new NotAuthenticated(noAuthHeaderErr))
      case (None, Some(_)) => f(new NotAuthenticated(noEnvHeaderErr))
      case (None, None) => f(new NotAuthenticated(s"$noEnvHeaderErr and $noEnvHeaderErr"))
    }
  }
}


