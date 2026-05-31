package io.github.atwa.komposed.testing

actual fun <S> ReduceResult<S>.stateDiff(): List<PropertyChange> = listOf(
    PropertyChange("state", previousState, nextState)
)
