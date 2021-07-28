package io.connorwyatt.wedding.invitations.http

data class HttpResponse<T>(val statusCode: Int, val body: T? = null) {
  val isSuccessStatusCode: Boolean get() = statusCode in 200..299
}
