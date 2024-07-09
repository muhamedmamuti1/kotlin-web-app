package io.memorix.user

import io.memorix.exceptions.*
import org.mindrot.jbcrypt.BCrypt
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.regex.Pattern

private val EMAIL_REGEX_PATTERN = Pattern.compile(
    "^\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b"
)

class UserService : KoinComponent {
    private val userRepository: UserRepositoryInterface by inject()

    private fun isValidEmail(email: String): Boolean {
        val matcher = EMAIL_REGEX_PATTERN.matcher(email)
        return matcher.matches()
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

        val hashedPassword = hashPassword(user.password)
        val userWithHashedPassword = user.copy(password = hashedPassword)
        userRepository.addUser(userWithHashedPassword)
    }

    suspend fun getUsers(query: String?, limit: String?): List<User> {
        if (query == null) {
            throw InvalidUserDataException()
        }
        val limitInt = Integer.parseInt(limit)
        return userRepository.usersByName(query, limitInt)
    }
}