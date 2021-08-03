package io.connorwyatt.wedding.invitations.messages.commands

import io.connorwyatt.wedding.invitations.messages.models.InviteeResponse
import org.axonframework.modelling.command.TargetAggregateIdentifier

data class RespondToInvitation(
  @TargetAggregateIdentifier
  val invitationId: String,
  val contactInformation: String?,
  val inviteeResponses: List<InviteeResponse>,
)
