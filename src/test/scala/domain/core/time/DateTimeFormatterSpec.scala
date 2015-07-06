package domain.core.time

import domain.core.time.DateTimeUtil
import org.joda.time.DateTime
import org.scalatest.{MustMatchers, WordSpec}
import DateTimeUtil._

class DateTimeFormatterSpec extends WordSpec with MustMatchers {
  "formatAsDay " should {
    "return DateTime in dd/mm/yyyy format" in {
      val dateOfBirth: DateTime = new DateTime(1975, 1, 1, 0, 0, 0, 0)
      dateOfBirth.formatAsDay must equal ("01/01/1975")
    }
  }

  "formatAsDayTime " should {
    "return DateTime in dd/mm/yyyy format" in {
      val dateOfBirth: DateTime = new DateTime(1975, 1, 1, 10, 30, 20, 0)
      dateOfBirth.formatAsDayTime must equal ("01/01/1975 10:30:20")
    }
  }

  "formatAsDayMonthYearText " should {
    "return DateTime in Day d Month Year like Tuesday 7 April 2015 format" in {
      val dateOfBirth: DateTime = new DateTime(2015, 4, 7, 0, 0, 0, 0)
      dateOfBirth.formatAsDayMonthYearText must equal ("Tuesday 7 April 2015")
    }
  }
}