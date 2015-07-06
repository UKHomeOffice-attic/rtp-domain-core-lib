package domain.core.mongo

import org.joda.time.DateTime
import com.mongodb.casbah.commons.MongoDBObject

trait MongoObjectDate {
  def mongoObjectFromAndToDateOptions(fromDate: Option[DateTime], toDate: Option[DateTime]) = {
    val mongoObject = if (fromDate.isDefined) {
      if (toDate.isEmpty)
        Some(MongoDBObject("$gte" -> fromDate.get))
      else
        Some(MongoDBObject("$gte" -> fromDate.get, "$lte" -> toDate.get))
    } else if (toDate.isDefined) {
      Some(MongoDBObject("$lte" -> toDate.get))
    }
    else {
      None
    }

    mongoObject
  }
}