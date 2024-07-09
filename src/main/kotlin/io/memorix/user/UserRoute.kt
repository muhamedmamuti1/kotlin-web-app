package io.memorix.user

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.memorix.responses.handleException

fun Route.user() {
    val userService = UserService()

    // Add your routes here
    route("/users") {
        post {
            try {
                val user = call.receive<User>()
                userService.validateAndAddUser(user)
                call.respond(HttpStatusCode.Accepted)
            } catch (ex: Exception) {
                handleException(ex, call)
            }
        }

        get {
            val query = call.request.queryParameters["query"]
            val limit = call.request.queryParameters["limit"]
            try {
                val users = userService.getUsers(query, limit)
                call.respond(users)
            } catch (ex: Exception) {
                handleException(ex, call)
            }
        }
    }
}
