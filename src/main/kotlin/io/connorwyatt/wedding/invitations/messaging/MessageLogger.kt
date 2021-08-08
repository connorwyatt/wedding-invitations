package io.connorwyatt.wedding.invitations.messaging

import org.axonframework.messaging.InterceptorChain
import org.axonframework.messaging.Message
import org.axonframework.messaging.MessageDispatchInterceptor
import org.axonframework.messaging.MessageHandlerInterceptor
import org.axonframework.messaging.unitofwork.UnitOfWork
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.util.function.BiFunction

@Component
class MessageLogger<T : Message<*>?> :
  MessageDispatchInterceptor<T>,
  MessageHandlerInterceptor<T> {
  override fun handle(messages: List<T>): BiFunction<Int, T, T> =
    BiFunction { _, message ->
      message?.payload?.let { payload ->
        logger.info("Dispatched message: [{}]", payload)
      }

      message
    }

  override fun handle(
    unitOfWork: UnitOfWork<out T>,
    interceptorChain: InterceptorChain,
  ): Any? {
    val message = unitOfWork.message

    message?.payload?.let { payload ->
      logger.info("Incoming message: [{}]", payload)
    }

    return try {
      val returnValue = interceptorChain.proceed()

      message?.payload?.let { payload ->
        logger.info("Handled message successfully: [{}]", payload)
      }

      returnValue
    } catch (e: Exception) {
      message?.payload?.let { payload ->
        logger.error("Failed to handle message: [{}]", payload)
        logger.error("Exception was:", e)
      }

      throw e
    }
  }

  companion object {
    private val logger: Logger = LoggerFactory.getLogger(MessageLogger::class.java)
  }
}

