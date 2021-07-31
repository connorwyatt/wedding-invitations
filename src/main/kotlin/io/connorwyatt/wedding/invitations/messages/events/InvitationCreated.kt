package io.connorwyatt.wedding.invitations.messages.events

data class InvitationCreated(
  val invitationId: String,
  val code: String,
  val addressedTo: String,
  val emailAddress: String?,
)
