package io.github.atwa.komposed.testing

actual fun <S> ReduceResult<S>.stateDiff(): List<PropertyChange> =
    (previousState as Any).javaClass.declaredFields
        .filter { !it.isSynthetic }
        .mapNotNull { field ->
            field.isAccessible = true
            val before = field.get(previousState)
            val after = field.get(nextState)
            if (before != after) PropertyChange(field.name, before, after) else null
        }
