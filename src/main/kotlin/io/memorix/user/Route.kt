package io.memorix.user

import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.memorix.exceptions.*

fun Route.user() {
    val userService = UserService()

    // Add your routes here
    route("/users") {
        get {
            val query = call.request.queryParameters["query"]
            val limit = call.request.queryParameters["limit"]
            val users = userService.getUsers(query, limit)
            call.respond(users)
        }

        post {
            try {
                val user = call.receive<User>()
                userService.validateAndAddUser(user)
                call.respond(HttpStatusCode.Accepted)
            } catch (ex: DuplicateEmailException) {
                call.respond(HttpStatusCode.BadRequest, ex.message ?: "Duplicate e-mail}")
            } catch (ex: InvalidUserDataException) {
                call.respond(HttpStatusCode.BadRequest, ex.message ?: "Invalid user data")
            } catch (ex: IllegalStateException) {
                call.respond(HttpStatusCode.BadRequest)
            } catch (ex: JsonConvertException) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }
    }
}
