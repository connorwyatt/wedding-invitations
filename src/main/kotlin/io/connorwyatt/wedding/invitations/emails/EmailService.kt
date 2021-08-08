package io.connorwyatt.wedding.invitations.emails

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.Response
import com.sendgrid.SendGridAPI
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class EmailService(private val sendGridApi: SendGridAPI) {
  fun sendInvitationEmail(emailAddress: String, addressedTo: String, invitationUrl: String) {
    val fromEmailAddress = Email("wedding@connorwyatt.io", "Connor & Laura's Wedding")
    val replyToEmailAddress = Email("connorwyatt1@gmail.com", "Connor Wyatt")
    val subject = "You're invited to our wedding!"
    val toEmailAddress = Email(emailAddress)
    val content = Content("text/html", "You're invited to our wedding!")
    val mail = Mail(fromEmailAddress, subject, toEmailAddress, content).apply {
      replyTo = replyToEmailAddress
      setTemplateId("d-87f02d363c074961b88e7b36a28d33c5")
      addCustomArg("addressedTo", addressedTo)
      addCustomArg("invitationUrl", invitationUrl)
      addPersonalization(Personalization().apply {
        addTo(toEmailAddress)
        addDynamicTemplateData("addressedTo", addressedTo)
        addDynamicTemplateData("invitationUrl", invitationUrl)
      })
    }

    logger.info(
      "Sending invitation email to \"{}\" addressed to \"{}\" with invitation URL \"{}\", body \"{}\"",
      emailAddress,
      addressedTo,
      invitationUrl,
      mail.build()
    )
    sendMail(mail)
    logger.info("Successfully sent invitation email to \"{}\"", emailAddress)
  }

  private fun sendMail(mail: Mail): Response {
    val request = Request()
    request.method = Method.POST
    request.endpoint = "mail/send"
    request.body = mail.build()
    return sendGridApi.api(request)
  }

  companion object {
    private val logger: Logger = LoggerFactory.getLogger(EmailService::class.java)
  }
}
