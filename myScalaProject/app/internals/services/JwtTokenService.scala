package internals.services

import javax.inject.Inject
import internals.{JwtToken, JwtTokenTable}
import internals.repositories.JwtTokenRepository

class JwtTokenService @Inject()( override val repository: JwtTokenRepository)
  extends BaseEntityService[JwtTokenTable, JwtToken, JwtTokenRepository] {


}
