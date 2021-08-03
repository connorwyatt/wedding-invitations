package io.connorwyatt.wedding.invitations.messages.models

import java.time.Instant

data class Invitation(
  val id: String,
  val code: String,
  val status: InvitationStatus,
  val type: InvitationType,
  val addressedTo: String,
  val emailAddress: String?,
  val emailSent: Boolean = false,
  val contactInformation: String? = null,
  val createdAt: Instant,
  val sentAt: Instant? = null,
  val respondedAt: Instant? = null,
  val invitees: List<Invitee> = listOf(),
)
