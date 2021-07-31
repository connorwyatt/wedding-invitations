package io.connorwyatt.wedding.invitations

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties("web-app")
data class WebAppProperties(val invitationUrlPrefix: String)
