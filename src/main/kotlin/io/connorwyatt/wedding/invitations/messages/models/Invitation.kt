package io.connorwyatt.wedding.invitations.messages.models

import java.time.Instant

data class Invitation(
  val id: String,
  val code: String,
  val status: InvitationStatus,
  val emailAddress: String?,
  val createdAt: Instant,
  val sentAt: Instant? = null,
  val respondedAt: Instant? = null,
  val invitees: List<Invitee> = listOf(),
)
