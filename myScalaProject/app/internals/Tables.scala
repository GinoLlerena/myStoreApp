package internals

import java.sql.Timestamp
import scala.reflect.ClassTag
import slick.jdbc.MySQLProfile.api._

abstract class BaseEntityTable[E: ClassTag](tag: Tag, tableName: String) extends Table[E](tag, tableName) {
  val id: Rep[String] = column[String]("ID", O.PrimaryKey)

  def createDate = column[Timestamp]("CREATE_DATE")
}

class FormTable(tag: Tag) extends BaseEntityTable[Form](tag, "FORMS") {
  def name = column[String]("NAME")
  def elements =column[String]("ELEMENTS")

  def * = (id.?, name, elements, createDate.?) <> (Form.tupled, Form.unapply)
}

class UserTable(tag: Tag) extends BaseEntityTable[User](tag, tableName = "USERS"){
  def firstName = column[String]("FIRSTNAME")
  def lastName = column[String]("LASTNAME")
  def email = column[String]("EMAIL")
  def password = column[String]("PASSWORD")
  def address = column[String]("ADDRESS")
  def role = column[String]("ROLE")
  def verified = column[Boolean]("VERIFIED")

  def * = (id.?, firstName, lastName, email, password, address.?,  role.?, verified.?, createDate.?) <> (User.tupled, User.unapply)
}

class JwtTokenTable(tag: Tag) extends BaseEntityTable[JwtToken](tag, "TOKENS"){
  def userId = column[String]("EMAIL")
  def jwtToken = column[String]("TOKEN")
  def isActive = column[String]("ACTIVE")

  def * = (id.?, userId, jwtToken, isActive, createDate.?) <> (JwtToken.tupled, JwtToken.unapply)

}
