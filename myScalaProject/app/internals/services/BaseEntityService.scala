package internals.services

import scala.concurrent.Future
import internals._
import internals.repositories.{BaseEntityRepository}

trait BaseEntityService[T <: BaseEntityTable[E],E <: BaseEntity, R <: BaseEntityRepository[T,E]] {

  val repository: R

  def getById(id: String): Future[Option[E]] = repository.getById(id)

  def getAll: Future[Seq[E]] = repository.getAll

  def save(row: E): Future[_] = repository.save(row)

  def deleteById(id: String): Future[Int] = repository.deleteById(id)

  def updateById(id: String, row: E): Future[_] = repository.updateById(id,row)

}
