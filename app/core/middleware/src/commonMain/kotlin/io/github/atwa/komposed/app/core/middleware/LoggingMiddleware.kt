package io.github.atwa.komposed.app.core.middleware

import io.github.atwa.komposed.middleware.createMiddleware

fun <S> loggingMiddleware() = createMiddleware<S> { _, action, next ->
    println("Logger → $action")
    next(action)
    println("Logger ← $action dispatched")
}
