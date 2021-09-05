package io.connorwyatt.wedding.invitations.spreadsheets.googlesheets

import io.connorwyatt.wedding.invitations.messages.events.InvitationCreated
import io.connorwyatt.wedding.invitations.messages.events.InvitationEmailSent
import io.connorwyatt.wedding.invitations.messages.events.InvitationResponseReceived
import io.connorwyatt.wedding.invitations.messages.events.InviteeAdded
import io.connorwyatt.wedding.invitations.messages.models.Invitation
import io.connorwyatt.wedding.invitations.messages.models.InvitationStatus.created
import io.connorwyatt.wedding.invitations.messages.models.InvitationStatus.responseReceived
import io.connorwyatt.wedding.invitations.messages.models.Invitee
import io.connorwyatt.wedding.invitations.messages.models.InviteeStatus.attending
import io.connorwyatt.wedding.invitations.messages.models.InviteeStatus.notAttending
import io.connorwyatt.wedding.invitations.messages.models.InviteeStatus.unknown
import io.connorwyatt.wedding.invitations.spreadsheets.SpreadsheetsService
import io.connorwyatt.wedding.invitations.sql.SqlInvitationsRepository
import kotlinx.coroutines.runBlocking
import org.axonframework.eventhandling.EventHandler
import org.axonframework.eventhandling.Timestamp
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class GoogleSheetUpdater(
  private val googleSheetsService: SpreadsheetsService,
  private val repository: SqlInvitationsRepository,
) {
  @EventHandler
  fun on(event: InvitationCreated, @Timestamp timestamp: Instant) {
    googleSheetsService.addInvitation(
      Invitation(
        id = event.invitationId,
        code = event.code,
        type = event.type,
        status = created,
        addressedTo = event.addressedTo,
        emailAddress = event.emailAddress,
        createdAt = timestamp,
      )
    )
  }

  @EventHandler
  fun on(event: InviteeAdded) {
    googleSheetsService.addInvitee(
      event.invitationId,
      Invitee(
        id = event.inviteeId,
        name = event.name,
        status = unknown,
        requiresFood = event.requiresFood,
      )
    )
  }

  @EventHandler
  fun on(event: InvitationEmailSent, @Timestamp timestamp: Instant) = runBlocking {
    val invitation = repository.getById(event.invitationId)
      ?: throw Exception("Cannot find Invitation.")

    googleSheetsService.updateInvitation(invitation.copy(emailSent = true, sentAt = timestamp))
  }

  @EventHandler
  fun on(event: InvitationResponseReceived, @Timestamp timestamp: Instant) = runBlocking {
    val invitation = repository.getById(event.invitationId)
      ?: throw Exception("Cannot find Invitation.")

    googleSheetsService.updateInvitation(
      invitation.copy(
        status = responseReceived,
        contactInformation = event.contactInformation,
        respondedAt = timestamp
      )
    )

    event.inviteeResponses.forEach { inviteeResponse ->
      val invitee = invitation.invitees.single { it.id == inviteeResponse.id }
      googleSheetsService.updateInvitee(
        event.invitationId,
        invitee.copy(
          status = if (inviteeResponse.attending) attending else notAttending,
          foodOption = inviteeResponse.foodOption,
          dietaryNotes = inviteeResponse.dietaryNotes),
      )
    }
  }
}
