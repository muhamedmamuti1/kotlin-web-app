package io.memorix.responses

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.memorix.helpers.Logger

/**
 * This function handles the exceptions based on their type.
 */
suspend fun handleException(cause: Throwable, call: ApplicationCall) {
    when (cause) {
        is DuplicateEmailException -> call.respond(HttpStatusCode.BadRequest, ErrorResponse(cause.message ?: ResponseMessages.DUPLICATE_EMAIL))
        is InvalidUserDataException -> call.respond(HttpStatusCode.BadRequest, ErrorResponse(ResponseMessages.INVALID_USER_DATA))
        is InvalidPasswordException -> call.respond(HttpStatusCode.BadRequest, ErrorResponse(ResponseMessages.INVALID_PASSWORD))
        is InvalidQueryParamException -> call.respond(HttpStatusCode.BadRequest, ErrorResponse(ResponseMessages.INVALID_QUERY_PARAM_VALUE))
        is BadRequestException -> call.respond(HttpStatusCode.BadRequest, ErrorResponse(cause.message ?: ResponseMessages.BAD_REQUEST))
        is IllegalStateException, is JsonConvertException -> call.respond(HttpStatusCode.BadRequest)
        else -> {
            Logger.error(cause.message ?: ResponseMessages.SOMETHING_WENT_WRONG)
            call.respond(HttpStatusCode.InternalServerError, cause.message ?: ResponseMessages.SOMETHING_WENT_WRONG)
        }
    }
}