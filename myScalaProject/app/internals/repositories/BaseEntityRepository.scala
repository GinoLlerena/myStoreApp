package internals.repositories

import internals.{BaseEntity, BaseEntityTable}
import play.api.db.slick.DatabaseConfigProvider
import slick.basic.DatabaseConfig
import slick.dbio.Effect
import slick.jdbc.JdbcProfile
import slick.jdbc.JdbcBackend
import slick.jdbc.MySQLProfile.api._
import slick.lifted.CanBeQueryCondition
import slick.sql.FixedSqlAction

import scala.concurrent.Future
import scala.reflect.ClassTag
import scala.util.Try

trait BaseEntityRepositoryComponent[T <: BaseEntityTable[E], E <: BaseEntity] {
  def getById(id: String): Future[Option[E]]

  def getAll: Future[Seq[E]]

  def filter[C <: Rep[_]](expr: T => C)(implicit wt: CanBeQueryCondition[C]): Future[Seq[E]]

  //  todo: findout what to return from save
  def save(row: E): Future[_]

  def deleteById(id: String): Future[Int]

  def updateById(id: String, row: E): Future[_]
}


trait BaseEntityRepositoryQuery[T <: BaseEntityTable[E], E <: BaseEntity] {
  val query: TableQuery[T]

  def getByIdQuery(id: String): Query[T, E, Seq] = query.filter(table => table.id === id)

  def getAllQuery: Query[T, E, Seq] = query

  def filterQuery[C <: Rep[_]](expr: T => C)(implicit wt: CanBeQueryCondition[C]): Query[T, E, Seq] = query.filter(expr)

  def saveQuery(row: E): FixedSqlAction[Int, NoStream, Effect.Write] = query += row

  def saveListQuery(rows: Seq[E]): FixedSqlAction[Option[Int], NoStream, Effect.Write] = query ++= rows

  def deleteByIdQuery(id: String): FixedSqlAction[Int, NoStream, Effect.Write] = query.filter(t => t.id === id).delete

  def updateByIdQuery(id: String, row: E): FixedSqlAction[Int, NoStream, Effect.Write] = query.filter(t => t.id === id).update(row)
}


abstract class BaseEntityRepository[T <: BaseEntityTable[E], E <: BaseEntity : ClassTag](dbConfigProvider: DatabaseConfigProvider, clazz: TableQuery[T]) extends BaseEntityRepositoryQuery[T, E] with BaseEntityRepositoryComponent[T, E] {

  //    lazy val clazzEntity = ClassTag[E].asInstanceOf[Class[E]]
  val query: TableQuery[T] = clazz
  val dbConfig: DatabaseConfig[JdbcProfile] = dbConfigProvider.get[JdbcProfile]
  val db: JdbcBackend#DatabaseDef = dbConfig.db

  def getAll: Future[Seq[E]] = {
    val action = getAllQuery.sortBy(x => x.createDate.desc).result
    //println(action.statements.head)
    db.run(action)
  }

  def getById(id: String): Future[Option[E]] = {
    val action = getByIdQuery(id).result.headOption
//    println(action.statements.head)
    db.run(action)
  }

  def filter[C <: Rep[_]](expr: T => C)(implicit wt: CanBeQueryCondition[C]): Future[Seq[E]] = {
    db.run(filterQuery(expr).result)
  }

  def save(row: E): Future[Try[Int]] = {
    db.run(saveQuery(row).asTry)
  }

  def saveList(row: Seq[E]) = {
    db.run(saveListQuery(row).asTry)
  }

  def updateById(id: String, row: E): Future[Try[Int]] = {
    val action = updateByIdQuery(id, row)
    println(action.statements.head)
    db.run(action.asTry)
  }

  def deleteById(id: String): Future[Int] = {
    db.run(deleteByIdQuery(id))
  }
}