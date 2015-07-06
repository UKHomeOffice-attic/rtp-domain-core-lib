package domain.core.mongo

import java.util.UUID

import com.mongodb.casbah.commons.conversions.scala.{RegisterConversionHelpers, RegisterJodaTimeConversionHelpers}
import com.mongodb.casbah.{MongoClient, MongoClientURI}
import domain.core.mongo.{MongoConnector, Mongo}
import org.specs2.execute.{AsResult, Result}
import org.specs2.mutable.Specification
import org.specs2.specification.AroundEach

import scala.util.Try

/**
 * Not nice to name a trait prefixed by "With" as it will probably mixed in using "with".
 * However, this seems to be a naming idiom (certainly from Play) to distinguish this trait that is only for testing as opposed to say main code named "Mongo"
 */
trait WithMongo extends Mongo with AroundEach {
  this: Specification =>

  isolated

  val server = "127.0.0.1"

  val port = 27017

  val database = s"test-${UUID.randomUUID()}"

  val connectTimeoutMS = 10000

  val mongoConnectionUri = s"mongodb://$server:$port/$database?connectTimeoutMS=$connectTimeoutMS"

  lazy val mongoClientURI = MongoClientURI(mongoConnectionUri)

  lazy val mongoClient = MongoClient(mongoClientURI)

  lazy val mongoConnector = new MongoConnector(mongoClientURI.getURI) {
    override lazy val db = mongoClient(database)
  }

  lazy val mongoConfiguration: Map[String, String] = Map(
    "mongodb.uri" -> s"mongodb://$server:$port/$database",
    "caseworkerdomain.mongodb" -> s"mongodb://$server:$port/$database",
    "memcachedplugin" -> "disabled")

  override def around[T: AsResult](t: => T): Result = try {
    debug(s"+ Created $database in spec $getClass")
    AsResult(t)
  } finally {
    debug(s"x Dropping $database")
    closeMongo
  }

  override def finalize() = {
    closeMongo
    super.finalize()
  }

  def closeMongo = {
    Try { mongoClient.dropDatabase(database) }
    Try { mongoClient.close() }
  }

  RegisterConversionHelpers()
  RegisterJodaTimeConversionHelpers()
}