package io.memorix.user

import io.ktor.server.plugins.*
import io.memorix.responses.*
import org.mindrot.jbcrypt.BCrypt
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class UserService : KoinComponent {
    private val userRepository: UserRepositoryInterface by inject()

    private fun String.isValidName(): Boolean {
        val nameRegex = Regex("^[a-zA-Z\\s]+\$")
        return nameRegex.matches(this)
    }

    private fun String.isValidEmail(): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")
        return emailRegex.matches(this)
    }

    private fun String.isValidPassword(): Boolean {
        val passwordRegex = Regex("^(?=.*[A-Z]).{6,}\$")
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

        if (!user.name.isValidName()) {
            throw BadRequestException(ResponseMessages.NAME_MUST_BE_STRING)
        }

        if (emailExists(user.email)) {
            throw DuplicateEmailException(user.email)
        }

        if (!user.email.isValidEmail()) {
            throw InvalidUserDataException()
        }

        if (user.password.isBlank()) {
            throw InvalidUserDataException()
        }

        if (!user.password.isValidPassword()) {
            throw InvalidPasswordException()
        }

        val hashedPassword = hashPassword(user.password)
        val userWithHashedPassword = user.copy(password = hashedPassword)
        userRepository.addUser(userWithHashedPassword)
    }

    suspend fun getUsers(query: String?, limit: String?): UsersResponse {
        // Validate the query parameter
        if (query == null || !query.isValidName()) {
            throw InvalidQueryParamException()
        }
        // Validate and parse the limit parameter
        val limitInt = try {
            limit?.toInt() ?: throw InvalidQueryParamException()
        } catch (e: NumberFormatException) {
            throw InvalidQueryParamException()
        }
        val filteredUsers = userRepository.usersByName(query, limitInt)
        val userResponses = filteredUsers.map { user ->
            UserResponse(email = user.email, name = user.name)
        }
        return UsersResponse(users = userResponses, total = userRepository.countUsers().toInt())
    }
}