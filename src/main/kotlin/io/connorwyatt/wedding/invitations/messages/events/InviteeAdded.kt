package io.connorwyatt.wedding.invitations.messages.events

data class InviteeAdded(
  val invitationId: String,
  val inviteeId: String,
  val name: String?,
)
