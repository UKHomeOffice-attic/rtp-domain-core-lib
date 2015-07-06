package domain.core.mongo

import domain.core.mongo.MongoConnector
import org.specs2.mutable.Specification
import com.github.fakemongo.Fongo
import com.mongodb.casbah.MongoDB

trait WithEmbeddedMongo extends WithMongo {
  this: Specification =>

  isolated

  val fongo = new Fongo(mongoClientURI.getURI)

  override lazy val mongoConnector = new MongoConnector(mongoClientURI.getURI) {
    override lazy val db: MongoDB = new MongoDB(fongo.getDB(database))
  }
}