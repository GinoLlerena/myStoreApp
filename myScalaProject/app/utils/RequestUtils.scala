package utils

import play.api.libs.functional.syntax._
import play.api.libs.json.Reads.{email, minLength, verifying}
import play.api.libs.json._

object RequestUtils {

  case class RequestChangePassword(email: String, newPassword: String)

  object Implicits {

    implicit val requestChangePassword: OFormat[RequestChangePassword] = Json.format[RequestChangePassword]

  }

}
