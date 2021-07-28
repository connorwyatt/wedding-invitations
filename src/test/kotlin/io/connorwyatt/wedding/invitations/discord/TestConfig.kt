package io.connorwyatt.wedding.invitations.discord

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestConfig {
  @Bean
  @Primary
  fun discordClient(): DiscordClient = StubDiscordClient()
}
