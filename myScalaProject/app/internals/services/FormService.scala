package internals.services

import javax.inject.{Inject, Singleton}

import internals.repositories.{FormRepository}
import internals.{Form, FormTable}

@Singleton
class FormService @Inject()(override val repository: FormRepository)
  extends BaseEntityService[FormTable, Form, FormRepository] {

}