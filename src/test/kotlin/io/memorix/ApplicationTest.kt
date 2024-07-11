package io.memorix

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.memorix.plugins.configureRouting
import io.memorix.responses.DuplicateEmailException
import io.memorix.responses.InvalidPasswordException
import io.memorix.responses.InvalidUserDataException
import io.memorix.user.User
import io.memorix.user.UserService
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureRouting()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
    @Test
    suspend fun testApiResponses() {
        val userService = UserService()

        // Test valid user
        val validUser = User("John Doe", "john.doe@example.com", "Test123")
        assertDoesNotThrow { userService.validateAndAddUser(validUser) }

        // Test duplicate email
        val duplicateEmailUser = User("John Doe", "john.doe@example.com", "Test123")
        assertThrows<DuplicateEmailException> { userService.validateAndAddUser(duplicateEmailUser) }

        // Test invalid email format
        val invalidEmailUser = User("Another Test", "invalid_email", "Password1")
        assertThrows<InvalidUserDataException> { userService.validateAndAddUser(invalidEmailUser) }

        // Test password validations
        val invalidPasswordUser = User("Test User", "new.user@example.com", "short")
        assertThrows<InvalidPasswordException> { userService.validateAndAddUser(invalidPasswordUser) }
    }
}
