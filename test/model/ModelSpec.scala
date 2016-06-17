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

package model

import java.time.LocalDateTime

import uk.gov.hmrc.play.test.UnitSpec
import java.util.Random
import play.api.libs.json._

object Generator {
  import uk.gov.hmrc.domain.Generator

  val rand = new Random()
  val ninoGenerator = new Generator(rand)

  def randomNino: String = ninoGenerator.nextNino.nino.replaceFirst("MA", "AA")
  def randomProtectionID = rand.nextLong
  def randomFP16ProtectionReference=("FP16" + Math.abs(rand.nextLong)).substring(0,9) + "C"
  def randomIP16ProtectionReference=("IP16" + Math.abs(rand.nextLong)).substring(0,9) + "B"
  def randomIP14ProtectionReference=("IP14" + Math.abs(rand.nextLong)).substring(0,9) + "A"
  def randomOlderProtectionReference=("A" +  Math.abs(rand.nextLong)).substring(0,5) + "A"
}

object ProtectionTestData {

  import Generator._

  val currDate = LocalDateTime.now.format(java.time.format.DateTimeFormatter.ISO_LOCAL_DATE)
  val currTime = LocalDateTime.now.format(java.time.format.DateTimeFormatter.ISO_LOCAL_TIME)


}

