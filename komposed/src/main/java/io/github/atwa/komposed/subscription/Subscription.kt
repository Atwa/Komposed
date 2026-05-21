package io.github.atwa.komposed.subscription

/** Watches a derived value [T] from global state [S]. When [selector] produces a value that
 *  differs from its previous result, [action] is called with the new value and the returned
 *  action is dispatched through the full store pipeline — middleware, reducer, and effect routing.
 *
 *  Subscriptions are declared at the composition level (e.g. ViewModel), which keeps individual
 *  feature modules unaware of each other. */
data class Subscription<S, T>(
    val selector: (S) -> T,
    val action: (T) -> Any,
)
