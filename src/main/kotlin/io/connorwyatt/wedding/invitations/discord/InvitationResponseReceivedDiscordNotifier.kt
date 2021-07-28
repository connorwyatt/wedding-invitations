package io.connorwyatt.wedding.invitations.discord

import io.connorwyatt.wedding.invitations.messages.events.InvitationResponseReceived
import io.connorwyatt.wedding.invitations.sql.SqlInvitationsRepository
import kotlinx.coroutines.runBlocking
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class InvitationResponseReceivedDiscordNotifier(
  private val discordClient: DiscordClient,
  private val repository: SqlInvitationsRepository,
) {
  @EventHandler
  fun on(event: InvitationResponseReceived) = runBlocking {
    val invitation = repository.getById(event.invitationId)
      ?: throw Exception("Could not find invitation.")

    discordClient.sendToWebhook(invitation, event.inviteeResponses)
  }
}
