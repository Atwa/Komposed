package io.github.atwa.komposed

/** The outcome of a reducer invocation — either a state change, an effect, or both. */
sealed interface ReduceType<STATE, out ACTION> {

    /** Pure state transition with no side effects. */
    data class Reduce<STATE, out ACTION>(val state: STATE) : ReduceType<STATE, ACTION>

    /** State transition accompanied by a side [effect]. */
    data class ReduceWithEffect<STATE, out ACTION>(val state: STATE, val effect: Effect<ACTION>) :
        ReduceType<STATE, ACTION>

    /** An [effect] with no state change. */
    data class SideEffect<STATE, out ACTION>(val effect: Effect<ACTION>) : ReduceType<STATE, ACTION>

    companion object {

        /** Wraps this state in a [Reduce] with no side effects. */
        fun <STATE, ACTION> STATE.reduce(): ReduceType<STATE, ACTION> = Reduce(this)

        /** Pairs this state with an [Effect], producing a [ReduceWithEffect]. */
        fun <STATE, ACTION> STATE.withEffect(
            effect: () -> Effect<ACTION>,
        ): ReduceType<STATE, ACTION> = ReduceWithEffect(this, effect())

        /** Returns a [SideEffect] with no state change. */
        fun <STATE, ACTION> effect(
            effect: () -> Effect<ACTION>,
        ): ReduceType<STATE, ACTION> = SideEffect(effect())
    }
}
