package io.connorwyatt.wedding.invitations.sql

import io.connorwyatt.wedding.invitations.messages.models.Invitation
import io.connorwyatt.wedding.invitations.messages.models.Invitee
import kotlinx.coroutines.future.await
import org.jooq.DSLContext
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class SqlInvitationsRepository(
  private val context: DSLContext,
  private val invitationsRecordMapper: InvitationsRecordMapper,
  private val inviteesRecordMapper: InviteesRecordMapper,
) {
  suspend fun search(): List<Invitation> {
    val invitations = context
      .selectFrom(InvitationsTable.table)
      .orderBy(InvitationsTable.table.createdAt.desc())
      .fetchAsync()
      .await()
      .map(invitationsRecordMapper)

    val inviteeRecords = context
      .selectFrom(InviteesTable.table)
      .where(InviteesTable.table.invitationId.`in`(invitations.map { it.id }))
      .fetchAsync()
      .await()

    return invitations.map { invitation ->
      val invitees = inviteeRecords
        .filter { it.get(InviteesTable.table.invitationId) == invitation.id }
        .map(inviteesRecordMapper::map)

      invitation.copy(invitees = invitees)
    }
  }

  suspend fun getById(id: String): Invitation? {
    val invitation = context
      .selectFrom(InvitationsTable.table)
      .where(InvitationsTable.table.id.eq(id))
      .fetchAsync()
      .await()
      .map(invitationsRecordMapper)
      .singleOrNull()
      ?: return null

    val invitees = context
      .selectFrom(InviteesTable.table)
      .where(InviteesTable.table.invitationId.eq(id))
      .fetchAsync()
      .await()
      .map(inviteesRecordMapper)

    return invitation.copy(invitees = invitees)
  }

  @Transactional
  suspend fun insert(invitation: Invitation) {
    val invitationRecord = context.newRecord(InvitationsTable.table, invitation)

    context
      .insertInto(InvitationsTable.table)
      .set(invitationRecord)
      .executeAsync()
      .await()

    invitation.invitees.forEach { insert(it, invitation.id) }
  }

  @Transactional
  suspend fun update(invitation: Invitation) {
    val invitationRecord = context.newRecord(InvitationsTable.table, invitation)

    context
      .update(InvitationsTable.table)
      .set(invitationRecord)
      .where(InvitationsTable.table.id.eq(invitation.id))
      .executeAsync()
      .await()

    context
      .deleteFrom(InviteesTable.table)
      .where(InviteesTable.table.invitationId.eq(invitation.id))
      .executeAsync()
      .await()

    invitation.invitees.forEach { insert(it, invitation.id) }
  }

  private suspend fun insert(invitee: Invitee, invitationId: String) {
    val inviteesRecord = context.newRecord(InviteesTable.table, invitee)

    context
      .insertInto(InviteesTable.table)
      .set(InviteesTable.table.invitationId, invitationId)
      .set(inviteesRecord)
      .executeAsync()
      .await()
  }
}
