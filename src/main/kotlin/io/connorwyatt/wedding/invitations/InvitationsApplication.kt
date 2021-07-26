package io.connorwyatt.wedding.invitations

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.transaction.annotation.EnableTransactionManagement

@SpringBootApplication
@EnableTransactionManagement
class InvitationsApplication

fun main(args: Array<String>) {
  runApplication<InvitationsApplication>(*args)
}
