package io.memorix.user

import io.memorix.db.*
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.lowerCase
import java.util.*

class UserRepository(
    private val database: Database
) : UserRepositoryInterface {

    override suspend fun addUser(user: User): Unit = suspendTransaction(database) {
        UserDAO.new {
            name = user.name
            email = user.email
            password = user.password
        }
    }

    override suspend fun allUsers(): List<User> = suspendTransaction(database) {
        UserDAO.all().map(::daoToModel)
    }

    override suspend fun countUsers(): Long = suspendTransaction(database) {
        UserDAO.count()
    }

    override suspend fun usersByName(query: String, limit: Int): List<User> = suspendTransaction(database) {
        UserDAO
            .find { UserTable.name.lowerCase() like "${query.lowercase(Locale.getDefault())}%" }
            .limit(limit)
            .map(::daoToModel)
            .toList()
    }
}