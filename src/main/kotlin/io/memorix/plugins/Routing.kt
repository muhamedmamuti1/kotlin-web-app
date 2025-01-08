package io.memorix.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.autohead.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.resources.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.memorix.responses.handleException
import io.memorix.user.UserService
import io.memorix.user.user
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    install(AutoHeadResponse)
    install(Resources)
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            handleException(cause, call)
        }
    }
    routing {
        val userService: UserService by inject()

        get("/") {
            call.respondText("Hello World!")
        }
        user(userService)
    }
}
