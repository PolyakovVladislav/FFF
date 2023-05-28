package com.fairyfo.frenzy.ui.extensions

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.Navigator

internal fun NavController.navigateSafe(
    @IdRes resId: Int,
    args: Bundle? = null,
    navOptions: NavOptions? = null,
    navExtras: Navigator.Extras? = null
): Boolean {
    val action = currentDestination?.getAction(resId) ?: graph.getAction(resId)
    return try {
        if ((action != null && currentDestination?.id != action.destinationId) ||
            (action == null && currentDestination?.id != resId)
        ) {
            navigate(resId, args, navOptions, navExtras)
            true
        } else {
            Log.e("Debug", "Action is null or destination screen is set already")
            false
        }
    } catch (ex: IllegalArgumentException) {
        Log.e("Debug", ex.message.toString())

        // Navigation action/destination cannot be found from the current destination Destination
        false
    }
}

internal fun NavController.navigateSafe(directions: NavDirections) {
    navigateSafe(directions.actionId, directions.arguments)
}

