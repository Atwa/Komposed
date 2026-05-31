package io.github.atwa.komposed.app.core.middleware

import io.github.atwa.komposed.middleware.createMiddleware

fun <S> analyticsMiddleware() = createMiddleware<S> { _, action, next ->
    println("Analytics → $action tracked")
    next(action)
}
