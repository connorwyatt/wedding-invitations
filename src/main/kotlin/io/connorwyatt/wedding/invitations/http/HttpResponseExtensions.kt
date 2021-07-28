package io.connorwyatt.wedding.invitations.http

import java.net.http.HttpResponse

val <T> HttpResponse<T>.isSuccessStatusCode: Boolean
  get() = this.statusCode() in 200..299
