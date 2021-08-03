package io.connorwyatt.wedding.invitations.sql

import io.connorwyatt.wedding.invitations.messages.models.Invitation
import io.connorwyatt.wedding.invitations.messages.models.InvitationStatus
import io.connorwyatt.wedding.invitations.messages.models.InvitationType
import org.jooq.RecordMapper
import org.springframework.stereotype.Component

@Component
class InvitationsRecordMapper : RecordMapper<InvitationsRecord, Invitation> {
  override fun map(record: InvitationsRecord): Invitation {
    return with(InvitationsTable) {
      Invitation(
        id = record.get(table.id),
        code = record.get(table.code),
        type = record.get(table.type, InvitationType::class.java),
        status = record.get(table.status, InvitationStatus::class.java),
        addressedTo = record.get(table.addressedTo),
        emailAddress = record.get(table.emailAddress),
        emailSent = record.get(table.emailSent),
        contactInformation = record.get(table.contactInformation),
        createdAt = record.get(table.createdAt).toInstant(),
        sentAt = record.get(table.sentAt)?.toInstant(),
        respondedAt = record.get(table.respondedAt)?.toInstant(),
        invitees = listOf()
      )
    }
  }
}
