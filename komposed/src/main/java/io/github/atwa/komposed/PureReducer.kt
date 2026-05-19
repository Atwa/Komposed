package io.github.atwa.komposed

/** A side-effect-free reducer: maps (state, action) to a [ReduceType] with no external dependencies. */
typealias PureReducer<STATE, ACTION> = (
    state: STATE,
    action: ACTION,
) -> ReduceType<STATE, ACTION>

/** Creates a [PureReducer] from a lambda. */
fun <STATE, ACTION> pureReducer(
    reduce: (STATE, ACTION) -> ReduceType<STATE, ACTION>,
): PureReducer<STATE, ACTION> = { state, action -> reduce(state, action) }

/** Lifts a [PureReducer] on a local state slice into a [PureReducer] on the global state,
 *  using [selector] to extract the slice and [modifier] to write it back. */
fun <GLOBAL, STATE, ACTION> PureReducer<STATE, ACTION>.pullback(
    selector: (global: GLOBAL) -> STATE,
    modifier: (global: GLOBAL, local: STATE) -> GLOBAL,
): PureReducer<GLOBAL, ACTION> = { global, action ->
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

/** Lifts a [PureReducer] using a [Lens] for zero-boilerplate pullback. */
fun <GLOBAL, STATE, ACTION> PureReducer<STATE, ACTION>.pullback(
    lens: Lens<GLOBAL, STATE>,
): PureReducer<GLOBAL, ACTION> = pullback(selector = lens.get, modifier = lens.set)
