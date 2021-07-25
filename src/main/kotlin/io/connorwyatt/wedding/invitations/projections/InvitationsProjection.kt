package io.connorwyatt.wedding.invitations.projections

import io.connorwyatt.wedding.invitations.messages.events.InvitationCreated
import io.connorwyatt.wedding.invitations.messages.models.Invitation
import kotlinx.coroutines.runBlocking
import org.axonframework.eventhandling.EventHandler
import org.springframework.stereotype.Component

@Component
class InvitationsProjection(private val inMemoryInvitationsRepository: InMemoryInvitationsRepository) {
  @EventHandler
  fun on(event: InvitationCreated) {
    runBlocking {
      inMemoryInvitationsRepository.insert(Invitation(event.invitationId, event.code))
    }
  }
}
