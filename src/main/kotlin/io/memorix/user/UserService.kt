package io.memorix.user

import io.memorix.exceptions.*
import org.mindrot.jbcrypt.BCrypt

class UserService {
    private val userRepository = UserRepository()

    private suspend fun emailExists(email: String): Boolean {
        val users = userRepository.allUsers()
        return users.any { it.email == email }
    }

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    suspend fun getUsers(query: String?, limit: String?): List<User> {
        if (query == null) {
            throw InvalidUserDataException()
        }
        val limitInt = Integer.parseInt(limit)
        return userRepository.usersByName(query, limitInt)
    }

    suspend fun validateAndAddUser(user: User) {
        if (emailExists(user.email)) {
            throw DuplicateEmailException(user.email)
        }

        if (user.password.isBlank()) {
            throw InvalidUserDataException()
        }

        val hashedPassword = hashPassword(user.password)
        val userWithHashedPassword = user.copy(password = hashedPassword)
        userRepository.addUser(userWithHashedPassword)
    }
}