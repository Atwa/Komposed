package io.github.atwa.komposed

/**
 * Intercepts dispatched actions before they reach the reducers.
 *
 * Call [next] to forward the action downstream to the next middleware or the reducer.
 * Omit the [next] call to swallow the action entirely — useful for feature flags or auth guards.
 * Middleware executes in declaration order; the [next] chain unwinds in reverse (outermost last).
 */
typealias Middleware<S, A> = (state: S, action: A, next: (A) -> Unit) -> Unit

/** Creates a [Middleware] from a lambda — avoids explicit type parameters at the call site. */
fun <S> createMiddleware(block: Middleware<S, Any>): Middleware<S, Any> = block

/**
 * Builds a right-to-left middleware chain and invokes it with [action].
 *
 * The rightmost middleware is closest to [applyReducers]; the leftmost middleware is the first
 * to receive the action and the last to resume after [next] returns.
 */
fun <S> List<Middleware<S, Any>>.apply(state: S, action: Any, applyReducers: (Any) -> Unit) {
    val chain = foldRight(
        applyReducers
    ) { middleware, next ->
        { action: Any -> middleware(state, action, next) }
    }
    chain(action)
}
