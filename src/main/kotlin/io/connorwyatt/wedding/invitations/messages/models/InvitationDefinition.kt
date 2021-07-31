package io.connorwyatt.wedding.invitations.messages.models

data class InvitationDefinition(
  val code: String,
  val addressedTo: String,
  val emailAddress: String?,
  val invitees: List<InviteeDefinition>,
)
