package io.connorwyatt.wedding.invitations.discord

import io.connorwyatt.wedding.invitations.messages.models.Invitation
import io.connorwyatt.wedding.invitations.messages.models.InviteeResponse

class StubDiscordClient : DiscordClient {
  override suspend fun sendToWebhook(invitation: Invitation, inviteeResponses: List<InviteeResponse>) = Unit
}
