package io.connorwyatt.wedding.invitations.domain

import io.connorwyatt.wedding.invitations.messages.commands.RespondToInvitation
import io.connorwyatt.wedding.invitations.messages.events.InvitationCreated
import io.connorwyatt.wedding.invitations.messages.events.InvitationEmailSent
import io.connorwyatt.wedding.invitations.messages.events.InvitationResponseReceived
import io.connorwyatt.wedding.invitations.messages.events.InviteeAdded
import io.connorwyatt.wedding.invitations.messages.models.InviteeDefinition
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

  constructor(code: String, emailAddress: String?, addressedTo: String, invitees: List<InviteeDefinition>) {
    val invitationId = UUID.randomUUID().toString()

    apply(InvitationCreated(invitationId, code, addressedTo, emailAddress))

    invitees.forEach { invitee ->
      apply(InviteeAdded(invitationId, UUID.randomUUID().toString(), invitee.name))
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

    apply(InvitationResponseReceived(invitationId, command.inviteeResponses))
  }

  fun acknowledgeInvitationEmailSent() {
    apply(InvitationEmailSent(invitationId))
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
}
