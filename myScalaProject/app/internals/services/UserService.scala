package internals.services

import javax.inject.{Inject, Singleton}
import internals.{User, UserTable}
import internals.repositories.UserRepository
import utils.ResponseCodes
import slick.jdbc.MySQLProfile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Try}
import utils.BCryptPass.validateHashPass
import utils.ErrorsHelper

@Singleton
class UserService @Inject()(override  val repository: UserRepository)
  extends BaseEntityService[UserTable, User, UserRepository] {

  def processLogin(email: String, password: String): Future[Try[User]] = {

    val userFilterByEmail = repository.filter(_.email === email)
    val userLogin = {
      for {
        userOption <- userFilterByEmail.map(_.head)
      } yield {
        if (validateHashPass(password, userOption.password)) {
          Try(userOption)
        }
        else
          Failure(new Exception(s"${ResponseCodes.UNAUTHORIZED}"))
      }
    } recover{
      case e: Exception => Failure(new Exception(s"${ResponseCodes.USER_NOT_FOUND}", e))
    }
    userLogin
  }

}
