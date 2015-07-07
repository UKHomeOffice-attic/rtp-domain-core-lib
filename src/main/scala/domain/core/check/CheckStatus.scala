package domain.core.check

import com.novus.salat.annotations._

@Salat
trait CheckStatus {
  val displayName: String

  @Persist val name: String = getClass.getSimpleName.dropRight(1)
}

object CheckStatus {
  val allStatuses = Seq(CHECK_NOT_STARTED, CHECK_IN_PROGRESS, CHECK_ACCEPTED, CHECK_AWAITING_INFO, CHECK_REJECTED, CHECK_INVESTIGATE, CHECK_ESCALATED)

  def forName(name: String): Option[CheckStatus] =
    allStatuses.find(caseStatus => caseStatus.name.equals(name) || caseStatus.displayName.equals(name))
}

case object CHECK_NOT_STARTED extends CheckStatus { override val displayName = "Not Started" }

case object CHECK_IN_PROGRESS extends CheckStatus { override val displayName = "In Progress" }

case object CHECK_ACCEPTED extends CheckStatus { override val displayName = "Accepted" }

case object CHECK_REJECTED extends CheckStatus { override val displayName = "Rejected" }

case object CHECK_AWAITING_INFO extends CheckStatus { override val displayName = "Awaiting Info" }

case object CHECK_INVESTIGATE extends CheckStatus { override val displayName = "Investigate" }

case object CHECK_ESCALATED extends CheckStatus { override  val displayName = "Escalated" }