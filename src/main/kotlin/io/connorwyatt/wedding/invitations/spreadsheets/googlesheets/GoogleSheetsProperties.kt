package io.connorwyatt.wedding.invitations.spreadsheets.googlesheets

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("google-sheets")
data class GoogleSheetsProperties(val spreadsheetId: String)
