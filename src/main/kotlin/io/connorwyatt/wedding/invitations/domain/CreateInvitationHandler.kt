package io.connorwyatt.wedding.invitations.domain

import io.connorwyatt.wedding.invitations.messages.commands.CreateInvitation
import io.connorwyatt.wedding.invitations.sql.SqlInvitationsRepository
import kotlinx.coroutines.runBlocking
import org.axonframework.commandhandling.CommandHandler
import org.axonframework.modelling.command.Repository
import org.springframework.stereotype.Component

@Component
class CreateInvitationHandler(
  private val aggregateRepository: Repository<Invitation>,
  private val invitationsRepository: SqlInvitationsRepository,
) {
  @CommandHandler
  fun handle(command: CreateInvitation): String = runBlocking {
    command.code.let { code ->
      if (isCodeUsed(code)) {
        throw Exception("Invitation already exists with code: ${code}.")
      }
    }

    command.emailAddress?.let { emailAddress ->
      if (isEmailAddressUsed(emailAddress)) {
        throw Exception("Invitation already exists with email address: ${emailAddress}.")
      }
    }

    return@runBlocking aggregateRepository
      .newInstance {
        Invitation(
          command.code,
          command.type,
          command.emailAddress,
          command.addressedTo,
          command.invitees
        )
      }
      .identifierAsString()
  }

  private suspend fun isCodeUsed(code: String) =
    invitationsRepository.search(code = code).any()

  private suspend fun isEmailAddressUsed(emailAddress: String) =
    invitationsRepository.search(emailAddress = emailAddress).any()
}
