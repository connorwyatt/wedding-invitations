package io.connorwyatt.wedding.invitations.messages.commands

import org.axonframework.modelling.command.TargetAggregateIdentifier

data class SendInvitationEmail(@TargetAggregateIdentifier val invitationId: String)
