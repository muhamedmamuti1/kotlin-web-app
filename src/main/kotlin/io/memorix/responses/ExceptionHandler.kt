package io.memorix.responses

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*

class DuplicateEmailException(email: String) : Exception(ResponseMessages.DUPLICATE_EMAIL + email)
class InvalidUserDataException : Exception()
class InvalidPasswordException : Exception()

/**
 * This function handles the exceptions based on their type.
 */
suspend fun handleException(ex: Exception, call: ApplicationCall) {
    when (ex) {
        is DuplicateEmailException -> call.respond(HttpStatusCode.BadRequest, ErrorResponse(ex.message ?: ResponseMessages.DUPLICATE_EMAIL))
        is InvalidUserDataException -> call.respond(HttpStatusCode.BadRequest, ErrorResponse(ResponseMessages.INVALID_USER_DATA))
        is InvalidPasswordException -> call.respond(HttpStatusCode.BadRequest, ErrorResponse(ResponseMessages.INVALID_PASSWORD))
        is BadRequestException -> call.respond(HttpStatusCode.BadRequest, ErrorResponse(ex.message ?: ResponseMessages.BAD_REQUEST))
        is IllegalStateException, is JsonConvertException -> call.respond(HttpStatusCode.BadRequest)
        else -> call.respond(HttpStatusCode.InternalServerError)
    }
}