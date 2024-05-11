package com.deveem.dmarket.core.extention

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.deveem.dmarket.R

fun Fragment.activityNavController() = requireActivity().findNavController(R.id.navigation_host_fragment)

fun Fragment.flowNavController(@IdRes navHostFragmentId: Int) =
  requireActivity().findNavController(navHostFragmentId)

fun NavController.directionsSafeNavigation(directions: NavDirections) {
  currentDestination?.getAction(directions.actionId)?.let { navigate(directions) }
}