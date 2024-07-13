package io.memorix.user

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.memorix.responses.handleException

fun Route.user() {
    val userService = UserService()

    route("/users") {
        /**
         * Create user endpoint.
         *
         * @body User full name, email and password.
         * @return Empty body with status 202 Accepted.
         */
        post {
            try {
                val user = call.receive<User>()
                userService.validateAndAddUser(user)
                call.respond(HttpStatusCode.Accepted)
            } catch (ex: Exception) {
                handleException(ex, call)
            }
        }

        /**
         * Search users endpoint.
         *
         * @param query Name of the users to search.
         * @param limit Limits the number of rows to return.
         * @return List of found users with the total number of users in system.
         */
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
