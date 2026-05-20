package io.github.atwa.komposed

import kotlinx.coroutines.flow.Flow

/**
 * Marker for an asynchronous operation that may produce [ACTION] values dispatched back to the store.
 *
 * Four concrete types cover the common async patterns:
 * - [SuspendEffect] — fire-and-forget, no action dispatched
 * - [ActionableEffect] — suspends once, dispatches the returned action
 * - [FlowEffect] — collects a stream, dispatches each emission
 * - [NavigationEffect] — executes a navigation command via [Navigator]; re-dispatched through
 *   the middleware chain so [navigationMiddleware] can intercept it before reducers see it
 */
interface Effect<out ACTION>

/** Runs [handle] once without dispatching any action — useful for fire-and-forget side effects
 *  such as analytics calls or cache writes. */
data class SuspendEffect(val handle: suspend () -> Unit) : Effect<Nothing>

/** Invokes [actionProducer] once and dispatches the returned action back through the store pipeline. */
data class ActionableEffect<out ACTION>(val actionProducer: suspend () -> ACTION) : Effect<ACTION> {
    suspend fun handle(handle: (@UnsafeVariance ACTION) -> Unit) = handle(actionProducer())
}

/** Collects [emitter] and dispatches each emitted value as an action — use for ongoing streams
 *  such as database observation or WebSocket feeds. */
data class FlowEffect<out ACTION>(private val emitter: () -> Flow<ACTION>) : Effect<ACTION> {
    suspend fun handle(handle: (@UnsafeVariance ACTION) -> Unit) = emitter().collect { handle(it) }
}

/** A navigation side effect expressed as a [Navigator] lambda.
 *
 * The store does not execute this effect directly. Instead it re-dispatches the instance back
 * through the middleware chain, where [navigationMiddleware] intercepts it and invokes [block]
 * with the bound [Navigator]. Reducers never receive a [NavigationEffect]. */
data class NavigationEffect(val block: Navigator.() -> Unit) : Effect<Nothing>
