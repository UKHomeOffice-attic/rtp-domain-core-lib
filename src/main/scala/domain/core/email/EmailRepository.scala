package domain.core.email

import com.mongodb.casbah.Imports
import com.mongodb.casbah.Imports._
import com.mongodb.casbah.commons.MongoDBObject
import domain.core.mongo.{CasbahRepository, MongoConnector}
import grizzled.slf4j.Logging
import org.bson.types.ObjectId
import org.joda.time.DateTime

class EmailRepository(val mc: MongoConnector) extends CasbahRepository with Logging {
  val collectionName = "email"

  val MAX_LIMIT = 100

  def insert(email: Email) = collection.insert(email.toDBObject)

  def findByEmailId(emailId: String): Option[Email] = {
    collection.findOne(MongoDBObject(Email.EMAIL_ID -> new ObjectId(emailId))) match {
      case Some(dbo) => Some(Email(dbo))
      case None => None
    }
  }

  def findByCaseId(caseId: String): List[Email] = {
    val emailCursor = collection.find(MongoDBObject(Email.CASE_ID -> new ObjectId(caseId))).sort(orderBy = MongoDBObject(Email.DATE -> -1)).toList
    for {x <- emailCursor} yield {
      Email(x)
    }
  }

  def findByCaseIdAndType(caseId: String, emailType: String): List[Email] = {
    val emailCursor = collection.find(MongoDBObject(Email.CASE_ID -> new ObjectId(caseId),
      Email.TYPE -> emailType)).toList
    for {x <- emailCursor} yield {
      Email(x)
    }
  }

  def findForCasesAndEmailTypes(caseIds: Iterable[ObjectId], emailTypes: Seq[String]): List[Email] = {
    val emailCursor = collection.find(byCaseIdsAndEmailTypes(caseIds, emailTypes)).toList
    for {x <- emailCursor} yield {
      Email(x)
    }
  }


  def findEmailTypesAndCaseIds(caseIds: Iterable[ObjectId], emailTypes: Seq[String]): List[(String, ObjectId)] =
    (for {
      item <- collection.find(byCaseIdsAndEmailTypes(caseIds, emailTypes), MongoDBObject(Email.CASE_ID -> 1, Email.TYPE -> 1))
      emailType <- item.getAs[String](Email.TYPE)
      caseId <- item.getAs[ObjectId](Email.CASE_ID)
    } yield {
        (emailType, caseId)
      }) toList


  def byCaseIdsAndEmailTypes(caseIds: Iterable[ObjectId], emailTypes: Seq[String]): Imports.DBObject = {
    $and(Email.CASE_ID $in caseIds, Email.TYPE $in emailTypes)
  }


  def findByStatus(emailStatus: String): List[Email] = {
    val emailCursor = collection.find(byEmailStatus(emailStatus)).limit(MAX_LIMIT).toList
    for {x <- emailCursor} yield {
      Email(x)
    }
  }

  def byEmailStatus(emailStatus: String): Imports.DBObject = {
    MongoDBObject(Email.STATUS -> emailStatus)
  }

  def resend(emailId: String): Email = {
    val email = findByEmailId(emailId)
    val newEmail = email.get.copy(emailId = new ObjectId().toString, date = new DateTime, status = EmailStatus.STATUS_WAITING)
    insert(newEmail)
    newEmail
  }

  def resend(emailId: String, recipient: String): Email = {
    val email = findByEmailId(emailId)

    val newEmail = email.get.copy(
      emailId = new ObjectId().toString,
      recipient = recipient,
      date = new DateTime,
      status = EmailStatus.STATUS_WAITING)

    insert(newEmail)
    newEmail
  }

  def updateStatus(emailId: String, newStatus: String) =
    collection.update(MongoDBObject(Email.EMAIL_ID -> new ObjectId(emailId)), $set(Email.STATUS -> newStatus))

  def removeByCaseId(caseId: String): Unit =
    collection.remove(MongoDBObject(Email.CASE_ID -> new ObjectId(caseId)))
}