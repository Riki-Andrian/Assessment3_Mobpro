package com.android.kotlinmvvmtodolist.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.android.kotlinmvvmtodolist.R
import com.android.kotlinmvvmtodolist.adapter.ChildFragmentStateAdapter
import com.android.kotlinmvvmtodolist.databinding.FragmentViewpagerContainerBinding
import com.android.kotlinmvvmtodolist.ui.viewmodel.AppbarViewModel
import com.google.android.material.tabs.TabLayoutMediator


class ViewpagerContainer : BaseDataBindingFragment<FragmentViewpagerContainerBinding>() {
    override fun getLayoutRes(): Int = R.layout.fragment_viewpager_container

    val appbarViewModel by activityViewModels<AppbarViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewPager2 = dataBinding!!.viewPager
        viewPager2.adapter =
            ChildFragmentStateAdapter(childFragmentManager, viewLifecycleOwner.lifecycle)
        val tabLayout = dataBinding!!.tabLayout
        TabLayoutMediator(tabLayout, viewPager2){
                tab, position ->
            when(position){
                0 -> tab.text="Taks"
                1 -> tab.text="Cuaca"
            }
        }.attach()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                appbarViewModel.currentNavController.value =
                    appbarViewModel.currentNavController.value
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_viewpager_container, container, false)
    }


}