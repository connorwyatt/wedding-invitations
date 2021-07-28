package io.connorwyatt.wedding.invitations.discord

import io.connorwyatt.wedding.invitations.messages.models.Invitation
import io.connorwyatt.wedding.invitations.messages.models.InviteeResponse

interface DiscordClient {
  suspend fun sendToWebhook(invitation: Invitation, inviteeResponses: List<InviteeResponse>)
}
