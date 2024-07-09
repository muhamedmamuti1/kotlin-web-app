package io.memorix.user

import org.koin.dsl.module

val userDi = module {
    single<UserRepositoryInterface> { UserRepository(database = get()) }
    single { UserService() }
}