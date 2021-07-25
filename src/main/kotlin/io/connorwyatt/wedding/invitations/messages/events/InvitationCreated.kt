package io.connorwyatt.wedding.invitations.messages.events

import java.util.UUID

data class InvitationCreated(val invitationId: UUID, val code: String)
