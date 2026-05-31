package io.github.atwa.komposed.testing

/**
 * Returns a list of [PropertyChange] entries for every field that differs between
 * [previousState] and [nextState] — useful for debugging assertion failures.
 */
expect fun <S> ReduceResult<S>.stateDiff(): List<PropertyChange>
