package io.memorix.user

interface UserRepositoryInterface {
    suspend fun addUser(user: User)
    suspend fun allUsers(): List<User>
    suspend fun countUsers(): Long
    suspend fun usersByName(query: String, limit: Int): List<User>
}