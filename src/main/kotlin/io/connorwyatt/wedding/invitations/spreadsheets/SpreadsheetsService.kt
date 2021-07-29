package io.connorwyatt.wedding.invitations.spreadsheets

import io.connorwyatt.wedding.invitations.messages.models.Invitation
import io.connorwyatt.wedding.invitations.messages.models.Invitee
import java.net.URL

interface SpreadsheetsService {
  fun getSpreadsheetUrl(): URL

  fun addInvitation(invitation: Invitation)

  fun updateInvitation(invitation: Invitation)

  fun addInvitee(invitationId: String, invitee: Invitee)

  fun updateInvitee(invitationId: String, invitee: Invitee)
}
