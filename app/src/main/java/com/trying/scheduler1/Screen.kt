package com.trying.scheduler1

sealed class Screen(val route: String) {
    object startScreen: Screen("startScreen")
    object addTaskScreen: Screen("addTaskScreen")
}