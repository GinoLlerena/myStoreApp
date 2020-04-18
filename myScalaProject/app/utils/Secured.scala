package utils

import play.api.libs.json.{Json, OFormat}
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import internals.services.UserService
import internals.{User}

trait Secured {

  val userService: UserService
  val cc: ControllerComponents
  implicit val ec: ExecutionContext

  case class NotAuthorizedResponse(responseCode: Int, responseMessage: String)
  implicit val notAuthorizedResponseFormat: OFormat[NotAuthorizedResponse] = Json.format[NotAuthorizedResponse]

  private def notAuthorizedResponse: NotAuthorizedResponse =
    NotAuthorizedResponse(ResponseCodes.GENERIC_ERROR, "Not Authorized")

  case class JwtBody(exp: Int, iat: Int, userId: String, email: String)
  implicit val jwtBodyReads =  Json.reads[JwtBody]
  val emptyJwtBody = JwtBody(0, 0, "", "")

  class UserRequest[A](val user: User, request: Request[A])
    extends WrappedRequest[A](request)

  object AuthenticatedAction extends ActionBuilder[UserRequest, AnyContent] {

    def invokeBlock[A](request: Request[A], block: UserRequest[A] => Future[Result]): Future[Result] = {

      val jwtTokenSplit = request.headers.get("Authorization").getOrElse("").split(" ")
      val jwtToken = if(jwtTokenSplit.length > 1)  jwtTokenSplit(1) else ""
      val decodedJwt: Try[String] = JWTUtil.decodeToken(jwtToken)
      val jwtBody: JwtBody = Json.parse(decodedJwt.getOrElse("{}")).validate[JwtBody].getOrElse(emptyJwtBody)
      val userId = jwtBody.userId

      userService.getById(userId).flatMap {
        case Some(user) => block(new UserRequest(user, request))
        case None => Future.successful(Results.Unauthorized(Json.toJson(notAuthorizedResponse)))
      }
    }

    override def parser: BodyParser[AnyContent] = cc.parsers.default

    override protected def executionContext: ExecutionContext = ec
  }

}