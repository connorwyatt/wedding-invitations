package io.connorwyatt.wedding.invitations.discord

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import java.net.URI

@ConstructorBinding
@ConfigurationProperties("discord")
data class DiscordProperties(val webhookUri: URI, val logWebhookUri: URI)
