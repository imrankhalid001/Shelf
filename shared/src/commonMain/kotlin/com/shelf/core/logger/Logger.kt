package com.shelf.core.logger

interface Logger {
    fun debug(tag: String, message: String)
    fun info(tag: String, message: String)
    fun warning(tag: String, message: String)
    fun error(tag: String, message: String, throwable: Throwable? = null)
}

object SystemLogger : Logger {
    override fun debug(tag: String, message: String) {
        println("[DEBUG] [$tag] $message")
    }

    override fun info(tag: String, message: String) {
        println("[INFO] [$tag] $message")
    }

    override fun warning(tag: String, message: String) {
        println("[WARN] [$tag] $message")
    }

    override fun error(tag: String, message: String, throwable: Throwable?) {
        println("[ERROR] [$tag] $message ${throwable?.message ?: ""}")
    }
}
