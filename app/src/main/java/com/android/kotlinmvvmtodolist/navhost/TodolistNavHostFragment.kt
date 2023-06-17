package com.android.kotlinmvvmtodolist.navhost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.android.kotlinmvvmtodolist.R
import com.android.kotlinmvvmtodolist.databinding.FragmentNavhostTodoBinding
import com.android.kotlinmvvmtodolist.ui.BaseDataBindingFragment
import com.android.kotlinmvvmtodolist.ui.viewmodel.AppbarViewModel

class TodolistNavHostFragment: BaseDataBindingFragment<FragmentNavhostTodoBinding>(){

    override fun getLayoutRes(): Int = R.layout.fragment_navhost_todo
    private val appbarViewModel by activityViewModels<AppbarViewModel>()

    private var navController : NavController? =null

    private val nestedNavHostFragmentId = R.id.nestedTodoNavHostFragment

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nestedNavHostFragment =
            childFragmentManager.findFragmentById(nestedNavHostFragmentId) as? NavHostFragment
        navController = nestedNavHostFragment?.navController
    }

    override fun onResume() {
        super.onResume()
        appbarViewModel.currentNavController.value = navController
    }

    override fun onPause() {
        super.onPause()
    }
}
