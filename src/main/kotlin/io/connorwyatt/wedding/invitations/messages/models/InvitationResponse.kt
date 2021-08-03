package io.connorwyatt.wedding.invitations.messages.models

data class InvitationResponse(
  val contactInformation: String? = null,
  val invitees: List<InviteeResponse>,
)
