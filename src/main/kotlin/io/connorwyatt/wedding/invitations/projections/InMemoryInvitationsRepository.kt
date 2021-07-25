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
      throw Exception()
    }

    invitations = invitations.plus(invitation.id to invitation)
  }
}
