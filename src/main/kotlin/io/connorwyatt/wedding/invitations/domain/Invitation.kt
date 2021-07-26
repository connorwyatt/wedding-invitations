package io.connorwyatt.wedding.invitations.domain

import io.connorwyatt.wedding.invitations.messages.events.InvitationCreated
import io.connorwyatt.wedding.invitations.messages.events.InviteeAdded
import io.connorwyatt.wedding.invitations.messages.models.InviteeDefinition
import org.axonframework.eventsourcing.EventSourcingHandler
import org.axonframework.modelling.command.AggregateIdentifier
import org.axonframework.modelling.command.AggregateLifecycle.apply
import org.axonframework.spring.stereotype.Aggregate
import java.util.UUID

@Aggregate
class Invitation {
  @AggregateIdentifier
  private lateinit var invitationId: String

  constructor()

  constructor(code: String, emailAddress: String?, invitees: List<InviteeDefinition>) {
    val invitationId = UUID.randomUUID().toString()

    apply(InvitationCreated(invitationId, code, emailAddress))

    invitees.forEach { invitee ->
      apply(InviteeAdded(invitationId, UUID.randomUUID().toString(), invitee.name))
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
