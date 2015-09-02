package domain.core.handofffiles

import com.mongodb.casbah.commons.MongoDBObject
import domain.core.mongo.WithEmbeddedMongo
import org.bson.types.ObjectId
import org.joda.time.DateTime
import org.specs2.mutable.Specification

class HandoffFileRepositorySpec extends Specification with WithEmbeddedMongo {
  val repository = new HandoffFileRepository(mongoConnector)
  
  "removeOlderThanDays" should {
    "remove handoff files and chunks older than 1 day" in {
      val objectId = new ObjectId()
      repository.collection.insert(MongoDBObject("_id" -> objectId, "uploadDate" -> DateTime.now.minusDays(2)))
      repository.handoffChunksRepository.collection.insert(MongoDBObject("files_id" -> objectId))

      repository.removeOlderThanDays(1)

      repository.collection.size mustEqual 0
      repository.handoffChunksRepository.collection.size mustEqual 0
    }

    "not remove handoff files and chunks created under 1 day" in {
      val objectId = new ObjectId()
      repository.collection.insert(MongoDBObject("_id" -> objectId, "uploadDate" -> DateTime.now))
      repository.handoffChunksRepository.collection.insert(MongoDBObject("files_id" -> objectId))

      repository.removeOlderThanDays(1)

      repository.collection.size mustEqual 1
      repository.handoffChunksRepository.collection.size mustEqual 1
    }
  }
}