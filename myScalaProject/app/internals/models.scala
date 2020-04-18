package internals

import java.sql.Timestamp
import java.sql.Date
import play.api.libs.json._
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter._

trait BaseEntity {
  val id: Option[String]
  val createDate: Option[Timestamp]
}

case class Form(
                     id: Option[String] = None,
                     name: String,
                     elements: String,
                     createDate: Option[Timestamp] = None,
                   ) extends  BaseEntity

case class User(id: Option[String] = None,
                firstname: String,
                lastname: String,
                email: String,
                password: String,
                address: Option[String],
                role: Option[String],
                verified: Option[Boolean],
                createDate: Option[Timestamp] = None
               ) extends BaseEntity

case class JwtToken(id: Option[String],
                    userId : String,
                    jwtToken: String,
                    isActive: String,
                    createDate: Option[Timestamp] = None
                   ) extends BaseEntity
