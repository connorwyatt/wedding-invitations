package io.connorwyatt.wedding.invitations.messages.models

import java.time.Instant

data class Invitation(
  val id: String,
  val code: String,
  val status: InvitationStatus,
  val invitationPosted: Boolean = false,
  val emailAddress: String?,
  val invitationEmailed: Boolean = false,
  val createdAt: Instant,
  val respondedAt: Instant? = null,
  val invitees: List<Invitee> = listOf(),
)
