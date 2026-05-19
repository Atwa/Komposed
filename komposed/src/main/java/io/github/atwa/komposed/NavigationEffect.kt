package io.github.atwa.komposed



/** A navigation side effect expressed as a [Navigator] lambda. Dispatched back through the
 *  middleware chain via [NavigationEffect] so [navigationMiddleware] can intercept it. */
data class NavigationEffect(val block: Navigator.() -> Unit) : Effect<Nothing>
