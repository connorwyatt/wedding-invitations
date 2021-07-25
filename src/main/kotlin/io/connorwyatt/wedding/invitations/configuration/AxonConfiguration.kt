package io.connorwyatt.wedding.invitations.configuration

import org.axonframework.common.jdbc.ConnectionProvider
import org.axonframework.eventhandling.tokenstore.TokenStore
import org.axonframework.eventhandling.tokenstore.jdbc.JdbcTokenStore
import org.axonframework.eventhandling.tokenstore.jdbc.TokenSchema
import org.axonframework.serialization.Serializer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AxonConfiguration {
  @Bean
  fun tokenSchema(): TokenSchema =
    TokenSchema
      .builder()
      .setTokenTable("axon_tokens")
      .setProcessorNameColumn("processor_name")
      .setSegmentColumn("segment")
      .setTokenColumn("token")
      .setTokenTypeColumn("token_type")
      .setTimestampColumn("timestamp")
      .setOwnerColumn("owner")
      .build()

  @Bean
  fun tokenStore(tokenSchema: TokenSchema, connectionProvider: ConnectionProvider, serializer: Serializer): TokenStore =
    JdbcTokenStore.builder()
      .schema(tokenSchema)
      .connectionProvider(connectionProvider)
      .serializer(serializer)
      .build()
}
