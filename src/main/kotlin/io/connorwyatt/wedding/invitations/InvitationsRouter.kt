package io.connorwyatt.wedding.invitations

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class InvitationsRouter {
  @Bean
  fun route(invitationsHandler: InvitationsHandler) = coRouter {
    GET("/invitations", invitationsHandler::getInvitations)
    POST("/invitations", invitationsHandler::postInvitation)
    GET("/invitations/{invitationId}", invitationsHandler::getInvitationById)
    POST("/invitations/{invitationId}/actions/send-invitation-email", invitationsHandler::sendInvitationEmail)
    POST("/invitations/{invitationId}/actions/respond", invitationsHandler::respondToInvitation)
  }
}
