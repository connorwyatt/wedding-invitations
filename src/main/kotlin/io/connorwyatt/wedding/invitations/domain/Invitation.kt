package io.connorwyatt.wedding.invitations.domain

import io.connorwyatt.wedding.invitations.messages.commands.RespondToInvitation
import io.connorwyatt.wedding.invitations.messages.events.InvitationCreated
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
  private lateinit var invitationId: String

  private var inviteeIds = setOf<String>()

  private var responseReceived = false

  constructor()

  constructor(code: String, emailAddress: String?, invitees: List<InviteeDefinition>) {
    val invitationId = UUID.randomUUID().toString()

    apply(InvitationCreated(invitationId, code, emailAddress))

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

  @EventSourcingHandler
  fun on(event: InvitationCreated) {
    invitationId = event.invitationId
  }

  @EventSourcingHandler
  fun on(event: InviteeAdded) {
    inviteeIds = inviteeIds.plus(event.inviteeId)
  }

  @EventSourcingHandler
  fun on(event: InvitationResponseReceived) {
    responseReceived = true
  }
}
