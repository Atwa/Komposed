package io.github.atwa.komposed.sample.core.middleware

import android.util.Log
import io.github.atwa.komposed.createMiddleware

fun <S> analyticsMiddleware() = createMiddleware<S> { _, action, next ->
    Log.d("Analytics", "→ $action tracked")
    next(action)
}
