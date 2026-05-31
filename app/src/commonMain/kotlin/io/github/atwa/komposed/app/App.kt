package io.github.atwa.komposed.app

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import io.github.atwa.komposed.app.core.navigation.CheckoutRoute
import io.github.atwa.komposed.app.core.navigation.NavigatorImpl
import io.github.atwa.komposed.app.navigation.appScreens
import io.github.atwa.komposed.app.ui.theme.CellularTheme
import org.koin.compose.koinInject

private const val TRANSITION_DURATION = 150

@Composable
fun App() {
    val navigator = koinInject<NavigatorImpl>()
    val navController = rememberNavController()
    SideEffect { navigator.bind(navController) }
    CellularTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            NavHost(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
                navController = navController,
                startDestination = CheckoutRoute,
                enterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        tween(TRANSITION_DURATION),
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.Start,
                        tween(TRANSITION_DURATION),
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        tween(TRANSITION_DURATION),
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        AnimatedContentTransitionScope.SlideDirection.End,
                        tween(TRANSITION_DURATION),
                    )
                },
            ) {
                appScreens()
            }
        }
    }
}
