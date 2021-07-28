package io.connorwyatt.wedding.invitations.discord

import io.connorwyatt.wedding.invitations.googlesheet.GoogleSheetsProperties
import io.connorwyatt.wedding.invitations.http.HttpClient
import io.connorwyatt.wedding.invitations.messages.models.FoodOption.standard
import io.connorwyatt.wedding.invitations.messages.models.Invitation
import io.connorwyatt.wedding.invitations.messages.models.InviteeResponse
import org.springframework.stereotype.Component

@Component
class HttpDiscordClient(
  private val httpClient: HttpClient,
  private val discordProperties: DiscordProperties,
  private val googleSheetsProperties: GoogleSheetsProperties,
) :
  DiscordClient {
  override suspend fun sendToWebhook(invitation: Invitation, inviteeResponses: List<InviteeResponse>) {
    httpClient.post<Unit>(discordProperties.webhookUri.toString(), mapOf<String, Any>(
      "embeds" to listOf(
        mapOf(
          "title" to "Wedding Invitation Response Received",
          "description" to "[Google Sheet](${googleSheetsProperties.sheetUri}) updated.",
          "color" to 16711422,
          "fields" to inviteeResponses.map { inviteeResponse ->
            mapOf(
              "name" to (invitation.invitees.single { it.id == inviteeResponse.id }.name ?: "_No name_"),
              "value" to listOfNotNull(
                (if (inviteeResponse.attending) "$tickEmoji Attending" else "$crossEmoji Not attending"),
                (if (inviteeResponse.attending) (if (inviteeResponse.foodOption == standard) ":hamburger: Standard food" else ":salad: Vegetarian food") else null),
                (if (inviteeResponse.attending) (if (inviteeResponse.dietaryNotes != null) ":information_source: ${inviteeResponse.dietaryNotes}" else ":information_source: _No dietary information_") else null)
              ).joinToString("\n")
            )
          }
        )
      )
    ))
  }

  companion object {
    private const val tickEmoji = "<:circletick:869731271243358228>"
    private const val crossEmoji = "<:circlecross:869731292894351421>"
  }
}