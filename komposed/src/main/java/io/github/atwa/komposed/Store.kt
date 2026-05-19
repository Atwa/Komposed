package io.github.atwa.komposed

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.reflect.KClass

/** Observable container for state [S]. Dispatched actions pass through the middleware chain
 *  before being handled by the matching [PureReducer]. */
interface Store<S> {
    val state: StateFlow<S>
    val reducers: Map<KClass<*>, PureReducer<S, Any>>
    fun dispatch(action: Any)
}

private class StoreImpl<S>(
    initialValue: S,
    override val reducers: Map<KClass<*>, PureReducer<S, Any>>,
    private val middlewares: List<Middleware<S, Any>>,
    private val scope: CoroutineScope,
) : Store<S> {

    private val _state = MutableStateFlow(initialValue)
    override val state: StateFlow<S> = _state.asStateFlow()

    override fun dispatch(action: Any) {
        middlewares.apply(_state.value, action) { action: Any -> reducers.apply(action) }
    }

    private fun Map<KClass<*>, PureReducer<S, Any>>.apply(action: Any) {
        val reducer = entries.firstOrNull { it.key.isInstance(action) }?.value ?: return
        when (val result = reducer.invoke(_state.value, action)) {
            is ReduceType.Reduce -> _state.update { result.state }

            is ReduceType.ReduceWithEffect -> {
                _state.update { result.state }
                scope.launch { handleEffect(result.effect) }
            }

            is ReduceType.SideEffect -> scope.launch {
                handleEffect(result.effect)
            }
        }
    }

    private suspend fun handleEffect(effect: Effect<Any>) {
        when (effect) {
            is NavigationEffect -> scope.launch { dispatch(effect) }
            is SuspendEffect -> effect.handle()
            is ActionableEffect<Any> -> effect.handle { scope.launch { dispatch(it) } }
            is FlowEffect<Any> -> effect.handle { scope.launch { dispatch(it) } }
        }
    }
}

/** Creates a [Store] wired with the given [reducers], [middlewares], and coroutine [scope]. */
fun <S> createStore(
    initialValue: S,
    scope: CoroutineScope,
    middlewares: List<Middleware<S, Any>> = emptyList(),
    reducers: Map<KClass<*>, PureReducer<S, Any>>,
): Store<S> = StoreImpl(
    initialValue = initialValue,
    reducers = reducers,
    middlewares = middlewares,
    scope = scope,
)
