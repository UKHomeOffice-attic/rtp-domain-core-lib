package domain.core.mongo

import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.{MongoCollection, MongoDB}

trait CasbahRepository {
  val mc: MongoConnector

  val collectionName: String

  lazy val db: MongoDB = mc.db

  lazy val collection: MongoCollection = db(collectionName)

  def removeAll() = collection.remove(MongoDBObject.empty)

  def drop() = collection.drop()
}