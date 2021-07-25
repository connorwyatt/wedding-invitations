package io.connorwyatt.wedding.invitations.domain

import io.connorwyatt.wedding.invitations.messages.commands.CreateInvitation
import io.connorwyatt.wedding.invitations.messages.events.InvitationCreated
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle
import org.axonframework.spring.stereotype.Aggregate
import java.util.UUID

@Aggregate
class Invitation {
  @AggregateIdentifier
  private lateinit var invitationId: UUID

  constructor()

  @CommandHandler
  constructor(command: CreateInvitation) {
    AggregateLifecycle.apply(InvitationCreated(UUID.randomUUID(), command.code))
  }

  @EventSourcingHandler
  fun on(event: InvitationCreated) {
    invitationId = event.invitationId
  }
}
