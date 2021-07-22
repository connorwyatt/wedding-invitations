package io.connorwyatt.wedding.invitations.config

import org.axonframework.eventsourcing.eventstore.EventStorageEngine
import org.axonframework.eventsourcing.eventstore.inmemory.InMemoryEventStorageEngine
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@TestConfiguration
class TestConfig {
  @Bean
  fun inMemoryEventStore(): EventStorageEngine {
    return InMemoryEventStorageEngine()
  }
}
