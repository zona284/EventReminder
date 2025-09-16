package app.rakha.eventreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import app.rakha.eventreminder.routes.AppRoutes
import app.rakha.eventreminder.ui.theme.EventReminderTheme
import app.rakha.reminder.ui.form.ReminderFormScreen
import app.rakha.reminder.ui.list.ReminderListScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EventReminderTheme {
                ReminderAppScreen()
            }
        }
    }
}

@Composable
fun ReminderAppScreen(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = AppRoutes.REMINDER_LIST,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(400)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(400)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(300)) }
    ) {
        composable(AppRoutes.REMINDER_LIST) {
            ReminderListScreen(
                onAddNewReminder = {
                    navController.navigate(AppRoutes.REMINDER_FORM)
                }
            )
        }

        composable(AppRoutes.REMINDER_FORM) {
            ReminderFormScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}