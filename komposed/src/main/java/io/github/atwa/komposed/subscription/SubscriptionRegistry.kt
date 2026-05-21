package io.github.atwa.komposed.subscription

/** Collects [Subscription]s to be passed to [io.github.atwa.komposed.createStore]. */
class SubscriptionRegistry<S> {
    internal val subscriptions = mutableListOf<Subscription<S, *>>()

    /** Registers a subscription that watches [selector] on global state [S].
     *  Whenever the selected value changes, [action] is called with the new value
     *  and the result is dispatched through the full store pipeline. */
    fun <T> subscription(selector: (S) -> T, action: (T) -> Any) {
        subscriptions.add(Subscription(selector, action))
    }
}

/** DSL for building the subscription list passed to [io.github.atwa.komposed.createStore]. */
fun <S> subscriptions(build: SubscriptionRegistry<S>.() -> Unit): List<Subscription<S, *>> =
    SubscriptionRegistry<S>().apply(build).subscriptions
