package io.connorwyatt.wedding.invitations.messages.models

data class Invitee(
  val name: String?,
  val status: InviteeStatus,
  val foodOption: FoodOption? = null,
  val dietaryNotes: String? = null,
)
