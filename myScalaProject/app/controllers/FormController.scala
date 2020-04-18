package controllers

import javax.inject._
import akka.actor.ActorSystem
import play.api.mvc._
import play.api.libs.json.Json

import scala.concurrent.duration._
import scala.concurrent.{ExecutionContext, Future, Promise}
import internals.services.FormService
import utils.ResponseCodes
import utils.ResponseUtils.JsonOk
import helpers.FormControllerHelper.{ toFormsResponse}

@Singleton
class FormController @Inject()(val formService: FormService, cc: ControllerComponents, actorSystem: ActorSystem)(implicit exec: ExecutionContext) extends AbstractController(cc) {

  //val logger = Logger(this.getClass)


  def showForms(): Action[AnyContent] = Action.async { implicit request =>
    formService
      .getAll
      .map(forms =>
        JsonOk(toFormsResponse(forms))
      )
  }
}
