package io.connorwyatt.wedding.invitations

import org.axonframework.springboot.autoconfig.JdbcAutoConfiguration
import org.axonframework.springboot.autoconfig.JpaAutoConfiguration
import org.axonframework.springboot.autoconfig.JpaEventStoreAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [
  JdbcAutoConfiguration::class,
  JpaAutoConfiguration::class,
  JpaEventStoreAutoConfiguration::class
])
class InvitationsApplication

fun main(args: Array<String>) {
  runApplication<InvitationsApplication>(*args)
}
