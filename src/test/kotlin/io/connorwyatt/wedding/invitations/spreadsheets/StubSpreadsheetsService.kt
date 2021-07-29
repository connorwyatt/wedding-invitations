package io.connorwyatt.wedding.invitations.spreadsheets

import io.connorwyatt.wedding.invitations.messages.models.Invitation
import io.connorwyatt.wedding.invitations.messages.models.Invitee
import java.net.URL

class StubSpreadsheetsService : SpreadsheetsService {
  override fun getSpreadsheetUrl() = URL("http://localhost")

  override fun addInvitation(invitation: Invitation) = Unit

  override fun updateInvitation(invitation: Invitation) = Unit

  override fun addInvitee(invitationId: String, invitee: Invitee) = Unit

  override fun updateInvitee(invitationId: String, invitee: Invitee) = Unit
}
