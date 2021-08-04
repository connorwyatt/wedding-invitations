package io.connorwyatt.wedding.invitations.messages.models

data class InviteeResponse(
  val id: String,
  val attending: Boolean,
  val foodOption: FoodOption?,
  val dietaryNotes: String?,
)
