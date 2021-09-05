package io.connorwyatt.wedding.invitations.domain

import io.connorwyatt.wedding.invitations.WebAppProperties
import io.connorwyatt.wedding.invitations.emails.EmailService
import io.connorwyatt.wedding.invitations.messages.commands.CreateInvitation
import io.connorwyatt.wedding.invitations.messages.commands.RespondToInvitation
import io.connorwyatt.wedding.invitations.messages.commands.SendInvitationEmail
import io.connorwyatt.wedding.invitations.messages.events.InvitationCreated
import io.connorwyatt.wedding.invitations.messages.events.InvitationEmailSent
import io.connorwyatt.wedding.invitations.messages.events.InvitationResponseReceived
import io.connorwyatt.wedding.invitations.messages.events.InviteeAdded
import io.connorwyatt.wedding.invitations.sql.SqlInvitationsRepository
import kotlinx.coroutines.runBlocking
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate
import java.util.UUID

@Aggregate
class Invitation {
  @AggregateIdentifier
  final lateinit var invitationId: String
    private set
  final lateinit var code: String
    private set
  final lateinit var addressedTo: String
    private set
  final var emailAddress: String? = null
    private set
  final var emailSent: Boolean = false
    private set
  final var inviteeIds = setOf<String>()
    private set
  final var responseReceived = false
    private set

  constructor()

  @CommandHandler
  constructor(command: CreateInvitation, repository: SqlInvitationsRepository) {
    runBlocking {
      command.code.let { code ->
        if (isCodeUsed(code, repository)) {
          throw Exception("Invitation already exists with code: ${code}.")
        }
      }

      command.emailAddress?.let { emailAddress ->
        if (isEmailAddressUsed(emailAddress, repository)) {
          throw Exception("Invitation already exists with email address: ${emailAddress}.")
        }
      }

      apply(InvitationCreated(
        command.invitationId,
        command.code,
        command.type,
        command.addressedTo,
        command.emailAddress))

      command.invitees.forEach { invitee ->
        apply(InviteeAdded(command.invitationId, UUID.randomUUID().toString(), invitee.name, invitee.requiresFood))
      }
    }
  }

  @CommandHandler
  fun handle(command: RespondToInvitation) {
    if (responseReceived) {
      throw Exception("Response already received.")
    }

    val responseInviteeIds = command.inviteeResponses.map { it.id }

    val inviteeCountsMatch = responseInviteeIds.count() == inviteeIds.count()

    if (!inviteeCountsMatch) {
      throw Exception("Invitee responses count does not match invitees count on invitation.")
    }

    val allInviteesResponded = responseInviteeIds.containsAll(inviteeIds)

    if (!allInviteesResponded) {
      throw Exception("Not all invitees have responded.")
    }

    apply(InvitationResponseReceived(invitationId, command.contactInformation, command.inviteeResponses))
  }

  @CommandHandler
  fun handle(
    command: SendInvitationEmail,
    repository: SqlInvitationsRepository,
    emailService: EmailService,
    webAppProperties: WebAppProperties,
  ) {
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

      apply(InvitationEmailSent(invitationId))
    }
  }

  @EventSourcingHandler
  fun on(event: InvitationCreated) {
    invitationId = event.invitationId
    code = event.code
    addressedTo = event.addressedTo
    emailAddress = event.emailAddress
  }

  @EventSourcingHandler
  fun on(event: InviteeAdded) {
    inviteeIds = inviteeIds.plus(event.inviteeId)
  }

  @EventSourcingHandler
  fun on(event: InvitationEmailSent) {
    emailSent = true
  }

  @EventSourcingHandler
  fun on(event: InvitationResponseReceived) {
    responseReceived = true
  }

  private suspend fun isCodeUsed(code: String, repository: SqlInvitationsRepository) =
    repository.search(code = code).any()

  private suspend fun isEmailAddressUsed(emailAddress: String, repository: SqlInvitationsRepository) =
    repository.search(emailAddress = emailAddress).any()

}
