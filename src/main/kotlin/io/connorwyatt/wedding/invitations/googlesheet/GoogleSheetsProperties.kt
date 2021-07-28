package io.connorwyatt.wedding.invitations.googlesheet

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URI

@ConstructorBinding
@ConfigurationProperties("google-sheets")
data class GoogleSheetsProperties(val sheetUri: URI)
