package io.memorix.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> suspendTransaction(database: Database, block: Transaction.() -> T): T =
    newSuspendedTransaction(Dispatchers.IO, db = database, statement = block)
