package io.github.atwa.komposed

import io.github.atwa.komposed.effect.Effect
import io.github.atwa.komposed.effect.EffectHandler
import io.github.atwa.komposed.effect.NavigationEffect
import io.github.atwa.komposed.middleware.Middleware
import io.github.atwa.komposed.middleware.apply
import io.github.atwa.komposed.reducer.Reducer
import io.github.atwa.komposed.reducer.ReduceType
import io.github.atwa.komposed.subscription.Subscription
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlin.reflect.KClass

/** Observable container for state [S]. Dispatched actions pass through the middleware chain
 *  before being handled by the matching [Reducer]. Effects emitted by reducers are routed
 *  to registered [EffectHandler]s, which dispatch zero, one, or many actions back into the store.
 *
 *  Subscriptions watch derived values of [S]; when a selector's output changes after a state
 *  update the produced action is dispatched through the full pipeline automatically.
 *
 *  Threading is enforced internally — client code passes a plain scope with no dispatcher override:
 *  - Action routing and state updates run on [Dispatchers.Main.immediate]
 *  - Effect handler bodies run on [Dispatchers.IO]
 *  - Actions dispatched back from handlers are posted to [Dispatchers.Main.immediate] */
interface Store<S> {
    val state: StateFlow<S>
    val reducers: Map<KClass<*>, Reducer<S, Any>>
    fun dispatch(action: Any)
}

private class StoreImpl<S>(
    initialValue: S,
    override val reducers: Map<KClass<*>, Reducer<S, Any>>,
    private val effectHandlers: Map<KClass<*>, EffectHandler<Effect, Any>>,
    private val middlewares: List<Middleware<S, Any>>,
    private val subscriptions: List<Subscription<S, *>>,
    scope: CoroutineScope,
    mainDispatcher: CoroutineDispatcher,
    ioDispatcher: CoroutineDispatcher,
) : Store<S> {

    private val _state = MutableStateFlow(initialValue)
    override val state: StateFlow<S> = _state.asStateFlow()

    private val mainScope = scope + mainDispatcher
    private val ioScope = scope + ioDispatcher

    override fun dispatch(action: Any) {
        mainScope.launch {
            middlewares.apply(_state.value, action) { applyReducers(it) }
        }
    }

    private fun applyReducers(action: Any) {
        val reducer = reducers.entries.firstOrNull { it.key.isInstance(action) }?.value ?: return
        val previousState = _state.value
        when (val result = reducer.invoke(previousState, action)) {
            is ReduceType.Reduce -> {
                _state.update { result.state }
                notifySubscriptions(previousState, result.state)
            }
            is ReduceType.ReduceWithEffect -> {
                _state.update { result.state }
                notifySubscriptions(previousState, result.state)
                ioScope.launch { handleEffect(result.effect) }
            }
            is ReduceType.SideEffect -> ioScope.launch { handleEffect(result.effect) }
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun notifySubscriptions(previousState: S, newState: S) {
        for (subscription in subscriptions) {
            val sub = subscription as Subscription<S, Any?>
            val previousValue = sub.selector(previousState)
            val newValue = sub.selector(newState)
            if (previousValue != newValue) {
                dispatch(sub.action(newValue))
            }
        }
    }

    private suspend fun handleEffect(effect: Effect) {
        when (effect) {
            is NavigationEffect -> dispatch(effect)
            else -> {
                val handler = effectHandlers.entries.firstOrNull { it.key.isInstance(effect) }?.value
                handler?.handle(effect) { actionProducer ->
                    val action = actionProducer()
                    mainScope.launch { applyReducers(action) }
                }
            }
        }
    }
}

/** Creates a [Store] wired with the given [reducers], [effectHandlers], [middlewares],
 *  [subscriptions], and coroutine [scope].
 *
 *  Pass a plain [scope] (e.g. `viewModelScope`) with no dispatcher override — [mainDispatcher] and
 *  [ioDispatcher] are applied internally. Override them only in tests via [io.github.atwa.komposed.testing.TestStore]. */
fun <S> createStore(
    initialValue: S,
    scope: CoroutineScope,
    middlewares: List<Middleware<S, Any>> = emptyList(),
    reducers: Map<KClass<*>, Reducer<S, Any>>,
    effectHandlers: Map<KClass<*>, EffectHandler<Effect, Any>> = emptyMap(),
    subscriptions: List<Subscription<S, *>> = emptyList(),
    mainDispatcher: CoroutineDispatcher = Dispatchers.Main.immediate,
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
): Store<S> = StoreImpl(
    initialValue = initialValue,
    reducers = reducers,
    effectHandlers = effectHandlers,
    middlewares = middlewares,
    subscriptions = subscriptions,
    scope = scope,
    mainDispatcher = mainDispatcher,
    ioDispatcher = ioDispatcher,
)
