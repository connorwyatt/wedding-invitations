package io.connorwyatt.wedding.invitations.messaging

import org.axonframework.commandhandling.CommandBus
import org.axonframework.commandhandling.CommandMessage
import org.axonframework.eventhandling.EventBus
import org.axonframework.eventhandling.EventMessage
import org.axonframework.messaging.MessageDispatchInterceptor
import org.axonframework.messaging.MessageHandlerInterceptor
import org.axonframework.queryhandling.QueryBus
import org.axonframework.queryhandling.QueryMessage
import org.springframework.beans.factory.getBean
import org.springframework.context.event.ContextStartedEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class LoggingInterceptorRegistration {
  @EventListener
  fun on(event: ContextStartedEvent) {
    val applicationContext = event.applicationContext

    val messageLogger = applicationContext.getBean<MessageLogger<*>>()

    applicationContext.getBean<CommandBus>().apply {
      registerDispatchInterceptor(messageLogger as MessageDispatchInterceptor<in CommandMessage<*>>)
      registerHandlerInterceptor(messageLogger as MessageHandlerInterceptor<in CommandMessage<*>>)
    }

    applicationContext.getBean<EventBus>().apply {
      registerDispatchInterceptor(messageLogger as MessageDispatchInterceptor<in EventMessage<*>>)
    }

    applicationContext.getBean<QueryBus>().apply {
      registerDispatchInterceptor(messageLogger as MessageDispatchInterceptor<in QueryMessage<*, *>>)
      registerHandlerInterceptor(messageLogger as MessageHandlerInterceptor<in QueryMessage<*, *>>)
    }
  }
}
