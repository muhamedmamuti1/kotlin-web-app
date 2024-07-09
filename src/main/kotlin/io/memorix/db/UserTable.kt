package io.memorix.db

import org.jetbrains.exposed.dao.id.IntIdTable

object UserTable : IntIdTable("users") {
    val name = varchar("name", 255)
    val email = varchar("email", 255)
    val password = varchar("password", 255)
}