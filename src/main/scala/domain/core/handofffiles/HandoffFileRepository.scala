package domain.core.handofffiles

import com.mongodb.casbah.commons.MongoDBObject
import domain.core.mongo.{CasbahRepository, MongoConnector}
import grizzled.slf4j.Logging
import org.bson.types.ObjectId
import org.joda.time.DateTime

class HandoffFileRepository(val mc: MongoConnector) extends CasbahRepository with Logging {

  val collectionName = "handoff.files"
  val handoffChunksRepository = new HandoffChunksRepository(mc)

  def removeOlderThanDays(days: Int) {

    val filesOlderThanDays = collection.find(MongoDBObject("uploadDate" -> MongoDBObject("$lt" -> DateTime.now.minusDays(days)))).toList
    filesOlderThanDays.foreach(file => {
     val filesId =  file.get("_id").asInstanceOf[ObjectId]
     handoffChunksRepository.removeByFilesId(filesId)
     collection.remove(MongoDBObject("_id" -> filesId))
    })
  }
}

class HandoffChunksRepository(val mc: MongoConnector) extends CasbahRepository with Logging {

  val collectionName = "handoff.chucks"

  def removeByFilesId(filesId: ObjectId) {
    collection.remove(MongoDBObject("files_id" -> filesId))
  }
}



