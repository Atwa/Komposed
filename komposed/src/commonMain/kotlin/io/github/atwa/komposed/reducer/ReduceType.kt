package io.github.atwa.komposed.reducer

import io.github.atwa.komposed.effect.Effect

/** The outcome of a reducer invocation — either a state change, an effect, or both. */
sealed interface ReduceType<STATE> {

    /** Pure state transition with no side effects. */
    data class Reduce<STATE>(val state: STATE) : ReduceType<STATE>

    /** State transition accompanied by a side [effect]. */
    data class ReduceWithEffect<STATE>(val state: STATE, val effect: Effect) : ReduceType<STATE>

    /** An [effect] with no state change. */
    data class SideEffect<STATE>(val effect: Effect) : ReduceType<STATE>

    companion object {

        /** Wraps this state in a [Reduce] with no side effects. */
        fun <STATE> STATE.reduce(): ReduceType<STATE> = Reduce(this)

        /** Pairs this state with an [Effect], producing a [ReduceWithEffect]. */
        fun <STATE> STATE.withEffect(effect: () -> Effect): ReduceType<STATE> =
            ReduceWithEffect(this, effect())

        /** Returns a [SideEffect] with no state change. */
        fun <STATE> effect(effect: () -> Effect): ReduceType<STATE> = SideEffect(effect())
    }
}
