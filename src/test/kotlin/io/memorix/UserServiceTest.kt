package io.memorix

import io.memorix.responses.*
import io.memorix.user.User
import io.memorix.user.UserRepositoryInterface
import io.memorix.user.UserService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.inject
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

val testModule = module {
    single<UserRepositoryInterface> { mockk(relaxed = true) }
    single { UserService() }
}

class UserServiceTest : KoinTest {
    private val userService: UserService by inject()
    private val userRepository: UserRepositoryInterface by inject()

    @Before
    fun setup() {
        startKoin {
            modules(testModule)
        }
    }

    @After
    fun teardown() {
        stopKoin()
    }

    @Test
    fun `test validateAndAddUser with valid data`() = runBlocking<Unit> {
        val user = User(name = "Test Test", email = "test@example.com", password = "Password1")

        coEvery { userRepository.allUsers() } returns emptyList()
        coEvery { userRepository.addUser(any()) } returns Unit

        userService.validateAndAddUser(user)

        coVerify { userRepository.addUser(any()) }
    }

    @Test
    fun `test validateAndAddUser with duplicate email`() = runBlocking<Unit> {
        val user = User(name = "Test Test", email = "test@example.com", password = "Password1")

        coEvery { userRepository.allUsers() } returns listOf(user)

        assertFailsWith<DuplicateEmailException> {
            userService.validateAndAddUser(user)
        }
    }

    @Test
    fun `test getUsers with valid query`() = runBlocking<Unit> {
        val users = listOf(User(name = "Test Test", email = "test@example.com", password = "Password1"))
        coEvery { userRepository.usersByName("Test", 10) } returns users
        coEvery { userRepository.countUsers() } returns 1

        val response = userService.getUsers("Test", "10")

        assertEquals(1, response.total)
        assertEquals("test@example.com", response.users.first().email)
    }

    @Test
    fun `test getUsers with null query`() = runBlocking<Unit> {
        assertFailsWith<InvalidQueryParamException> {
            userService.getUsers(null, "10")
        }
    }
}
