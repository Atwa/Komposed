package io.github.atwa.komposed

/** Intercepts dispatched actions before they reach the reducers. Call [next] to forward the action downstream. */
typealias Middleware<S, A> = (state: S, action: A, next: (A) -> Unit) -> Unit

/** Creates a [Middleware] from a lambda — avoids explicit type parameters at the call site. */
fun <S> createMiddleware(block: Middleware<S, Any>): Middleware<S, Any> = block

fun <S> List<Middleware<S, Any>>.apply(state: S, action: Any, applyReducers: (Any) -> Unit) {
    val chain = foldRight(
        applyReducers
    ) { middleware, next ->
        { action: Any -> middleware(state, action, next) }
    }
    chain(action)
}
