package io.github.atwa.komposed

/** A reducer that receives a [HANDLER] alongside state and action, enabling dependency injection
 *  without coupling the reducer to a concrete implementation. Bind it to a handler via [ReducerFactory.provide]. */
typealias Reducer<STATE, ACTION, HANDLER> = (
    state: STATE,
    action: ACTION,
    handler: HANDLER,
) -> ReduceType<STATE, ACTION>

/** Holds a [Reducer] and produces a [PureReducer] once a concrete [HANDLER] is supplied. */
class ReducerFactory<STATE, ACTION, HANDLER> internal constructor(
    private val reducerFn: Reducer<STATE, ACTION, HANDLER>,
) {
    /** Partially applies [handler], returning a [PureReducer] ready to be registered in the store. */
    fun provide(handler: HANDLER): PureReducer<STATE, ACTION> =
        { state, action -> reducerFn(state, action, handler) }
}

/** Creates a [ReducerFactory] from a [Reducer] lambda. */
fun <STATE, ACTION, HANDLER> reducer(
    factory: Reducer<STATE, ACTION, HANDLER>,
): ReducerFactory<STATE, ACTION, HANDLER> = ReducerFactory(factory)

/** Wires a handler and lifts the reducer into a global state slice in one step.
 *  Equivalent to [ReducerFactory.provide] followed by [PureReducer.pullback]. */
fun <GLOBAL, STATE, ACTION : Any, HANDLER> ReducerFactory<STATE, ACTION, HANDLER>.scoped(
    handler: HANDLER,
    lens: Lens<GLOBAL, STATE>,
): PureReducer<GLOBAL, ACTION> = provide(handler).pullback(lens)
