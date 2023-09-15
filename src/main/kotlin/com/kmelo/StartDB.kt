package com.kmelo

import com.kmelo.models.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

class StartDB {

    fun initDB(){
        Database.connect("jdbc:h2:mem:regular;DB_CLOSE_DELAY=-1;", "org.h2.Driver")

        transaction {
            SchemaUtils.create(Users)
        }
    }
}