package io.connorwyatt.wedding.invitations.discord

import com.fasterxml.jackson.databind.ObjectMapper
import io.connorwyatt.wedding.invitations.http.HttpClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordConfiguration {
  @Bean
  @Qualifier("discordHttpClient")
  fun httpClient(objectMapper: ObjectMapper): HttpClient {
    return HttpClient(
      java.net.http.HttpClient.newHttpClient(),
      objectMapper,
      LoggerFactory.getLogger(HttpClient::class.java)
    )
  }
}
