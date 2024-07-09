package io.memorix.exceptions

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.response.*

class DuplicateEmailException(email: String) : Exception("Duplicate email: $email")
class InvalidUserDataException : Exception("Invalid user data")

suspend fun handleException(ex: Exception, call: ApplicationCall) {
    when (ex) {
        is DuplicateEmailException -> call.respond(HttpStatusCode.BadRequest, ex.message ?: "Duplicate e-mail")
        is InvalidUserDataException -> call.respond(HttpStatusCode.BadRequest, ex.message ?: "Invalid user data")
        is IllegalStateException, is JsonConvertException -> call.respond(HttpStatusCode.BadRequest)
        else -> call.respond(HttpStatusCode.InternalServerError)
    }
}