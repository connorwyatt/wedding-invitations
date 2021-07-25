package io.connorwyatt.wedding.invitations.messages.events

import java.util.UUID

data class InviteeAdded(
  val invitationId: UUID,
  val name: String?,
)
