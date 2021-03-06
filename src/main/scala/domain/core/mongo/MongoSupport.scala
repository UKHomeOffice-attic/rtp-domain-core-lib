package domain.core.mongo

import org.bson.types.ObjectId
import org.json4s._
import org.json4s.native.JsonMethods._
import com.mongodb.DBObject
import com.mongodb.casbah.commons.MongoDBObject
import com.mongodb.casbah.commons.conversions.scala.RegisterJodaTimeConversionHelpers
import com.mongodb.util.JSON

trait MongoSupport {
  RegisterJodaTimeConversionHelpers()

  type UpdateCount = Int

  implicit val stringToObjectId: String => ObjectId =
    s => new ObjectId(s)

  implicit val objectIdToString: ObjectId => String =
    oid => oid.toHexString

  val all = MongoDBObject()

  implicit def toDBObject(json: JValue)(implicit formats: Formats = DefaultFormats): DBObject = {
    // JObjectParser.parse(json) Wanted to only use this line (instead of next 2) but is not correctly handling $date
    val dbObject = JSON.parse(compact(render(json)))
    dbObject.asInstanceOf[DBObject]
  }

  implicit def asJson(dbObject: DBObject)(implicit formats: Formats = DefaultFormats): JValue = parse(dbObject.toString)

  implicit class ObjectIdOps(_id: ObjectId) {
    def ~(j: JValue): JValue = merge(j)

    def merge(j: JValue): JValue = parse(s"""{ "_id" : { "$$oid" : "${_id}" } }""") merge j
  }

  implicit class MapDBObjectOps(m: Map[String, DBObject]) {
    def merge(kv: (String, DBObject)*): Map[String, DBObject] = kv.foldLeft(m) { case (map, (k, v)) =>
      map + map.get(k).map { o =>
        k -> (new MongoDBObject(o) ++ v)
      }.getOrElse(k -> v)
    }

    def merge(other: Map[String, DBObject]): Map[String, DBObject] = merge(other.toSeq: _*)
  }
}