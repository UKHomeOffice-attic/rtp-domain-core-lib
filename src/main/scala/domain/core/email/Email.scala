package domain.core.email

import com.mongodb.casbah.query.BaseImports
import com.mongodb.{BasicDBList, DBObject}
import com.mongodb.casbah.commons.{MongoDBList, MongoDBObject}
import org.bson.types.ObjectId
import org.joda.time.DateTime

case class Email(emailId: String = new ObjectId().toString,
                 caseId: Option[String],
                 date: DateTime,
                 recipient: String,
                 subject: String,
                 text: String,
                 html: String,
                 status: String,
                 emailType: String,
                 cc: List[String] = List.empty) {

  def toDBObject: DBObject = {
    val builder = MongoDBObject.newBuilder
    builder += Email.EMAIL_ID -> new ObjectId(emailId)
    caseId.map(id => builder += Email.CASE_ID -> new ObjectId(id))
    builder += Email.DATE -> date
    builder += Email.RECIPIENT -> recipient
    builder += Email.SUBJECT -> subject
    builder += Email.TEXT -> text
    builder += Email.HTML -> html
    builder += Email.STATUS -> status
    builder += Email.TYPE -> emailType
    builder += Email.CC -> MongoDBList(cc:_*).underlying
    builder.result()
  }
}

object Email {
  val EMAIL_ID: String = "_id"
  val CASE_ID: String = "caseId"
  val DATE: String = "date"
  val RECIPIENT: String = "recipient"
  val SUBJECT: String = "subject"
  val TEXT: String = "text"
  val HTML: String = "html"
  val STATUS: String = "status"
  val TYPE: String = "type"
  val CC: String = "cc"

  def apply(dbObject: DBObject): Email = {
    new Email(dbObject.get(EMAIL_ID).asInstanceOf[ObjectId].toString,
      if (dbObject.containsField(CASE_ID)) Some(dbObject.get(CASE_ID).asInstanceOf[ObjectId].toString) else None,
      dbObject.get(DATE).asInstanceOf[DateTime],
      dbObject.get(RECIPIENT).asInstanceOf[String],
      dbObject.get(SUBJECT).asInstanceOf[String],
      dbObject.get(TEXT).asInstanceOf[String],
      dbObject.get(HTML).asInstanceOf[String],
      dbObject.get(STATUS).asInstanceOf[String],
      dbObject.get(TYPE).asInstanceOf[String],
      if (dbObject.containsField(CC)) dbObject.get(CC).asInstanceOf[BasicDBList].toArray(Array.empty[String]).toList else List.empty
    )
  }
}

object EmailStatus {
  val STATUS_WAITING: String = "WAITING"
  val STATUS_SENT: String = "SENT"
  val STATUS_ERROR: String = "ERROR"
  val STATUS_EMAIL_ADDRESS_ERROR: String = "ERROR - Email Address"
}
