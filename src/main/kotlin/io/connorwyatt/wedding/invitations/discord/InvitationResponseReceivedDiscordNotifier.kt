package io.connorwyatt.wedding.invitations.discord

import io.connorwyatt.wedding.invitations.messages.events.InvitationResponseReceived
import io.connorwyatt.wedding.invitations.sql.SqlInvitationsRepository
import kotlinx.coroutines.runBlocking
import org.axonframework.eventhandling.EventHandler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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

    logger.info("Sending notification to Discord for Response Received: \"{}\"", invitation.addressedTo)
    discordClient.sendToWebhook(invitation, event.inviteeResponses)
    logger.info("Successfully sent notification to Discord for Response Received: \"{}\"", invitation.addressedTo)
  }

  companion object {
    private val logger: Logger = LoggerFactory.getLogger(InvitationResponseReceivedDiscordNotifier::class.java)
  }
}
