package utils

import pdi.jwt._
import play.api.libs.json._
import utils.ResponseUtils.Implicits._

import scala.util.Try

object JWTUtil {

  private val JwtSecretKey = "secretKey"
  private val JwtSecretAlgorithm = JwtAlgorithm.HS256
  val SECONDS_DAY = 86400
  val SECONDS_HOUR = 3600


  def createToken(payload: Map[String, Any], nameRole: String): String = {
    val rolesAdmin = List("admin")
    val isAdminUser = rolesAdmin.find(_ == nameRole).isDefined
    Jwt.encode(JwtClaim(Json.stringify(Json.toJson(payload))).issuedNow.expiresIn(if(isAdminUser) {SECONDS_DAY*60}  else SECONDS_HOUR*8), JwtSecretKey, JwtSecretAlgorithm)
  }

  def decodeToken(token: String): Try[String] = {
    Jwt.decode(token, JwtSecretKey, Seq(JwtSecretAlgorithm))
  }

  def validateToken(token: String): Boolean ={
     Jwt.isValid(token, "secretKey", Seq(JwtSecretAlgorithm))
  }
}