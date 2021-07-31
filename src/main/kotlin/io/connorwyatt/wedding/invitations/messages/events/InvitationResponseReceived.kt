package io.connorwyatt.wedding.invitations.messages.events

import io.connorwyatt.wedding.invitations.messages.models.InviteeResponse

data class InvitationResponseReceived(
  val invitationId: String,
  val inviteeResponses: List<InviteeResponse>,
)

