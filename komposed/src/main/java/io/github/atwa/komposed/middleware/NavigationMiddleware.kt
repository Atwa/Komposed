package io.github.atwa.komposed.middleware

import android.util.Log
import io.github.atwa.komposed.effect.NavigationEffect
import io.github.atwa.komposed.navigation.Navigator

/**
 * Built-in middleware that intercepts [NavigationEffect] actions and executes their [Navigator]
 * lambda against the bound [navigator] instance.
 *
 * Place this in the middleware list **before** any middleware that should not see navigation
 * actions. All other actions are forwarded downstream unchanged via [next].
 */
fun <S> navigationMiddleware(navigator: Navigator) = createMiddleware<S> { _, action, next ->
    if (action is NavigationEffect) {
        Log.d("Navigator", action.toString())
        action.block(navigator)
    } else {
        next(action)
    }
}
