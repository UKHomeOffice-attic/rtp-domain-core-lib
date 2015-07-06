package domain.core.time

import domain.core.time.DateTimeUtil
import org.joda.time.DateTime
import org.scalatest.{MustMatchers, WordSpec}

class DateTimeUtilSpec extends WordSpec with MustMatchers {
  "DateTimeUtil -> compareDatesOnly" should {
    "equal if dates is two hours in the future" in {
      val dateOfBirth: DateTime = new DateTime(1975, 1, 1, 0, 0, 0, 0)
      DateTimeUtil.compareDatesOnly(dateOfBirth, dateOfBirth.plusHours(2)) mustBe true
    }

    "equal if dates is two hours in the past" in {
      val dateOfBirth: DateTime = new DateTime(1975, 1, 1, 0, 0, 0, 0)
      DateTimeUtil.compareDatesOnly(dateOfBirth, dateOfBirth.minusHours(2)) mustBe true
    }

    "not equal if dates is more than two hours out" in {
      val dateOfBirth: DateTime = new DateTime(1975, 1, 1, 0, 0, 0, 0)
      DateTimeUtil.compareDatesOnly(dateOfBirth, dateOfBirth.plusHours(5)) mustBe false
    }

    "not equal if dates is more than two hours in the past" in {
      val dateOfBirth: DateTime = new DateTime(1975, 1, 1, 0, 0, 0, 0)
      DateTimeUtil.compareDatesOnly(dateOfBirth, dateOfBirth.minusHours(5)) mustBe false
    }
  }

  "DateTimeUtil -> duration in standard days" should {
    "calculate the duration between two given dates" in {
      //Given
      val endDate = DateTime.now
      val startDate = endDate.minusDays(60)
      //When
      val duration = DateTimeUtil.durationInStandardDays(startDate, endDate)
      //Then
      duration must be(60)
    }
  }
}