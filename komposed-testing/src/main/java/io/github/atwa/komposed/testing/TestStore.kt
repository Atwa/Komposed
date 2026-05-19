package io.github.atwa.komposed.testing

import io.github.atwa.komposed.Middleware
import io.github.atwa.komposed.PureReducer
import io.github.atwa.komposed.Store
import io.github.atwa.komposed.createMiddleware
import io.github.atwa.komposed.createStore
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.test.TestScope
import kotlin.reflect.KClass

/**
 * Wraps a real [Store] for integration-level tests. A recording middleware sits at position 0
 * in the chain so every action — including secondary dispatches produced by effects — is
 * captured in [dispatchedActions].
 *
 * Use [kotlinx.coroutines.test.advanceUntilIdle] after [dispatch] to let effect coroutines run
 * before asserting state or secondary actions.
 */
class TestStore<S>(
    initialState: S,
    reducers: Map<KClass<*>, PureReducer<S, Any>>,
    middlewares: List<Middleware<S, Any>> = emptyList(),
    scope: TestScope,
) {
    private val _dispatchedActions = mutableListOf<Any>()
    val dispatchedActions: List<Any> get() = _dispatchedActions.toList()

    private val recordingMiddleware: Middleware<S, Any> = createMiddleware { _, action, next ->
        _dispatchedActions.add(action)
        next(action)
    }

    private val store: Store<S> = createStore(
        initialValue = initialState,
        scope = scope,
        middlewares = listOf(recordingMiddleware) + middlewares,
        reducers = reducers,
    )

    val state: StateFlow<S> get() = store.state

    fun dispatch(action: Any) = store.dispatch(action)

    fun assertState(expected: S) {
        if (state.value != expected) throw AssertionError(
            "Expected state:\n$expected\n\nActual state:\n${state.value}"
        )
    }

    fun assertActionsDispatched(vararg expected: Any) {
        if (dispatchedActions != expected.toList()) throw AssertionError(
            "Expected dispatched actions:\n${expected.toList()}\n\nActual:\n$dispatchedActions"
        )
    }
}
