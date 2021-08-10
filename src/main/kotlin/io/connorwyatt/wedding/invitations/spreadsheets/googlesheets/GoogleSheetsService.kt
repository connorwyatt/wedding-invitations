package io.connorwyatt.wedding.invitations.spreadsheets.googlesheets

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.GoogleCredentials
import io.connorwyatt.wedding.invitations.messages.models.Invitation
import io.connorwyatt.wedding.invitations.messages.models.Invitee
import io.connorwyatt.wedding.invitations.spreadsheets.SpreadsheetsService
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.File
import java.io.FileInputStream
import java.net.URL
import java.time.ZoneId
import java.time.format.DateTimeFormatterBuilder
import java.time.format.SignStyle.EXCEEDS_PAD
import java.time.temporal.ChronoField.DAY_OF_MONTH
import java.time.temporal.ChronoField.HOUR_OF_DAY
import java.time.temporal.ChronoField.MINUTE_OF_HOUR
import java.time.temporal.ChronoField.MONTH_OF_YEAR
import java.time.temporal.ChronoField.SECOND_OF_MINUTE
import java.time.temporal.ChronoField.YEAR

@Component
class GoogleSheetsService(
  @Value("\${spring.application.name}") applicationName: String,
  private val googleSheetsProperties: GoogleSheetsProperties,
) : SpreadsheetsService {
  private val spreadsheetId = googleSheetsProperties.spreadsheetId
  private val sheets: Sheets by lazy {
    Sheets.Builder(
      GoogleNetHttpTransport.newTrustedTransport(),
      JacksonFactory.getDefaultInstance(),
      getServiceAccountCredentials()
    )
      .setApplicationName(applicationName)
      .build()
  }

  override fun getSpreadsheetUrl(): URL {
    return URL(sheets.spreadsheets().get(spreadsheetId).execute().spreadsheetUrl)
  }

  override fun addInvitation(invitation: Invitation) {
    val valueRange = ValueRange().setRange(invitationsDataRange).setValues(listOf(mapToRow(invitation)))

    sheets.spreadsheets()
      .values()
      .append(spreadsheetId, invitationsDataRange, valueRange)
      .setValueInputOption("USER_ENTERED")
      .execute()
  }

  override fun updateInvitation(invitation: Invitation) {
    val rows = sheets.spreadsheets()
      .values()
      .get(spreadsheetId, invitationsDataRange)
      .execute()
      .getValues()
      ?: emptyList()

    if (rows.count { it[0] == invitation.id } != 1) {
      throw Exception("Not exactly 1 invitation row in Sheet.")
    }

    val updatedRows = rows.map { row ->
      if (row[0] == invitation.id) mapToRow(invitation) else row
    }

    val valueRange = ValueRange().setRange(invitationsDataRange).setValues(updatedRows)

    sheets.spreadsheets()
      .values()
      .update(spreadsheetId, invitationsDataRange, valueRange)
      .setValueInputOption("USER_ENTERED")
      .execute()
  }

  override fun addInvitee(invitationId: String, invitee: Invitee) {
    val valueRange = ValueRange().setRange(inviteesDataRange).setValues(listOf(mapToRow(invitationId, invitee)))

    sheets.spreadsheets()
      .values()
      .append(spreadsheetId, inviteesDataRange, valueRange)
      .setValueInputOption("USER_ENTERED")
      .execute()
  }

  override fun updateInvitee(invitationId: String, invitee: Invitee) {
    val rows = sheets.spreadsheets()
      .values()
      .get(spreadsheetId, inviteesDataRange)
      .execute()
      .getValues()
      ?: emptyList()

    if (rows.count { it[1] == invitee.id } != 1) {
      throw Exception("Not exactly 1 invitee row in Sheet.")
    }

    val updatedRows = rows.map { row ->
      if (row[1] == invitee.id) mapToRow(invitationId, invitee) else row
    }

    val valueRange = ValueRange().setRange(inviteesDataRange).setValues(updatedRows)

    sheets.spreadsheets()
      .values()
      .update(spreadsheetId, inviteesDataRange, valueRange)
      .setValueInputOption("USER_ENTERED")
      .execute()
  }

  private fun getServiceAccountCredentials(): HttpCredentialsAdapter {
    val credentialsJson = googleSheetsProperties.serviceAccountCredentialsPath?.let { FileInputStream(File(it)) }
      ?: ClassPathResource(serviceAccountCredentialsFilePath).inputStream
    val credentials = GoogleCredentials.fromStream(credentialsJson).createScoped(SheetsScopes.SPREADSHEETS)
    return HttpCredentialsAdapter(credentials)
  }

  private fun mapToRow(invitation: Invitation) = listOf(
    invitation.id,
    invitation.code,
    invitation.type.toString(),
    invitation.addressedTo,
    invitation.status.toString(),
    invitation.emailAddress ?: blankValue,
    dateTimeFormatter.format(invitation.createdAt),
    invitation.sentAt?.let { dateTimeFormatter.format(it) } ?: blankValue,
    invitation.respondedAt?.let { dateTimeFormatter.format(it) } ?: blankValue,
    invitation.contactInformation ?: blankValue,
  )

  private fun mapToRow(invitationId: String, invitee: Invitee) = listOf(
    invitationId,
    invitee.id,
    invitee.name ?: blankValue,
    invitee.status.toString(),
    invitee.foodOption?.toString() ?: blankValue,
    invitee.dietaryNotes ?: blankValue,
  )

  companion object {
    private const val serviceAccountCredentialsFilePath = "/service-account-credentials.json"
    private const val invitationsSheetName = "Invitations"
    private const val invitationsDataRange = "$invitationsSheetName!A2:Z"
    private const val inviteesSheetName = "Invitees"
    private const val inviteesDataRange = "$inviteesSheetName!A2:Z"

    private const val blankValue = ""

    private val dateTimeFormatter = DateTimeFormatterBuilder()
      .appendValue(DAY_OF_MONTH, 2)
      .appendLiteral("/")
      .appendValue(MONTH_OF_YEAR, 2)
      .appendLiteral("/")
      .appendValue(YEAR, 4, 10, EXCEEDS_PAD)
      .appendLiteral(" ")
      .appendValue(HOUR_OF_DAY, 2)
      .appendLiteral(':')
      .appendValue(MINUTE_OF_HOUR, 2)
      .appendLiteral(':')
      .appendValue(SECOND_OF_MINUTE, 2)
      .toFormatter()
      .withZone(ZoneId.systemDefault())
  }
}
