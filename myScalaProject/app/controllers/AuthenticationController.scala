package controllers

import javax.inject.Inject
import play.api.i18n.I18nSupport
import play.api.mvc.{AbstractController, Action, ControllerComponents}
import internals.services._
import internals.{JwtToken, User}
import play.api.libs.json.{JsError, JsValue, Json}
import utils.ResponseUtils.{JsonOk, Response}
import utils.{ErrorsHelper, JWTUtil, ResponseCodes}
import utils.ResponseUtils.Implicits._

import scala.util.{Failure, Success}
import utils.JWTUtil.validateToken
import helpers.AuthControllerHelper.{UserResponseLogIn, RequestLogin}
import scala.concurrent.{ExecutionContext, Future}

class AuthenticationController @Inject()(
                                          cc: ControllerComponents,
                                          userService: UserService,
                                          jwtTokenService: JwtTokenService,
                                        )  extends AbstractController(cc) {

  implicit val ec: ExecutionContext = defaultExecutionContext

  def login: Action[JsValue] = Action.async(parse.json) { implicit request =>
    request.body.validate[RequestLogin].fold(
      invalidRequest => {
        val errors: JsValue = JsError.toJson(invalidRequest)
        Future.successful(
          Ok(Json.toJson(Response[JsValue](ResponseCodes.MISSING_FIELDS, "error", errors)))
        )
      },
      userRequest => {
        userService.processLogin(userRequest.email, userRequest.password) map {
          case Success(user) =>
            val token = JWTUtil.createToken(Map("userId" -> user.id.get, "email" -> user.email), user.role.get)
            //jwTokenService.save(JwtToken(None, users.id.getOrElse(""), used = true, token, None, None))
            JsonOk(
              Response[UserResponseLogIn](ResponseCodes.SUCCESS, "success",
                UserResponseLogIn(user.id, user.firstname, user.lastname, user.email, user.address, user.role))
            )
          case Failure(e) =>
            //logger.info(s"message ${e.getMessage}")
            val found = ErrorsHelper.get(e.getMessage.toInt)
            JsonOk(
              Response[String](e.getMessage.toInt,"error", s"${found.message}")
            )
        }
      }
    )
  }
}
