package io.connorwyatt.wedding.invitations

import io.connorwyatt.wedding.invitations.messages.commands.CreateInvitation
import io.connorwyatt.wedding.invitations.messages.commands.RespondToInvitation
import io.connorwyatt.wedding.invitations.messages.commands.SendInvitationEmail
import io.connorwyatt.wedding.invitations.messages.models.Invitation
import io.connorwyatt.wedding.invitations.messages.models.InvitationDefinition
import io.connorwyatt.wedding.invitations.messages.models.InvitationResponse
import io.connorwyatt.wedding.invitations.messages.models.Reference
import io.connorwyatt.wedding.invitations.messages.queries.InvitationByIdQuery
import io.connorwyatt.wedding.invitations.messages.queries.InvitationsQuery
import kotlinx.coroutines.future.await
import org.axonframework.commandhandling.gateway.CommandGateway
import org.axonframework.extensions.kotlin.query
import org.axonframework.extensions.kotlin.queryMany
import org.axonframework.queryhandling.QueryGateway
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait
import org.springframework.web.reactive.function.server.queryParamOrNull
import java.util.UUID

@Component
class InvitationsHandler(private val commandGateway: CommandGateway, private val queryGateway: QueryGateway) {
  suspend fun getInvitations(serverRequest: ServerRequest): ServerResponse {
    val code = serverRequest.queryParamOrNull("code")

    val invitations = queryGateway.queryMany<Invitation, InvitationsQuery>(InvitationsQuery(code = code)).await()

    return ServerResponse.ok().bodyValueAndAwait(invitations)
  }

  suspend fun getInvitationById(serverRequest: ServerRequest): ServerResponse {
    val invitationId = serverRequest.pathVariable("invitationId")

    val invitation = queryGateway.query<Invitation?, InvitationByIdQuery>(InvitationByIdQuery(invitationId)).await()
      ?: return ServerResponse.notFound().buildAndAwait()

    return ServerResponse.ok().bodyValueAndAwait(invitation)
  }

  suspend fun postInvitation(serverRequest: ServerRequest): ServerResponse {
    val definition = serverRequest.awaitBody<InvitationDefinition>()

    val command = CreateInvitation(
      UUID.randomUUID().toString(),
      definition.code,
      definition.type,
      definition.addressedTo,
      definition.emailAddress,
      definition.invitees
    )

    return try {
      val invitationId = commandGateway.send<String>(command).await()

      ServerResponse.accepted().bodyValueAndAwait(Reference(invitationId))
    } catch (e: Exception) {
      ServerResponse.badRequest().bodyValueAndAwait(e.message ?: "An unknown error occurred.")
    }
  }

  suspend fun sendInvitationEmail(serverRequest: ServerRequest): ServerResponse {
    val invitationId = serverRequest.pathVariable("invitationId")

    queryGateway.query<Invitation?, InvitationByIdQuery>(InvitationByIdQuery(invitationId)).await()
      ?: return ServerResponse.notFound().buildAndAwait()

    val command = SendInvitationEmail(invitationId)

    return try {
      commandGateway.send<String>(command).await()

      ServerResponse.accepted().buildAndAwait()
    } catch (e: Exception) {
      ServerResponse.badRequest().bodyValueAndAwait(e.message ?: "An unknown error occurred.")
    }
  }

  suspend fun respondToInvitation(serverRequest: ServerRequest): ServerResponse {
    val invitationId = serverRequest.pathVariable("invitationId")
    val definition = serverRequest.awaitBody<InvitationResponse>()

    queryGateway.query<Invitation?, InvitationByIdQuery>(InvitationByIdQuery(invitationId)).await()
      ?: return ServerResponse.notFound().buildAndAwait()

    val command = RespondToInvitation(invitationId, definition.contactInformation, definition.invitees)

    return try {
      commandGateway.send<String>(command).await()

      ServerResponse.accepted().buildAndAwait()
    } catch (e: Exception) {
      ServerResponse.badRequest().bodyValueAndAwait(e.message ?: "An unknown error occurred.")
    }
  }
}
