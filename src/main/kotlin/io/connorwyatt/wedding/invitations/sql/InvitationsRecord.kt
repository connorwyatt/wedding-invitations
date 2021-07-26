package io.connorwyatt.wedding.invitations.sql

import org.jooq.impl.CustomRecord

class InvitationsRecord private constructor() : CustomRecord<InvitationsRecord>(InvitationsTable.table)
