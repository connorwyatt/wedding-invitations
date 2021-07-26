package io.connorwyatt.wedding.invitations.sql

import io.connorwyatt.wedding.invitations.messages.models.Invitation
import io.connorwyatt.wedding.invitations.messages.models.InvitationStatus
import org.jooq.RecordMapper
import org.springframework.stereotype.Component

@Component
class InvitationsRecordMapper : RecordMapper<InvitationsRecord, Invitation> {
  override fun map(record: InvitationsRecord): Invitation {
    return with(InvitationsTable) {
      Invitation(
        id = record.get(table.id),
        code = record.get(table.code),
        status = record.get(table.status, InvitationStatus::class.java),
        invitationPosted = record.get(table.invitationPosted),
        emailAddress = record.get(table.emailAddress),
        invitationEmailed = record.get(table.invitationEmailed),
        createdAt = record.get(table.createdAt).toInstant(),
        respondedAt = record.get(table.respondedAt)?.toInstant(),
        invitees = listOf()
      )
    }
  }
}
