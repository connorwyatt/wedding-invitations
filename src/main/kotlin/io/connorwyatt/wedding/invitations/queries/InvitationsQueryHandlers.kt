package io.connorwyatt.wedding.invitations.queries

import io.connorwyatt.wedding.invitations.messages.queries.InvitationByIdQuery
import io.connorwyatt.wedding.invitations.messages.queries.InvitationsQuery
import io.connorwyatt.wedding.invitations.sql.SqlInvitationsRepository
import kotlinx.coroutines.runBlocking
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class InvitationsQueryHandlers(private val repository: SqlInvitationsRepository) {
  @QueryHandler
  fun handle(query: InvitationsQuery) = runBlocking {
    repository.search(code = query.code)
  }

  @QueryHandler
  fun handle(query: InvitationByIdQuery) = runBlocking {
    repository.getById(query.id)
  }
}
