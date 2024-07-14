package io.memorix.helpers

import mu.KotlinLogging

object Logger {
    private val logger = KotlinLogging.logger {}
    fun error(message: String, throwable: Throwable? = null) {
        if (throwable != null) {
            logger.error(throwable) { message }
        } else {
            logger.error { message }
        }
    }
}
