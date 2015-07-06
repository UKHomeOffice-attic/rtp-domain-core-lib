package domain.core.mongo

import org.specs2.mutable.Specification

class WithEmbeddedMongoSpec extends Specification with WithEmbeddedMongo {
  "Mongo" should {
    "show when connection is closed" in {
      closeMongo
      mongoClient.databaseNames() must throwAn[IllegalStateException]
    }
  }
}