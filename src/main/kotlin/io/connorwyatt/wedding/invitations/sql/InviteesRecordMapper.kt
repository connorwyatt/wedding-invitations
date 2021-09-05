package io.connorwyatt.wedding.invitations.sql

import io.connorwyatt.wedding.invitations.messages.models.FoodOption
import io.connorwyatt.wedding.invitations.messages.models.Invitee
import io.connorwyatt.wedding.invitations.messages.models.InviteeStatus
import org.jooq.RecordMapper
import org.springframework.stereotype.Component

@Component
class InviteesRecordMapper : RecordMapper<InviteesRecord, Invitee> {
  override fun map(record: InviteesRecord): Invitee {
    return with(InviteesTable) {
      Invitee(
        id = record.get(table.id),
        name = record.get(table.name),
        status = record.get(table.status, InviteeStatus::class.java),
        requiresFood = record.get(table.requiresFood),
        foodOption = record.get(table.foodOption, FoodOption::class.java),
        dietaryNotes = record.get(table.dietaryNotes),
      )
    }
  }
}
