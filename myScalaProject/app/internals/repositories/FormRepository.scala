package internals.repositories

import javax.inject.{Inject, Singleton}
import play.api.db.slick.DatabaseConfigProvider
import slick.lifted.TableQuery
import internals._

@Singleton
class FormRepository @Inject()(dbConfigProvider: DatabaseConfigProvider )
  extends BaseEntityRepository[FormTable, Form](dbConfigProvider, TableQuery[FormTable])