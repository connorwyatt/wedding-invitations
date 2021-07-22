package io.connorwyatt.wedding.invitations

import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait
import org.springframework.web.reactive.function.server.buildAndAwait

@Component
class InvitationsHandler {
  suspend fun getInvitations(serverRequest: ServerRequest) = ServerResponse.ok().bodyValueAndAwait(listOf<Unit>())

  suspend fun postInvitation(serverRequest: ServerRequest) = ServerResponse.accepted().buildAndAwait()

  suspend fun getInvitationById(serverRequest: ServerRequest) = ServerResponse.notFound().buildAndAwait()
}
