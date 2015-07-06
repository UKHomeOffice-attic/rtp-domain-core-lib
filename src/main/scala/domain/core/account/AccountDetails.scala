package domain.core.account

case class AccountDetails(id: String, email: String, firstName: String, surname: String, permission: Permission, locked: Boolean) {
  def name = s"$firstName $surname"
}

object AccountDetails {
  val SYSTEM_ACCOUNT = AccountDetails("system", "system", "system", "account", Administrator, locked = true)

  def apply(id: String, email: String, firstName: String, surname: String, permission: String, locked: Boolean) =
    new AccountDetails(id = id, email = email, firstName = firstName, surname = surname,permission = Permission.valueOf(permission), locked = locked)

  def unapply2(account: AccountDetails): Option[(String, String, String, String, String, Boolean)] = {
    Some((account.id, account.email, account.firstName, account.surname, account.permission match {
      case permission: Permission => permission.name
    }, account.locked))
  }
}