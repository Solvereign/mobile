package com.example.learnlanguage.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.learnlanguage.ui.word.EditDestination
import com.example.learnlanguage.ui.word.EntryScreen
import com.example.learnlanguage.ui.word.EntryScreenDestination
import com.example.learnlanguage.ui.word.HomeDestination
import com.example.learnlanguage.ui.word.ListScreen
import com.example.learnlanguage.ui.word.ModeDestination
import com.example.learnlanguage.ui.word.ModeScreen
import com.example.learnlanguage.ui.word.WordEditScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = HomeDestination.route,
        modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            ListScreen(
                navigateToWordEntry = { navController.navigate(EntryScreenDestination.route) },
                navigateToWordUpdate = {id ->
                    navController.navigate("${EditDestination.route}/${id}"
                ) },
                navigateToMode = { navController.navigate(ModeDestination.route) })
        }
        composable(route = EntryScreenDestination.route) {
            EntryScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(
//            route = "${EditDestination.route}/{${EditDestination.idArg}}",
            route = EditDestination.routeWithArg,
            arguments = listOf(navArgument(EditDestination.idArg) {
                type = NavType.IntType
            })
            ) {
            WordEditScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
        composable(route = ModeDestination.route) {
            ModeScreen(
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }
    }
}