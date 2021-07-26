package io.connorwyatt.wedding.invitations.sql

import org.jooq.TableField
import org.jooq.impl.CustomTable
import org.jooq.impl.DSL.name
import org.jooq.impl.SQLDataType.VARCHAR

class InviteesTable private constructor() : CustomTable<InviteesRecord>(name("invitees")) {
  val id: TableField<InviteesRecord, String> = createField(name("id"), VARCHAR)
  val invitationId: TableField<InviteesRecord, String> = createField(name("invitation_id"), VARCHAR)
  val name: TableField<InviteesRecord, String> = createField(name("name"), VARCHAR)
  val status: TableField<InviteesRecord, String> = createField(name("status"), VARCHAR)
  val foodOption: TableField<InviteesRecord, String> = createField(name("food_option"), VARCHAR)
  val dietaryNotes: TableField<InviteesRecord, String> = createField(name("dietary_notes"), VARCHAR)

  override fun getRecordType(): Class<out InviteesRecord> {
    return InviteesRecord::class.java
  }

  companion object {
    val table by lazy { InviteesTable() }
  }
}

