package io.connorwyatt.wedding.invitations.sql

import org.jooq.TableField
import org.jooq.impl.CustomTable
import org.jooq.impl.DSL.name
import org.jooq.impl.SQLDataType.TIMESTAMPWITHTIMEZONE
import org.jooq.impl.SQLDataType.VARCHAR
import java.time.OffsetDateTime

class InvitationsTable private constructor() : CustomTable<InvitationsRecord>(name("invitations")) {
  val id: TableField<InvitationsRecord, String> = createField(name("id"), VARCHAR)
  val code: TableField<InvitationsRecord, String> = createField(name("code"), VARCHAR)
  val status: TableField<InvitationsRecord, String> = createField(name("status"), VARCHAR)
  val emailAddress: TableField<InvitationsRecord, String> = createField(name("email_address"), VARCHAR)
  val createdAt: TableField<InvitationsRecord, OffsetDateTime> = createField(name("created_at"), TIMESTAMPWITHTIMEZONE)
  val sentAt: TableField<InvitationsRecord, OffsetDateTime> = createField(name("sent_at"), TIMESTAMPWITHTIMEZONE)
  val respondedAt: TableField<InvitationsRecord, OffsetDateTime> =
    createField(name("responded_at"), TIMESTAMPWITHTIMEZONE)

  override fun getRecordType(): Class<out InvitationsRecord> {
    return InvitationsRecord::class.java
  }

  companion object {
    val table by lazy { InvitationsTable() }
  }
}

