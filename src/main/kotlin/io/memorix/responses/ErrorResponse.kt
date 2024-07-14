package io.memorix.responses

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(val error: String)

class DuplicateEmailException(email: String) : Exception(ResponseMessages.DUPLICATE_EMAIL + email)
class InvalidUserDataException : Exception(ResponseMessages.INVALID_USER_DATA)
class InvalidQueryParamException : Exception(ResponseMessages.INVALID_QUERY_PARAM_VALUE)
class InvalidPasswordException : Exception(ResponseMessages.INVALID_PASSWORD)
class BadRequestException(message: String) : Exception(message)