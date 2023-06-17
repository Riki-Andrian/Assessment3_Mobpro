package com.android.kotlinmvvmtodolist.navhost

import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.android.kotlinmvvmtodolist.R
import com.android.kotlinmvvmtodolist.databinding.FragmentNavhostParentBinding
import com.android.kotlinmvvmtodolist.ui.BaseDataBindingFragment
import com.android.kotlinmvvmtodolist.ui.viewmodel.AppbarViewModel
import androidx.lifecycle.Observer

class ParentNavHostFragment : BaseDataBindingFragment<FragmentNavhostParentBinding>(){
    override fun getLayoutRes(): Int = R.layout.fragment_navhost_parent

    private var navController: NavController? = null
    private val nestedNavHostFragmentId = R.id.nestedParentNavHostFragment
    private val appbarViewModel by activityViewModels<AppbarViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nestedNavHostFragment =
            childFragmentManager.findFragmentById(nestedNavHostFragmentId) as? NavHostFragment
        navController = nestedNavHostFragment?.navController

        val appBarConfig = AppBarConfiguration(navController!!.graph)
        dataBinding!!.toolbar.setupWithNavController(navController!!, appBarConfig)

        appbarViewModel.currentNavController.observe(viewLifecycleOwner, Observer { navController ->
            navController?.let {
                val  appBarConfig = AppBarConfiguration(it.graph)
                dataBinding!!.toolbar.setupWithNavController(it, appBarConfig)
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

}