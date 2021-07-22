package io.connorwyatt.wedding.invitations

import io.connorwyatt.wedding.invitations.config.TestConfig
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@Import(TestConfig::class)
@TestPropertySource(locations = ["/application.yaml"])
class InvitationsApplicationTests {
  @Test
  fun contextLoads() {
  }
}
