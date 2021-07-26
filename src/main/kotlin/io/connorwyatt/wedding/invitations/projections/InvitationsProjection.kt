package io.connorwyatt.wedding.invitations.projections

import io.connorwyatt.wedding.invitations.messages.events.InvitationCreated
import io.connorwyatt.wedding.invitations.messages.events.InviteeAdded
import io.connorwyatt.wedding.invitations.messages.models.Invitation
import io.connorwyatt.wedding.invitations.messages.models.InvitationStatus.created
import io.connorwyatt.wedding.invitations.messages.models.Invitee
import io.connorwyatt.wedding.invitations.messages.models.InviteeStatus.unknown
import io.connorwyatt.wedding.invitations.sql.SqlInvitationsRepository
import kotlinx.coroutines.runBlocking
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.Timestamp
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class InvitationsProjection(private val repository: SqlInvitationsRepository) {
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

      repository.insert(invitation)
    }
  }

  @EventHandler
  fun on(event: InviteeAdded) {
    runBlocking {
      val invitation = repository.getById(event.invitationId)
        ?: throw Exception("Invitation could not be found.")

      val invitee = Invitee(
        id = event.inviteeId,
        name = event.name,
        status = unknown,
      )

      val updatedInvitation = invitation.copy(invitees = invitation.invitees.plus(invitee))

      repository.update(updatedInvitation)
    }
  }
}
