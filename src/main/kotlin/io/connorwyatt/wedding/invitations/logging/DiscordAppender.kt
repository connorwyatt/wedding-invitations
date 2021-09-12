package io.connorwyatt.wedding.invitations.logging

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.AppenderBase
import io.connorwyatt.wedding.invitations.discord.DiscordClient
import kotlinx.coroutines.runBlocking
import org.springframework.context.SmartLifecycle
import org.springframework.stereotype.Component

@Component
class DiscordAppender(private val discordClient: DiscordClient) : AppenderBase<ILoggingEvent>(), SmartLifecycle {
  override fun isRunning() = isStarted

  override fun append(eventObject: ILoggingEvent) {
    when (eventObject.level) {
      Level.ERROR -> {
        runBlocking { discordClient.log("Error", eventObject.formattedMessage) }
      }
    }
  }
}
