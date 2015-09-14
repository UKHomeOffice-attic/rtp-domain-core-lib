package domain.core.email

import domain.core.mongo.WithMongo
import org.bson.types.ObjectId
import org.joda.time.DateTime
import org.specs2.mock.Mockito
import org.specs2.mutable.Specification
import domain.core.email.EmailStatus._

class EmailRepositorySpec extends Specification with Mockito with WithMongo {
  val repository = new EmailRepository(mongoConnector)
  val PROVISIONAL_ACCEPTANCE = "provisional-acceptance"
  val FAILED_CREDIBILITY_CHECK = "failed-credibility-check"
  val MEMBERSHIP_EXPIRES_SOON = "expiring soon"

  def insertEmail(caseId: Option[ObjectId] = Some(new ObjectId()), emailType: String = PROVISIONAL_ACCEPTANCE, html: String = "html") = {
    val email = Email(
      caseId = if (caseId.isEmpty) None else Some(caseId.get.toString),
      date = new DateTime(),
      recipient = caseId + " recipient",
      subject = "subject",
      text = "text",
      html = html,
      status = STATUS_WAITING,
      emailType = emailType)

    repository.insert(email)
    email
  }

  "email repository" should {
    "find Email by caseId" in {
      val email = insertEmail()
      val emailDocuments = repository.findByCaseId(email.caseId.get.toString)

      emailDocuments.size mustEqual 1
      emailDocuments.head.recipient mustEqual s"${email.caseId} recipient"
    }

    "find Emails by caseId and emailType" in {
      insertEmail(emailType = PROVISIONAL_ACCEPTANCE)
      val failedCredibilityEmail = insertEmail(emailType = FAILED_CREDIBILITY_CHECK)
      val emailDocuments = repository.findByCaseIdAndType(failedCredibilityEmail.caseId.get.toString, FAILED_CREDIBILITY_CHECK)

      emailDocuments.size mustEqual 1
      emailDocuments.head.recipient mustEqual s"${failedCredibilityEmail.caseId} recipient"
    }

    "find Email by id" in {
      val email = insertEmail()
      val persistedEmail = repository.findByEmailId(email.emailId)
      persistedEmail.get.recipient mustEqual s"${email.caseId} recipient"
    }

    "find emails by email Id without a caseId" in {
      val emailObj = insertEmail(caseId = None)
      val email = repository.findByEmailId(emailObj.emailId)
      email.get.recipient mustEqual s"${emailObj.caseId} recipient"
    }

    "find emails by status" in {
      val emailObj = insertEmail()
      val emailDocs = repository.findByStatus(STATUS_WAITING)
      emailDocs.size mustEqual 1
      emailDocs.head.emailId mustEqual emailObj.emailId
    }
  }

  "findNextEmailToSend" should {
    "find the next email and set it to be IN_PROGRESS" in {
      val emailObj = insertEmail()

      repository.findByEmailId(emailId = emailObj.emailId).get.status must_== STATUS_WAITING

      val emailToSend: Option[Email] = repository.nextEmailToSend
      emailToSend.get must_==emailObj
      repository.findByEmailId(emailId = emailObj.emailId).get.status must_== STATUS_IN_PROGRESS
    }
  }

  "updateStatus" should {
    "update Email status" in {
      val emailObj = insertEmail()
      repository.updateStatus(emailObj.emailId, STATUS_SENT)
      val Some(updatedEmail) = repository.findByEmailId(emailObj.emailId)
      updatedEmail.status mustEqual STATUS_SENT
    }
  }

  "resend" should {
    "create a new Email" in {
      val emailObj = insertEmail()
      val newEmail = repository.resend(emailObj.emailId)
      val Some(foundEmail) = repository.findByEmailId(newEmail.emailId)
      foundEmail.status mustEqual STATUS_WAITING
    }

    "create a new Email with new recipient" in {
      val emailObj = insertEmail()
      val newEmail = repository.resend(emailObj.emailId, "peppa pig")
      val Some(foundEmail) = repository.findByEmailId(newEmail.emailId)
      foundEmail.status mustEqual STATUS_WAITING
      foundEmail.recipient mustEqual "peppa pig"
    }
  }

  "insert record" should {
    "contain the correct html" in {
      val templateHtml = "<html><title>Hello</title></html>"
      val emailObj = insertEmail(html = templateHtml)
      val foundEmail = repository.findByEmailId(emailObj.emailId)
      foundEmail.get.html mustEqual templateHtml
    }
  }

  "remove by Case Id" should {
    "remove an email associated with a case" in {
      val allCaseIds = (1 to 3).map { _ =>
        val email = insertEmail()
        email.caseId.get.toString
      }

      repository.removeByCaseId(allCaseIds.head)

      repository.findByCaseId(allCaseIds.head).size mustEqual 0
      repository.findByCaseId(allCaseIds(1)).size mustEqual 1
      repository.findByCaseId(allCaseIds(2)).size mustEqual 1
    }
  }

  "Find warning emails for cases" should {
    "Return all warning emails for each case" in {
      val _caseId= new ObjectId()

      val warningEmailTypes = Seq(PROVISIONAL_ACCEPTANCE, FAILED_CREDIBILITY_CHECK, MEMBERSHIP_EXPIRES_SOON)

      insertEmail(caseId = Some(_caseId), emailType = "Ignored Email Type")  // should be ignored in the query below
      val provisionalAcceptanceEmail = insertEmail(caseId = Some(_caseId), emailType = PROVISIONAL_ACCEPTANCE)
      val failedCredibilityEmail = insertEmail(caseId = Some(_caseId), emailType = FAILED_CREDIBILITY_CHECK)
      val membershipExpiresSoonEmail = insertEmail(caseId = Some(_caseId), emailType = MEMBERSHIP_EXPIRES_SOON)

      val emails = repository.findForCasesAndEmailTypes(Seq(_caseId), warningEmailTypes)

      emails mustEqual Seq(provisionalAcceptanceEmail, failedCredibilityEmail, membershipExpiresSoonEmail)
    }
  }
}