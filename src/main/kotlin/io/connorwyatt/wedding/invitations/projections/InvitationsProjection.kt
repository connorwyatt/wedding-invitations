package io.connorwyatt.wedding.invitations.projections

import io.connorwyatt.wedding.invitations.messages.events.InvitationCreated
import io.connorwyatt.wedding.invitations.messages.events.InviteeAdded
import io.connorwyatt.wedding.invitations.messages.models.Invitation
import io.connorwyatt.wedding.invitations.messages.models.InvitationStatus.created
import io.connorwyatt.wedding.invitations.messages.models.Invitee
import io.connorwyatt.wedding.invitations.messages.models.InviteeStatus.unknown
import kotlinx.coroutines.runBlocking
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.Timestamp
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class InvitationsProjection(private val inMemoryInvitationsRepository: InMemoryInvitationsRepository) {
  @EventHandler
  fun on(event: InvitationCreated, @Timestamp timestamp: Instant) {
    runBlocking {
      val invitation = Invitation(
        id = event.invitationId,
        code = event.code,
        status = created,
        emailAddress = event.emailAddress,
        createdAt = timestamp,
      )

      inMemoryInvitationsRepository.insert(invitation)
    }
  }

  @EventHandler
  fun on(event: InviteeAdded) {
    runBlocking {
      val invitation = inMemoryInvitationsRepository.getById(event.invitationId)
        ?: throw Exception("Invitation could not be found.")

      val invitee = Invitee(
        name = event.name,
        status = unknown,
      )

      val updatedInvitation = invitation.copy(invitees = invitation.invitees.plus(invitee))

      inMemoryInvitationsRepository.update(updatedInvitation)
    }
  }
}
