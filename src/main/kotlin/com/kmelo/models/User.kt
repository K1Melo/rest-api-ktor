package com.kmelo.models

import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object Users: Table(){
    val id: Column<String> = char("id", 36)
    val name: Column<String> = char("name", 50)

    override val primaryKey = PrimaryKey(id, name = "Users_Id")

    fun toUser(row: ResultRow): User = User(
        id = row[Users.id],
        name = row[Users.name],
    )
}

@Serializable
data class User (var id: String? = null, val name: String){
}