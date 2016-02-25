package domain.core.account

case class Account(email: String, firstName: String, surname: String, permission: Permission) {
  def name = s"$firstName $surname"

  def isManager = permission == Manager || permission == AdministratorManager

  def isAdministrator = permission == Administrator || permission == AdministratorManager || isTester

  def isCaseworker = permission == Caseworker

  def isGreenList = permission == GREENLIST_PERMISSION

  def isWICUArrivals = permission == WICU_ARRIVALS_PERMISSION

  def isWIAndPNCCrossCheck = permission == WI_CROSSCHECK_PERMISSION

  def isWICUCIO = permission == WICU_CIO_PERMISSION

  def isDailyWash = permission == DAILY_WASH_PERMISSION

  def isCustomerService = permission == CustomerService

  def isTester = permission == Tester

  def isSeniorCaseworker = permission == SeniorCaseworker

  def isManagerOrTester = isManager || isTester

  def isSeniorCaseworkerOrManager = isManagerOrTester || isSeniorCaseworker

  def isMida = permission == MIDA_PERMISSION
}

object Account {
  def apply(email: String, firstName: String, surname: String, permission: String): Account =
    Account(email: String, firstName: String, surname: String, Permission.valueOf(permission))

  def unapply2(account: Account): Option[(String, String, String, String)] = {
    val permission = account.permission match {
      case p @ (Caseworker | Manager | Administrator) => p.name
      case _ => ""
    }
    Some((account.email, account.firstName, account.surname, permission))
  }
}