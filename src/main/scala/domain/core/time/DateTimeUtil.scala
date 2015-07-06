package domain.core.time

import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, Duration, Interval}

object DateTimeUtil {
  val dayFormat = DateTimeFormat.forPattern("dd/MM/yyyy")
  val dayTimeFormat = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm:ss")
  val dateHHmmFormat = DateTimeFormat.forPattern("dd/MM/yyyy HH:mm")
  val dayMonthYearTextFormat =  DateTimeFormat.forPattern("EEEE d MMMM yyyy")

  implicit class BetterDateTime(val dt: DateTime) {
    def formatAsDay = formatDay(dt)
    def format(pattern: String) = DateTimeFormat.forPattern(pattern).print(dt)
    def formatAsDayTime = formatDayTime(dt)
    def formatAsDateHHmm = formatDateHHmm(dt)
    def formatAsDayMonthYearText = formatDayMonthYearText(dt)


    def formatDayMonthYearText(date: DateTime) = dayMonthYearTextFormat.print(date)
    def formatDay(date: DateTime) = dayFormat.print(date)
    def formatDayTime(date: DateTime) = dayTimeFormat.print(date)
    def formatDateHHmm(date: DateTime) = dateHHmmFormat.print(date)
  }



  //TODO Ask Ryan what this is all about???
  def compareDatesOnly(caseDate: DateTime, renewalDate: DateTime): Boolean = new Interval(caseDate.minusHours(3), caseDate.plusHours(3)).contains(renewalDate)

  def durationInStandardDays( startDate: DateTime, endDate: DateTime) = new Duration(startDate, endDate).getStandardDays
}