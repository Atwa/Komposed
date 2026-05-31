# A TCA-Inspired Architecture for Kotlin & Jetpack Compose — Why I Built Komposed

*How a question from an iOS engineer friend exposed a gap in the Android ecosystem — and what I built to fill it.*

---

## The Call That Started Everything

It was a regular Tuesday evening when I got a message from an iOS engineer friend of mine. He had just been handed a
new project — an Android app, built from scratch. No existing codebase, no inherited architecture, just a blank
`build.gradle.kts` and a deadline.

His first question was not about Kotlin syntax or Compose APIs. It was this:

> *"What's the Android equivalent of TCA?"*

And I paused.

For context: [The Composable Architecture (TCA)](https://github.com/pointfreeco/swift-composable-architecture) by
Point-Free is, for many iOS engineers, the gold standard of scalable, testable, predictable app architecture. It gives
you pure reducers, typed effects, composable features through lens-based state slicing, and a testing DSL that lets you
assert the exact sequence of state changes and effects your app produces. It is opinionated in all the right ways.

My friend wasn't asking for an architectural pattern. He was asking for a *framework* — something with the same
structural guarantees, the same composability, the same first-class testability. Something you wire up and then spend
your time writing app logic instead of reinventing plumbing.

The honest answer I gave him in that moment was: *I don't know of one.*

That answer bothered me enough that I spent the next week building one.

---

## What Already Exists — And Where It Falls Short

Before building anything, I spent time seriously evaluating the best existing options. Two stood out.

### [ReduxKotlin](https://reduxkotlin.org/)

[ReduxKotlin](https://reduxkotlin.org/) is a faithful, well-maintained Kotlin port of JavaScript Redux. It has the
same API — store, reducer, `getState`, `dispatch` — which makes it instantly familiar if you've ever worked with
JS Redux. It supports middleware, which is genuinely great.

But the moment you need async work, you hit the wall. To do anything asynchronous, you need a middleware that lets
you dispatch *functions* instead of actions. A thunk looks like this:

```kotlin
store.dispatch { dispatch, getState ->
    val result = repository.loadItems()
    dispatch(CartAction.CartLoaded(result))
}
```

This works, but it has real costs. You're dispatching a lambda — an opaque, untestable value — rather than a typed,
inspectable action. There is no compile-time constraint on what the thunk is allowed to dispatch back. A `CartThunk`
can accidentally dispatch a `DeliveryAction` and the compiler says nothing. Asserting in tests what a thunk *did*
requires either mocking the store or capturing dispatched calls in a wrapper. There is no built-in effect system, no
lens-based reducer composition, and no pullback mechanism. Combining reducers follows the JS Redux model of
`combineReducers` — a flat, manual slice declaration with no optics.

### [Komposable Architecture](https://github.com/toggl/komposable-architecture) by Toggl

[Komposable Architecture](https://github.com/toggl/komposable-architecture) is the most direct [TCA](https://github.com/pointfreeco/swift-composable-architecture)
port for Android I found — Toggl built it explicitly as a Kotlin adaptation of Point-Free's work, and it shows.
Reducers are pure functions, effects are typed values that emit actions via `Flow`, and state composition is supported
through a pullback mechanism.

But look at what that pullback actually requires:

```kotlin
PullbackReducer(
    innerReducer      = cartReducer,
    mapToLocalState   = { globalState -> globalState.cart },
    mapToLocalAction  = { globalAction -> globalAction as? CartAction },
    mapToGlobalState  = { globalState, localState -> globalState.copy(cart = localState) },
    mapToGlobalAction = { localAction -> CheckoutAction.Cart(localAction) },
)
```

**Four mapping functions per feature.** For a checkout screen with three sub-features — cart, delivery, place order —
that is twelve mapping lambdas declared before you've written a single line of business logic. For a screen with five
sub-features, it's twenty.

There is also no traditional middleware — cross-cutting concerns like logging are handled through higher-order
reducers, which means wrapping reducers inside other reducers. Navigation support is described in the
[Komposable Architecture](https://github.com/toggl/komposable-architecture) README as an *"early exploration"* of how
the architecture could work with Navigation 3. The testing API requires a `testReduce` callback lambda rather than a
chainable assertion DSL. These are not signs of a poorly-made library —
[Komposable Architecture](https://github.com/toggl/komposable-architecture) is well-engineered and the closest thing
to [TCA](https://github.com/pointfreeco/swift-composable-architecture) on Android that existed — but the ergonomic
friction accumulates meaningfully at production scale.

---

## Introducing [Komposed](https://github.com/atwa/komposed)

[Komposed](https://github.com/atwa/komposed) is a lightweight Kotlin library for predictable, testable, composable
state management on Android. It adapts the fundamental building blocks of
[TCA](https://github.com/pointfreeco/swift-composable-architecture) — reducers, typed effects, lenses, and middleware
— to idiomatic Kotlin and Jetpack Compose.

The guiding principle was simple: **every architectural layer should have exactly one job, and doing that job should
require the minimum possible code.**

---

## Architecture at a Glance

> The diagram below shows how a single dispatched action travels through the entire pipeline — from the UI, through middleware, into the reducer, and back out through effects and subscriptions.

![Komposed Data Flow](https://raw.githubusercontent.com/Atwa/Komposed/master/docs/diagram-1-data-flow.svg)

---

## The Architecture: One Job Per Layer

### Reducer — State Management, Nothing Else

A `Reducer` in [Komposed](https://github.com/atwa/komposed) is a pure function. It receives the current state and an
action, and it returns a `ReduceType` — either a new state, a new state plus an effect to launch, or just an effect
with no state change.

```kotlin
val cartReducer = reducer<CartState, CartAction> { state, action ->
    when (action) {
        CartAction.LoadCart ->
            state.copy(isLoading = true).withEffect { CartEffect.Load }

        is CartAction.CartLoaded ->
            state.copy(items = action.items, isLoading = false).reduce()

        is CartAction.CartFailed ->
            state.copy(isLoading = false, error = action.message).reduce()
    }
}
```

No I/O. No coroutines. No injected dependencies. No knowledge of where data comes from. The reducer's entire job is
computing the next state — and expressing intent to do async work as a typed value, not as a lambda.

This is the key structural choice that separates [Komposed](https://github.com/atwa/komposed) from thunk-based
approaches. Effects are **values you can inspect, assert, and route** — not opaque functions you have to mock around.

### EffectHandler — Side Effects, Nothing Else

An `EffectHandler` performs the async work the reducer described. It receives an effect, does its work, and dispatches
zero, one, or many actions back into the store. It never touches state directly.

```kotlin
class CartEffectHandlerImpl @Inject constructor(
    private val repository: CartRepository,
) : EffectHandler<CartEffect, CartAction> {

    override suspend fun handle(
        effect: CartEffect,
        dispatch: suspend (suspend () -> CartAction) -> Unit,
    ) {
        when (effect) {
            CartEffect.Load -> dispatch {
                repository.getItems().fold(
                    onSuccess = { CartAction.CartLoaded(it) },
                    onFailure = { CartAction.CartFailed(it.message ?: "Unknown error") },
                )
            }
        }
    }
}
```

The `dispatch { }` lambda runs on `Dispatchers.IO`, and the resulting action is automatically posted back to the main
thread by the store. The type parameter `A` is enforced at compile time — a `CartEffectHandler` cannot accidentally
dispatch a `DeliveryAction`. This is a guarantee thunks simply cannot provide.

### Store — Orchestration, Nothing Else

The store routes every dispatched action through the middleware chain, invokes the matching reducer, updates state,
and launches any emitted effects on the IO dispatcher. Client code passes a plain `viewModelScope` — no dispatcher
wrangling, no thread pinning.

```kotlin
val store = createStore(
    initialValue = CheckoutState(),
    scope        = viewModelScope,
    middlewares  = listOf(loggingMiddleware(), navigationMiddleware(navigator)),
    reducers = reducers {
        cartReducer.scoped(CheckoutState.cartLens)
        deliveryReducer.scoped(CheckoutState.deliveryLens)
        placeOrderReducer.scoped(CheckoutState.placeOrderLens)
    },
    effectHandlers = effectHandlers {
        cartEffectHandler.register()
        deliveryEffectHandler.register()
        placeOrderEffectHandler.register()
    },
    subscriptions = subscriptions {
        subscription(
            selector = { it.deliveryState.selectedAddress?.deliveryFee ?: 0.0 },
            action   = { BillAction.DeliveryFeeUpdated(it) },
        )
    },
)
```

### Middleware — Cross-Cutting Concerns, Nothing Else

Middleware intercepts every dispatched action before it reaches a reducer. Logging, analytics, A/B flags, navigation
— none of it belongs in business logic, and none of it needs to.

```kotlin
fun <S> loggingMiddleware() = createMiddleware<S> { _, action, next ->
    Log.d("Komposed", "→ ${action::class.simpleName}")
    next(action)
}
```

The built-in `navigationMiddleware` intercepts `NavigationEffect` — a sealed marker the reducer emits just like any
other effect — and executes it against a `Navigator`. The `NavController` never appears in a ViewModel or reducer.
This is how navigation is correctly a side effect of state transitions, not a direct call.

This is one of the sharpest practical differences from
[Komposable Architecture](https://github.com/toggl/komposable-architecture): [Komposed](https://github.com/atwa/komposed)
has first-class middleware, so you never need to wrap reducers inside other reducers to add cross-cutting behaviour.

---

## Composability: Solving Real Production Complexity

This is where the architecture earns its name.

Real production screens are not simple. A checkout screen might combine cart state, delivery address selection,
billing summary, and order placement — four distinct domains, each with its own async operations, each potentially
reacting to changes in the others. The conventional approach ends up as one enormous ViewModel, with one enormous
state class, and one enormous `when`-block that knows far too much about everything.

[Komposed](https://github.com/atwa/komposed) addresses this with **lens-based state composition**. A
`Lens<GLOBAL, LOCAL>` is a getter/setter pair, declared once as a companion:

```kotlin
data class CheckoutState(
    val cart:       CartState       = CartState(),
    val delivery:   DeliveryState   = DeliveryState(),
    val placeOrder: PlaceOrderState = PlaceOrderState(),
) {
    companion object {
        val cartLens       = lens(CheckoutState::cart)       { copy(cart = it) }
        val deliveryLens   = lens(CheckoutState::delivery)   { copy(delivery = it) }
        val placeOrderLens = lens(CheckoutState::placeOrder) { copy(placeOrder = it) }
    }
}
```

Pulling a local reducer up to global state is then one line:

```kotlin
cartReducer.scoped(CheckoutState.cartLens)
```

Each sub-feature lives in its own Gradle module. `DeliveryReducer` has zero knowledge of `CartState`. The checkout
screen is their *composition*, not their owner. Adding a fourth feature — say, a promo code module — is three lines
in the store registration. Nothing else changes.

Compare this to [Komposable Architecture](https://github.com/toggl/komposable-architecture)'s four-mapping-function
pullback, or to [ReduxKotlin](https://reduxkotlin.org/)'s `combineReducers`, which works but offers no optics
abstraction — every new slice is a new manual mapping.

---

## Subscriptions: Reactive Cross-Domain Coordination

Composable features raise an interesting problem at production scale: how does one feature react to state changes in another, without importing it?

Consider a checkout screen. The bill summary needs to reflect the delivery fee — which lives in `DeliveryModule`. But `BillModule` is a standalone Gradle module with no dependency on `DeliveryModule`. Adding an import would collapse the boundary the entire architecture is designed to protect.

[Komposed](https://github.com/atwa/komposed) solves this with **subscriptions**: a lightweight mechanism that watches a derived value of the global state and automatically dispatches an action whenever that value changes. The wiring lives at the composition boundary — the `ViewModel` — where both modules are visible. Neither module needs to know about the other.

```kotlin
val store = createStore(
    ...
    subscriptions = subscriptions {
        subscription(
            selector = { it.deliveryState.selectedAddress?.deliveryFee ?: 0.0 },
            action   = { BillAction.DeliveryFeeUpdated(it) },
        )
    },
)
```

`BillModule` only needs one new action and one reducer case:

```kotlin
data class DeliveryFeeUpdated(val fee: Double) : BillAction

is BillAction.DeliveryFeeUpdated -> state.copy(deliveryFees = action.fee).reduce()
```

Subscriptions fire after every state update. If `selector(prevState) == selector(newState)` nothing is dispatched — there is no loop risk, because `DeliveryFeeUpdated` only mutates `billState.deliveryFees`, not the delivery address the selector watches. The dispatched action re-enters the full middleware chain, so it is logged, analytics-tracked, and fully observable.

**A note on when to use subscriptions.** Subscriptions solve the specific problem of values that need to cross a module boundary — where neither module can import the other. Values derived entirely within one module's own state don't need them. `BillState` owns `orderTotal`, `serviceFees`, and `deliveryFees`, so its grand total is simply a derived property:

```kotlin
data class BillState(
    val serviceFees: Double = 0.0,
    val orderTotal:  Double = 0.0,
    val deliveryFees: Double = 0.0,
    val isLoading:   Boolean = false,
) {
    val grandTotal: Double get() = orderTotal + serviceFees + deliveryFees
}
```

No action, no subscription, no extra dispatch. The module computes what it already knows; the subscription carries only what it cannot see.

The cross-domain wire-up is fully testable without any mocks or event buses:

```kotlin
@Test
fun `selecting a delivery address pushes its fee into BillState via subscription`() = runTest {
    val store = buildStore(this)
    store.dispatch(DeliveryAction.FetchDeliveryAddresses)
    advanceUntilIdle()
    store.dispatch(DeliveryAction.OnDeliveryAddressSelected(1L))
    assert(store.state.value.billState.deliveryFees == 10.0)
    assert(store.state.value.billState.grandTotal == 10.0)
}
```

One declaration at the composition boundary. Both modules stay clean.

---

## Unidirectional Data Flow: Predictable by Construction

Every state change in [Komposed](https://github.com/atwa/komposed) follows one path:

```
Action → Middleware → Reducer → State + Effect → EffectHandler → Action → …
```

There is no way to mutate state from a coroutine, a callback, or a side channel. State is always a deterministic
function of the actions that preceded it. This means any bug can be reproduced by replaying the action sequence, and
any behaviour can be understood by reading a pure function — the reducer — with no hidden inputs.

It also means the UDF contract is impossible to accidentally violate. The framework enforces it at the type level.

---

## Testability: Assert What Your App Actually Does

This is where I wanted [Komposed](https://github.com/atwa/komposed) to shine most.

### Reducer Tests — No Coroutines, No Mocks

```kotlin
@Test
fun `LoadCart sets isLoading and emits Load effect`() {
    cartReducer.given(CartState(), CartAction.LoadCart)
        .assertState(CartState(isLoading = true))
        .assertEffect<CartEffect.Load>()
}

@Test
fun `CartFailed stores the error message`() {
    cartReducer
        .given(CartState(isLoading = true), CartAction.CartFailed("Timeout"))
        .assertState(CartState(isLoading = false, error = "Timeout"))
        .assertNoEffect()
}
```

Pure functions need no coroutines, no `TestScope`, no fakes. The test reads exactly like a specification.

### Effect Handler Tests — Dispatched Actions as a List

The `komposed-testing` module provides a `handle(effect)` extension that collects every dispatched action and returns
them as `List<A>`:

```kotlin
@Test
fun `Load dispatches CartLoaded on success`() = runTest {
    val result = handlerWith(successRepo).handle(CartEffect.Load)
    assert(result.single() == CartAction.CartLoaded(items))
}

@Test
fun `Load dispatches CartFailed with message on failure`() = runTest {
    val result = handlerWith(failureRepo).handle(CartEffect.Load)
    assert(result.single() == CartAction.CartFailed("Network error"))
}
```

No capture variables. No lambdas passed in. No mock dispatch function to wire up. The API surface is deliberately
minimal.

### Integration Tests — Assert the Full Action Sequence

`TestStore` records every dispatched action in order — including actions produced by effect handlers — so you can
assert not just final state but the exact flow your app took to get there:

```kotlin
@Test
fun `LoadCart effect round-trip updates state`() = runTest {
    val store = buildStore(this)
    store.dispatch(CartAction.LoadCart)
    advanceUntilIdle()
    store.assertState(CheckoutState(cart = CartState(items = listOf(testItem))))
    store.assertActionsDispatched(CartAction.LoadCart, CartAction.CartLoaded(testItem))
}
```

### Navigation Tests — Effects, Not Mocks

Navigation in [Komposed](https://github.com/atwa/komposed) is a typed effect. That means it's assertable the same
way any other effect is:

```kotlin
@Test
fun `OrderPlaced navigates to OrderDetails with the correct ID`() {
    placeOrderReducer
        .given(PlaceOrderState(), PlaceOrderAction.OrderPlaced(orderId = "ORD-99"))
        .assertNavigationEffect { nav ->
            assert(nav.navigations.last() == OrderDetailsRoute(orderId = "ORD-99"))
        }
}
```

`TestNavigator` records every `navigate()`, `navigateUp()`, and `popBackStack()` call. No Robolectric. No
instrumented test. No mock `NavController`.

---

## Side-by-Side Comparison

| Feature | [Komposed](https://github.com/atwa/komposed) | [ReduxKotlin](https://reduxkotlin.org/) | [Komposable Architecture](https://github.com/toggl/komposable-architecture) |
|:---|:---:|:---:|:---:|
| Pure reducers | ✅ | ✅ | ✅ |
| Typed effects | ✅ Sealed values | ❌ Thunk lambdas | ✅ Flow-based |
| Compile-time dispatch safety | ✅ | ❌ Untyped | ✅ |
| Middleware | ✅ Built-in | ✅ Built-in | ❌ Higher-order reducers |
| Lens / pullback | ✅ **1 line** | ❌ Manual `combineReducers` | ⚠️ **4 mapping functions** |
| Navigation as an effect | ✅ Built-in | ❌ Manual | ⚠️ Exploratory |
| Reducer testing DSL | ✅ Chainable assertions | ❌ | ⚠️ `testReduce` callback |
| Effect handler testing | ✅ `List<A>` extension | ❌ | ❌ |
| Action sequence assertions | ✅ `TestStore` | ❌ | ❌ |
| Subscriptions / cross-domain reactive state | ✅ Built-in | ❌ | ❌ |
| Ceremony to add a feature | 🟢 Low | 🟡 Medium | 🔴 Medium–High |

[ReduxKotlin](https://reduxkotlin.org/) is a solid Redux port, but the thunk middleware loses type safety at exactly
the boundary where it matters most — the async/dispatch boundary.
[Komposable Architecture](https://github.com/toggl/komposable-architecture) is the closest prior work to what I set
out to build, and it deserves recognition for that. But four mapping functions per pullback and no middleware mean
that the ergonomic cost grows with every domain you add to a screen.

---

## A Library for the Team That Wants to Ship

My iOS friend eventually built his Android project. He used [Komposed](https://github.com/atwa/komposed). His
feedback after a few weeks was the same as mine:

> *"It just gets out of the way."*

You define your state, write a pure reducer, implement an effect handler, and wire them together in a store. The
framework handles threading, effect routing, action type-safety, and testing infrastructure. You handle your app.

The architecture is explicitly inspired by [TCA](https://github.com/pointfreeco/swift-composable-architecture) — the
same ideas of pure reducers, typed effects, composable lenses, and first-class testability that iOS engineers have
come to rely on. [Komposed](https://github.com/atwa/komposed) is the answer to the question my friend asked that
Tuesday evening.

---

## Get Started

```kotlin
implementation("io.github.atwa:komposed:1.1.0")
testImplementation("io.github.atwa:komposed-testing:1.1.0")
```

Source, documentation, and the full app checkout app:
**[github.com/atwa/komposed](https://github.com/atwa/komposed)**

---

*Inspired by [The Composable Architecture](https://github.com/pointfreeco/swift-composable-architecture) by Point-Free.*

**Tags:** `Android` · `Kotlin` · `Architecture` · `JetpackCompose` · `StateManagement` · `TCA` · `UDF` · `MVI`
