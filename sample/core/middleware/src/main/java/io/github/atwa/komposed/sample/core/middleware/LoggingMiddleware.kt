package io.github.atwa.komposed.sample.core.middleware

import android.util.Log
import io.github.atwa.komposed.createMiddleware

fun <S> loggingMiddleware() = createMiddleware<S> { _, action, next ->
    Log.d("Logger", "→ $action")
    next(action)
    Log.d("Logger", "← $action dispatched")
}
