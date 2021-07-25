package io.connorwyatt.wedding.invitations.projections

import io.connorwyatt.wedding.invitations.messages.models.Invitation
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class InMemoryInvitationsRepository {
  private var invitations = mapOf<UUID, Invitation>()

  suspend fun search() = invitations.values.toList()

  suspend fun getById(id: UUID) = invitations[id]

  suspend fun getByCode(code: String) = invitations.values.singleOrNull {
    it.code == code
  }

  suspend fun insert(invitation: Invitation) {
    if (getById(invitation.id) != null) {
      throw Exception("Invitation already exists.")
    }

    invitations = invitations.plus(invitation.id to invitation)
  }

  suspend fun update(invitation: Invitation) {
    if (getById(invitation.id) == null) {
      throw Exception("Invitation does not exist.")
    }

    invitations = invitations.mapValues {
      if (it.key == invitation.id) invitation else it.value
    }
  }
}
