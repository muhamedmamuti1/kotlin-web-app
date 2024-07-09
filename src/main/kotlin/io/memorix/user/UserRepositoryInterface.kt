package io.memorix.user

interface UserRepositoryInterface {
    suspend fun allUsers(): List<User>
    suspend fun usersByName(query: String, limit: Int): List<User>?
    suspend fun addUser(user: User)
}