package io.connorwyatt.wedding.invitations.domain

import io.connorwyatt.wedding.invitations.WebAppProperties
import io.connorwyatt.wedding.invitations.emails.EmailService
import io.connorwyatt.wedding.invitations.messages.commands.SendInvitationEmail
import io.connorwyatt.wedding.invitations.sql.SqlInvitationsRepository
import kotlinx.coroutines.runBlocking
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.modelling.command.Repository
import org.springframework.stereotype.Component

@Component
class SendInvitationEmailHandler(
  private val aggregateRepository: Repository<Invitation>,
  private val repository: SqlInvitationsRepository,
  private val emailService: EmailService,
  private val webAppProperties: WebAppProperties,
) {
  @CommandHandler
  fun handle(command: SendInvitationEmail) {
    runBlocking {
      val invitation = repository.getById(command.invitationId)
        ?: throw Exception("Invitation could not be found.")

      if (invitation.emailSent) {
        throw Exception("Email has already been sent.")
      }

      val emailAddress = invitation.emailAddress
        ?: throw Exception("Cannot send email for an invitation with no email address.")

      emailService.sendInvitationEmail(
        emailAddress,
        invitation.addressedTo,
        "${webAppProperties.invitationUrlPrefix}${invitation.code}"
      )

      aggregateRepository.load(command.invitationId)
        .execute { it.acknowledgeInvitationEmailSent() }
    }
  }
}
