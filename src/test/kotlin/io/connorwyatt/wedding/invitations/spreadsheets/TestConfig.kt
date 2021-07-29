package io.connorwyatt.wedding.invitations.spreadsheets

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary

@TestConfiguration
class TestConfig {
  @Bean
  @Primary
  fun googleSheetsService(): SpreadsheetsService = StubSpreadsheetsService()
}
