package io.memorix.user

import io.memorix.responses.*
import org.mindrot.jbcrypt.BCrypt
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

private val emailRegex = Regex(
    "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$"
)
val passwordRegex = Regex("^(?=.*[A-Z]).{6,}\$")

class UserService : KoinComponent {
    private val userRepository: UserRepositoryInterface by inject()

    private fun isValidEmail(email: String): Boolean {
        return emailRegex.matches(email)
    }

    private suspend fun emailExists(email: String): Boolean {
        val users = userRepository.allUsers()
        return users.any { it.email == email }
    }

    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    suspend fun validateAndAddUser(user: User) {
        if (emailExists(user.email)) {
            throw DuplicateEmailException(user.email)
        }

        if (!isValidEmail(user.email)) {
            throw InvalidUserDataException()
        }

        if (user.password.isBlank()) {
            throw InvalidUserDataException()
        }

        if (!passwordRegex.matches(user.password)) {
            throw InvalidPasswordException()
        }

        val hashedPassword = hashPassword(user.password)
        val userWithHashedPassword = user.copy(password = hashedPassword)
        userRepository.addUser(userWithHashedPassword)
    }

    suspend fun getUsers(query: String?, limit: String?): UsersResponse {
        if (query == null) {
            throw InvalidUserDataException()
        }
        val limitInt = Integer.parseInt(limit)
        val filteredUsers = userRepository.usersByName(query, limitInt)
        val userResponses = filteredUsers.map { user ->
            UserResponse(email = user.email, name = user.name)
        }
        return UsersResponse(users = userResponses, total = userRepository.countUsers().toInt())
    }
}