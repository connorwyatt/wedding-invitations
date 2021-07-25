package io.connorwyatt.wedding.invitations.queries

import io.connorwyatt.wedding.invitations.messages.queries.InvitationByCodeQuery
import io.connorwyatt.wedding.invitations.messages.queries.InvitationByIdQuery
import io.connorwyatt.wedding.invitations.messages.queries.InvitationsQuery
import io.connorwyatt.wedding.invitations.projections.InMemoryInvitationsRepository
import kotlinx.coroutines.runBlocking
import org.axonframework.queryhandling.QueryHandler
import org.springframework.stereotype.Component

@Component
class InvitationsQueryHandlers(private val inMemoryInvitationsRepository: InMemoryInvitationsRepository) {
  @QueryHandler
  fun handle(query: InvitationsQuery) = runBlocking {
    inMemoryInvitationsRepository.search()
  }

  @QueryHandler
  fun handle(query: InvitationByIdQuery) = runBlocking {
    inMemoryInvitationsRepository.getById(query.id)
  }

  @QueryHandler
  fun handle(query: InvitationByCodeQuery) = runBlocking {
    inMemoryInvitationsRepository.getByCode(query.code)
  }
}
