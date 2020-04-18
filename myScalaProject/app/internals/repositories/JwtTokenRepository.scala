package internals.repositories

import internals.{JwtToken, JwtTokenTable}
import javax.inject.{Inject, Singleton}
import internals.JwtToken
import play.api.db.slick.DatabaseConfigProvider
import slick.lifted.TableQuery

import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try


@Singleton
class JwtTokenRepository @Inject()(dbConfigProvider: DatabaseConfigProvider)
  extends BaseEntityRepository[JwtTokenTable, JwtToken](dbConfigProvider, TableQuery[JwtTokenTable])

