package io.memorix.user

import io.ktor.server.plugins.*
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

    private fun String.isValidEmail(emailRegex: Regex): Boolean {
        return emailRegex.matches(this)
    }

    private fun String.isValidPassword(passwordRegex: Regex): Boolean {
        return passwordRegex.matches(this)
    }

    private suspend fun emailExists(email: String): Boolean {
        val users = userRepository.allUsers()
        return users.any { it.email == email }
    }

    /**
     * This function encrypts a given password.
     *
     * @param password The plaintext password to encrypt.
     * @return The encrypted password.
     */
    private fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    /**
     * This function validates user's name, email and password and creates the user.
     *
     * @param user User object.
     */
    suspend fun validateAndAddUser(user: User) {
        if (user.name.isBlank()) {
            throw BadRequestException(ResponseMessages.INVALID_USER_NAME)
        }

        if (emailExists(user.email)) {
            throw DuplicateEmailException(user.email)
        }

        if (!user.email.isValidEmail(emailRegex)) {
            throw InvalidUserDataException()
        }

        if (user.password.isBlank()) {
            throw InvalidUserDataException()
        }

        if (!user.password.isValidPassword(passwordRegex)) {
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