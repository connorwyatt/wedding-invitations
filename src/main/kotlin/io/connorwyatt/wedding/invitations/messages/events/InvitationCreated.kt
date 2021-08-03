package io.connorwyatt.wedding.invitations.messages.events

import io.connorwyatt.wedding.invitations.messages.models.InvitationType

data class InvitationCreated(
  val invitationId: String,
  val code: String,
  val type: InvitationType,
  val addressedTo: String,
  val emailAddress: String?,
)
