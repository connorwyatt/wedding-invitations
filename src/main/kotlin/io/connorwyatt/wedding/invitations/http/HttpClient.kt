package io.connorwyatt.wedding.invitations.http

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.future.await
import org.slf4j.Logger
import java.net.URI
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers
import java.time.Duration
import kotlin.system.measureTimeMillis

class HttpClient(
  private val httpClient: java.net.http.HttpClient,
  private val objectMapper: ObjectMapper,
  private val logger: Logger,
) {
  suspend inline fun <reified T> get(path: String) = get(path, T::class.java)

  suspend fun <T> get(path: String, valueType: Class<T>): HttpResponse<T> {
    val uri = URI(path)
    val request = defaultRequest()
      .uri(uri)
      .GET()
      .build()

    logger.info("Sending HTTP request to '{}'", uri)

    lateinit var response: java.net.http.HttpResponse<String>
    val responseTime = measureTimeMillis {
      response = httpClient.sendAsync(request, BodyHandlers.ofString()).await()
    }
    logger.info(
      "Response received in {}ms with status code '{}'",
      responseTime,
      response.statusCode()
    )

    if (!response.isSuccessStatusCode)
      return HttpResponse(response.statusCode())

    val responseBody = objectMapper.readValue(response.body(), valueType)

    return HttpResponse(response.statusCode(), responseBody)
  }

  suspend inline fun <reified T> post(path: String, body: Any?) = post(path, body, T::class.java)

  suspend fun <T> post(path: String, body: Any?, valueType: Class<T>): HttpResponse<T> {
    val uri = URI(path)
    val request = defaultRequest()
      .uri(uri)
      .header("Content-Type", "application/json")
      .POST(BodyPublishers.ofString(objectMapper.writeValueAsString(body)))
      .build()

    val response: java.net.http.HttpResponse<String> = execute(request)

    if (!response.isSuccessStatusCode)
      return HttpResponse(response.statusCode())

    val responseBody = objectMapper.readValue(response.body(), valueType)

    return HttpResponse(response.statusCode(), responseBody)
  }

  private fun defaultRequest() = HttpRequest.newBuilder()
    .timeout(Duration.ofSeconds(30))

  private suspend fun execute(request: HttpRequest): java.net.http.HttpResponse<String> {
    logger.info("Sending HTTP request to '{}'", request.uri())

    lateinit var response: java.net.http.HttpResponse<String>
    val responseTime = measureTimeMillis {
      response = httpClient.sendAsync(request, BodyHandlers.ofString()).await()
    }
    logger.info(
      "Response from '{}' received in {}ms with status code '{}'",
      request.uri(),
      responseTime,
      response.statusCode()
    )
    return response
  }
}
