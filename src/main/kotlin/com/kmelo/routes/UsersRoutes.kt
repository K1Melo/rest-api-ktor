package com.kmelo.routes

import com.kmelo.models.Users
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class UsersRoutes {
    fun Route.userRouting(){
        route("/user"){
            get{
                val users = transaction {
                    Users.selectAll().map { Users.toUser(it) }
                }
                return@get call.respond(users)
            }
            get("/{id}") {

            }
        }
    }
}