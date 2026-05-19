package io.github.atwa.komposed

import kotlinx.coroutines.flow.Flow

/** Marker for an asynchronous operation that may produce [ACTION] values dispatched back to the store. */
interface Effect<out ACTION>

/** Runs [handle] once without dispatching any action — useful for fire-and-forget side effects. */
data class SuspendEffect(val handle: suspend () -> Unit) : Effect<Nothing>

/** Invokes [actionProducer] once and dispatches the returned action. */
data class ActionableEffect<out ACTION>(val actionProducer: suspend () -> ACTION) : Effect<ACTION> {
    suspend fun handle(handle: (@UnsafeVariance ACTION) -> Unit) = handle(actionProducer())
}

/** Collects [emitter] and dispatches each emitted value as an action. */
data class FlowEffect<out ACTION>(private val emitter: () -> Flow<ACTION>) : Effect<ACTION> {
    suspend fun handle(handle: (@UnsafeVariance ACTION) -> Unit) = emitter().collect { handle(it) }
}
