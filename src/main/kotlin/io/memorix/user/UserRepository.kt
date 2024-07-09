package io.memorix.user

import io.memorix.db.*
import org.jetbrains.exposed.sql.lowerCase
import java.util.*

class UserRepository : UserRepositoryInterface {
    override suspend fun allUsers(): List<User> = suspendTransaction {
        UserDAO.all().map(::daoToModel)
    }

    override suspend fun usersByName(query: String, limit: Int): List<User> = suspendTransaction {
        UserDAO
            .find { UserTable.name.lowerCase() like "%${query.lowercase(Locale.getDefault())}%" }
            .limit(limit)
            .map(::daoToModel)
            .toList()
    }

    override suspend fun addUser(user: User): Unit = suspendTransaction {
        UserDAO.new {
            name = user.name
            email = user.email
            password = user.password
        }
    }
}
