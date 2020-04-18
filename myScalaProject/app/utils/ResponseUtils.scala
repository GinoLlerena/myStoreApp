package utils

import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Date

import play.api.libs.functional.syntax._
import play.api.libs.json._
import play.api.mvc.Result
import play.api.mvc.Results._

object ResponseCodes {
  val SUCCESS = 0
  val GENERIC_ERROR = 100
  val CONFLICT_ERROR = 409
  val MISSING_FIELDS = 422
  val UNAUTHORIZED = 401
  val USER_NOT_FOUND= 404
}
/*
  Note: Important!!! Only move here response objects or methods that are going to be used in more than one file.
  If you are not sure start by declaring your response object in the controller you are going to use it at and move it here
  if you end up using it in another place.
 */
object ResponseUtils {

  def JsonOk[T](t: T)(implicit writes: Writes[T]): Result = Ok(Json.toJson(t))

  case class Response[T](responseCode: Int, responseMessage: String, data: T)

  object Implicits {

    implicit val timestampFormat: Format[Timestamp] = new Format[Timestamp] {
//      todo: Write correct implementation
      override def reads(json: JsValue): JsResult[Timestamp] = {
        JsSuccess(new Timestamp(System.currentTimeMillis()))
      }

      override def writes(o: Timestamp): JsValue = {
        val simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        JsString(simpleDateFormat.format(new Date(o.getTime)))
      }
    }

    implicit def responseFormat[T](implicit fmt: Format[T]): Format[Response[T]] =
      (
        (__ \ "responseCode").format[Int] and
          (__ \ "responseMessage").format[String] and
          (__ \ "data").format[T](fmt)
        ) (Response.apply, unlift(Response.unapply))

//Generic Map  Write
    implicit val mapWrites: Writes[Map[String, Any]] = (o: Map[String, Any]) => {
      JsObject(
        o.map { kvp =>
          kvp._1 -> (
            kvp._2 match {
              case x: String => JsString(x)
              case x: Int => JsNumber(x)
              case _ => JsNull // Do whatever you want here.
            }
            )
        }
      )
    }


  }
}
