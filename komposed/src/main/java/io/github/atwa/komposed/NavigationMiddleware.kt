package io.github.atwa.komposed

import android.util.Log
import io.github.atwa.komposed.NavigationEffect
import io.github.atwa.komposed.createMiddleware

fun <S> navigationMiddleware(navigator: Navigator) = createMiddleware<S> { _, action, next ->
    if (action is NavigationEffect) {
        Log.d("Navigator", action.toString())
        action.block(navigator)
    } else {
        next(action)
    }
}
