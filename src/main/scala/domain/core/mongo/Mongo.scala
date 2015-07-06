package domain.core.mongo

import scala.collection.mutable
import scala.util.Try
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import grizzled.slf4j.Logging

trait Mongo {
  lazy val db: MongoDB = Mongo.mongodb(mongoConnectionUri)

  def mongoConnectionUri: String
}

object Mongo extends Logging {
  // TODO - Remove these debug statements
  println(s"=============================================================================================================================")
  println(s"MONGO WAS CALLED - Left this debug statement in until all specs stop interacting with real database as well as test databases")
  println(s"=============================================================================================================================")

  RegisterJodaTimeConversionHelpers()

  val mongodbs = mutable.Map.empty[String, MongoDB]

  val mongodb: String => MongoDB = uri => mongodbs.getOrElse(uri, {
    val mongoClientURI = MongoClientURI(uri)
    val database = mongoClientURI.database.getOrElse(throw new Exception(s"No database configured to connect to ${mongoClientURI.hosts.mkString}"))
    val mongoConnectionStats = s"database '$database' on ${mongoClientURI.hosts.mkString}"

    val mongoClient = MongoClient(mongoClientURI)
    info(s"Instantiated mongo client and connected to $mongoConnectionStats")

    sys addShutdownHook {
      closeAll()
      // mongoClient.close()
    }

    val m = mongoClient(database)
    mongodbs += uri -> m
    m
  })

  def closeAll() = mongodbs.values.foreach { m =>
    info(s"Disconnecting mongo client from ${m.getStats()}")
    Try { m.underlying.getMongo.close() }
  }
}

// TODO Get rid of the following (this is all that remains of the old (incorrect) code) - upon deletion, just mixin above Mongo trait.
class MongoConnector(val mongoConnectionUri: String) extends Mongo