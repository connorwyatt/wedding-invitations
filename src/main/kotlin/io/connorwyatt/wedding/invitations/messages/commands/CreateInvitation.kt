package io.connorwyatt.wedding.invitations.messages.commands

import io.connorwyatt.wedding.invitations.messages.models.InviteeDefinition

data class CreateInvitation(
  val code: String,
  val addressedTo: String,
  val emailAddress: String?,
  val invitees: List<InviteeDefinition>,
)
