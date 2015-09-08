package domain.core.account

import com.novus.salat.annotations._

@Salat
sealed trait Permission {
  @Persist val name: String
}

object Permission {
  val allPermissions = Seq(Administrator, AdministratorManager, Manager, SeniorCaseworker, Caseworker,
                           TRAVEL_HISTORY_PERMISSION, WI_CROSSCHECK_PERMISSION, OGD_DOWNLOAD_PERMISSION, PNC_CROSSCHECK_PERMISSION, WICU_ARRIVALS_PERMISSION, DAILY_WASH_PERMISSION, GREENLIST_PERMISSION, WICU_CIO_PERMISSION,
                           CustomerService)

  def valueOf(permission: String): Permission = permission match {
    case Caseworker.name => Caseworker
    case Manager.name => Manager
    case AdministratorManager.name => AdministratorManager
    case Administrator.name => Administrator
    case Tester.name => Tester
    case SeniorCaseworker.name => SeniorCaseworker
    case TRAVEL_HISTORY_PERMISSION.name => TRAVEL_HISTORY_PERMISSION
    case WI_CROSSCHECK_PERMISSION.name => WI_CROSSCHECK_PERMISSION
    case OGD_DOWNLOAD_PERMISSION.name => OGD_DOWNLOAD_PERMISSION
    case PNC_CROSSCHECK_PERMISSION.name => PNC_CROSSCHECK_PERMISSION
    case GREENLIST_PERMISSION.name => GREENLIST_PERMISSION
    case DAILY_WASH_PERMISSION.name => DAILY_WASH_PERMISSION
    case WICU_ARRIVALS_PERMISSION.name => WICU_ARRIVALS_PERMISSION
    case WICU_CIO_PERMISSION.name => WICU_CIO_PERMISSION
    case Smoke.name => Smoke
    case CustomerService.name => CustomerService
    case _ => null
//    case _ => throw new IllegalArgumentException(s"Invalid permission: $permission")
  }
}

case object Administrator extends Permission {
  override val name: String = "Administrator"
}

case object Manager extends Permission {
  override val name: String = "Manager"
}

case object AdministratorManager extends Permission {
  override val name: String = "AdministratorManager"
}

case object Caseworker extends Permission {
  override val name: String = "Caseworker"
}

case object CustomerService extends Permission {
  override val name: String = "CustomerService"
}

case object Tester extends Permission {
  override val name: String = "Tester"
}

case object SeniorCaseworker extends Permission {
  override val name: String = "Senior Caseworker"
}

case object TRAVEL_HISTORY_PERMISSION extends Permission {
  override val name: String = "TravelHistory"
}

case object WI_CROSSCHECK_PERMISSION extends Permission {
  override val name: String = "WICrossCheck"
}

case object OGD_DOWNLOAD_PERMISSION extends Permission {
  override val name: String = "OGDDownload"
}

case object PNC_CROSSCHECK_PERMISSION extends Permission {
  override val name: String = "PNCCrossCheck"
}

case object WICU_ARRIVALS_PERMISSION extends Permission {
  override val name: String = "WICUArrivals"
}

case object GREENLIST_PERMISSION extends Permission {
  override val name: String = "GreenList"
}

case object WICU_CIO_PERMISSION extends Permission {
  override val name: String = "WICUCIO"
}

case object DAILY_WASH_PERMISSION extends Permission {
  override val name: String = "DailyWash"
}

case object NO_PERMISSION extends Permission {
  override val name: String = "no permission to look at anything"
}

case object Smoke extends Permission {
  override val name: String = "Smoke"
}

case object LoggedIn extends Permission {
  override val name: String = "User is logged in - do not persist!"
}

case object CaseworkerReadOnly extends Permission {
  override val name: String = "CaseworkerReadOnly"
}