package io.memorix.responses

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(val email: String, val name: String)
@Serializable
data class UsersResponse(val users: List<UserResponse>, val total: Int)