    package io.github.atwa.komposed.reducer

import io.github.atwa.komposed.Lens

/** A side-effect-free reducer: maps (state, action) to a [ReduceType] with no external dependencies. */
typealias Reducer<STATE, ACTION> = (
    state: STATE,
    action: ACTION,
) -> ReduceType<STATE>

/** Creates a [Reducer] from a lambda. */
fun <STATE, ACTION> reducer(
    reduce: (STATE, ACTION) -> ReduceType<STATE>,
): Reducer<STATE, ACTION> = { state, action -> reduce(state, action) }

/** Lifts a [Reducer] on a local state slice into a [Reducer] on the global state,
 *  using [selector] to extract the slice and [modifier] to write it back. */
fun <GLOBAL, STATE, ACTION> Reducer<STATE, ACTION>.pullback(
    selector: (global: GLOBAL) -> STATE,
    modifier: (global: GLOBAL, local: STATE) -> GLOBAL,
): Reducer<GLOBAL, ACTION> = { global, action ->
    when (val result = invoke(selector(global), action)) {
        is ReduceType.Reduce -> ReduceType.Reduce(modifier(global, result.state))
        is ReduceType.ReduceWithEffect -> ReduceType.ReduceWithEffect(
            state = modifier(global, result.state),
            effect = result.effect,
        )
        is ReduceType.SideEffect -> ReduceType.ReduceWithEffect(
            state = global,
            effect = result.effect,
        )
    }
}

/** Lifts a [Reducer] using a [Lens] for zero-boilerplate pullback. */
fun <GLOBAL, STATE, ACTION> Reducer<STATE, ACTION>.pullback(
    lens: Lens<GLOBAL, STATE>,
): Reducer<GLOBAL, ACTION> = pullback(selector = lens.get, modifier = lens.set)
