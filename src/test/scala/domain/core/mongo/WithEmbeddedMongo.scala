package domain.core.mongo

import com.github.fakemongo.Fongo
import com.mongodb.casbah.MongoDB
import org.specs2.mutable.Specification

trait WithEmbeddedMongo extends WithMongo {
  this: Specification =>

  isolated

  val fongo = new Fongo(mongoClientURI.getURI)

  override lazy val mongoConnector = new MongoConnector(mongoClientURI.getURI) {
    override lazy val db: MongoDB = new MongoDB(fongo.getDB(database))
  }
}