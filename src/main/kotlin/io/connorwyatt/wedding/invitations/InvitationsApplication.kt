package io.connorwyatt.wedding.invitations

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import io.connorwyatt.wedding.invitations.logging.DiscordAppender
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableTransactionManagement
class InvitationsApplication

fun main(args: Array<String>) {
  runApplication<InvitationsApplication>(*args).run {
    start()
    addDiscordAppender()
  }
}

private fun ConfigurableApplicationContext.addDiscordAppender() {
  val loggerContext = LoggerFactory.getILoggerFactory() as LoggerContext
  val customAppender = getBean(DiscordAppender::class.java)
  val rootLogger: Logger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME)
  rootLogger.addAppender(customAppender)
}
