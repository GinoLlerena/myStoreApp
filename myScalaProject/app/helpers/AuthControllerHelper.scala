package  helpers

import play.api.libs.json.Reads.{email, verifying}
import play.api.libs.functional.syntax._
import play.api.libs.json.{Format, Json, OFormat, OWrites, Reads, __}
import utils.ResponseCodes

object AuthControllerHelper {

  case class RequestLogin(email: String, password: String)

  val requestLoginReads: Reads[RequestLogin] = (
    (__ \ "email").read[String](email) and
      (__ \ "password").read[String](verifying[String](x => !x.isEmpty))
    )(RequestLogin.apply _)

  val requestLoginWrites: OWrites[RequestLogin] = Json.writes[RequestLogin]

  implicit val requestLogin: Format[RequestLogin] = Format(requestLoginReads, requestLoginWrites)


  case class UserResponseLogIn(id: Option[String], firstname: String, lastName: String, email: String, address: Option[String], role: Option[String])

  implicit val userResponseLogInFormat: OFormat[UserResponseLogIn] = Json.format[UserResponseLogIn]

}

