package io.connorwyatt.wedding.invitations.domain

import io.connorwyatt.wedding.invitations.messages.commands.CreateInvitation
import io.connorwyatt.wedding.invitations.messages.events.InvitationCreated
import io.connorwyatt.wedding.invitations.messages.events.InviteeAdded
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.util.UUID

@Aggregate
class Invitation {
  @AggregateIdentifier
  private lateinit var invitationId: String

  constructor()

  @CommandHandler
  constructor(command: CreateInvitation) {
    invitationId = UUID.randomUUID().toString()

    AggregateLifecycle.apply(InvitationCreated(invitationId, command.code, command.emailAddress))

    command.invitees.forEach { invitee ->
      AggregateLifecycle.apply(InviteeAdded(invitationId, UUID.randomUUID().toString(), invitee.name))
    }
  }

  @EventSourcingHandler
  fun on(event: InvitationCreated) {
    invitationId = event.invitationId
  }

  @EventSourcingHandler
  fun on(event: InviteeAdded) {
  }
}
