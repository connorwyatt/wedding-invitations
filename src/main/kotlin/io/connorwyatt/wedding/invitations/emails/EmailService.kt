package io.connorwyatt.wedding.invitations.emails

import com.sendgrid.Method
import com.sendgrid.Request
import com.sendgrid.SendGridAPI
import com.sendgrid.helpers.mail.Mail
import com.sendgrid.helpers.mail.objects.Content
import com.sendgrid.helpers.mail.objects.Email
import com.sendgrid.helpers.mail.objects.Personalization
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
      addPersonalization(Personalization().apply {
        addTo(toEmailAddress)
        addDynamicTemplateData("addressedTo", addressedTo)
        addDynamicTemplateData("invitationUrl", invitationUrl)
      })
    }

    sendMail(mail)
  }

  private fun sendMail(mail: Mail) {
    val request = Request()
    request.method = Method.POST
    request.endpoint = "mail/send"
    request.body = mail.build()
    sendGridApi.api(request)
  }
}
