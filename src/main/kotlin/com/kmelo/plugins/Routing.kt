package com.kmelo.plugins

import com.kmelo.models.User
import com.kmelo.models.Users
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.request.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

fun Application.configureRouting() {
    routing {
        route("/user"){
            get{
                val users = transaction {
                    Users.selectAll().map { Users.toUser(it) }
                }
                return@get call.respond(users)
            }

            get("/{id}") {
                val id = call.parameters["id"] ?: return@get call.respondText(
                    "User not found", status = HttpStatusCode.NotFound
                )

                val user: List<User> = transaction { Users.select{
                    Users.id eq id
                }.map { Users.toUser(it) } }

                if(user.isNotEmpty()){
                    return@get call.respond(user.first())
                }

                return@get call.respondText(
                    "User not found",
                    status = HttpStatusCode.NotFound
                )
            }

            post {
                val user = call.receive<User>()

                user.id = UUID.randomUUID().toString()

                transaction {
                    Users.insert {
                        it[id] = user.id!!
                        it[name] = user.name
                    }
                }

                call.respondText("Created", status = HttpStatusCode.Created )
            }

            delete("/{id}") {
                val id = call.parameters["id"] ?: return@delete call.respondText(
                    "Insert user id to delete",
                    status = HttpStatusCode.BadRequest
                )

                val delete: Int = transaction {
                    Users.deleteWhere { Users.id eq id }
                }

                if (delete == 1) {
                    return@delete call.respondText("Deleted", status = HttpStatusCode.OK)
                }

                return@delete call.respondText("User not found", status = HttpStatusCode.NotFound)
            }
        }
    }
}