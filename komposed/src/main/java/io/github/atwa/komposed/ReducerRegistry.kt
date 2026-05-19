package io.github.atwa.komposed

import kotlin.reflect.KClass

/** Collects [PureReducer]s keyed by their handled action type. */
class ReducerRegistry<S> {

    val reducers = mutableMapOf<KClass<*>, PureReducer<S, Any>>()

    /** Wires [handler] into [this] factory, lifts the result into the global state via [lens],
     *  and registers it — all in one call. Shadows the top-level [scoped] extension inside
     *  the [reducers] DSL so no explicit registration is needed. */
    inline fun <reified ACTION : Any, LOCAL, HANDLER> ReducerFactory<LOCAL, ACTION, HANDLER>.scoped(
        handler: HANDLER,
        lens: Lens<S, LOCAL>,
    ) {
        @Suppress("UNCHECKED_CAST")
        reducers[ACTION::class] = provide(handler).pullback(lens) as PureReducer<S, Any>
    }

    /** Lifts this handler-free [PureReducer] into the global state via [lens] and registers it. */
    inline fun <reified ACTION : Any, LOCAL> PureReducer<LOCAL, ACTION>.scoped(
        lens: Lens<S, LOCAL>,
    ) {
        @Suppress("UNCHECKED_CAST")
        reducers[ACTION::class] = pullback(lens) as PureReducer<S, Any>
    }

    /** Registers a [PureReducer] that already operates on the global state [S]. */
    inline fun <reified ACTION : Any> PureReducer<S, ACTION>.register() {
        @Suppress("UNCHECKED_CAST")
        reducers[ACTION::class] = this as PureReducer<S, Any>
    }
}

/** DSL for building the reducer map passed to [createStore]. */
inline fun <S> reducers(build: ReducerRegistry<S>.() -> Unit): Map<KClass<*>, PureReducer<S, Any>> =
    ReducerRegistry<S>().apply(build).reducers
