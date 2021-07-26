package io.connorwyatt.wedding.invitations.sql

import org.jooq.impl.CustomRecord

class InviteesRecord private constructor() : CustomRecord<InviteesRecord>(InviteesTable.table)
