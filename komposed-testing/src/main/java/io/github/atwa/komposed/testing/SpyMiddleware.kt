package io.github.atwa.komposed.testing

import io.github.atwa.komposed.middleware.Middleware

/**
 * Shared call log passed to multiple [SpyMiddleware] instances to assert cross-middleware
 * execution order in a single list.
 */
class MiddlewareCallLog {
    val log = mutableListOf<String>()

    fun <S> spy(name: String, intercept: Boolean = false): SpyMiddleware<S> =
        SpyMiddleware(name, log, intercept)

    fun assertOrder(vararg expected: String) {
        if (log != expected.toList()) throw AssertionError(
            "Expected middleware call order:\n${expected.toList()}\n\nActual:\n$log"
        )
    }
}

/**
 * Records every action it sees and its position in the middleware chain relative to [next].
 * If [intercept] is true, [next] is never called — simulating a middleware that swallows actions.
 */
class SpyMiddleware<S>(
    private val name: String,
    private val callLog: MutableList<String>,
    private val intercept: Boolean = false,
) {
    val capturedActions = mutableListOf<Any>()

    val middleware: Middleware<S, Any> = { _, action, next ->
        callLog.add("$name:before")
        capturedActions.add(action)
        if (!intercept) {
            next(action)
            callLog.add("$name:after")
        }
    }
}
