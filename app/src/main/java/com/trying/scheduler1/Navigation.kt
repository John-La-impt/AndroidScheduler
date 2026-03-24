package com.trying.scheduler1

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

//class MyAnyFrameType : NavType<AnyFrame>(
//    isNullableAllowed = false
//) {
//    override fun put(
//        bundle: SavedState,
//        key: String,
//        value: AnyFrame
//    ) {
//        TODO("Not yet implemented")
//    }
//
//    override fun get(
//        bundle: SavedState,
//        key: String
//    ): AnyFrame? {
//        TODO("Not yet implemented")
//    }
//
//    override fun parseValue(value: String): AnyFrame {
//        TODO("Not yet implemented")
//    }
//}

@Composable
fun Navigation(context: Context) {

    // all screens
    val startScreen = StartActivity()
    val addTaskScreen = AddTaskActivity()

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.startScreen.route) {
        composable(route = Screen.startScreen.route) {
            startScreen.Starter(name = "Android", navController, context, modifier = Modifier)
        }
        composable(route = Screen.addTaskScreen.route) {
            addTaskScreen.TaskAdder(context, navController, modifier = Modifier)
        }
//            {
////            addTaskScreen.TaskAdder(context, Modifier)
//        }
    }
}

