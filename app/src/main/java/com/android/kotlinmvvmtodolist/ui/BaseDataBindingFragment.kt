package com.android.kotlinmvvmtodolist.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseDataBindingFragment<ViewBinding : ViewDataBinding> : Fragment() {

    var dataBinding: ViewBinding? = null

    @LayoutRes
    abstract fun getLayoutRes(): Int

    override fun onAttach(context: Context) {
        super.onAttach(context)
//        println("🥰 ${this.javaClass.simpleName} #${this.hashCode()}   onAttach()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("😀 ${this.javaClass.simpleName} #${this.hashCode()}  onCreate()")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        println("🤣 ${this.javaClass.simpleName} #${this.hashCode()} onCreateView()")
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false)
        dataBinding!!.lifecycleOwner = viewLifecycleOwner

        return dataBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        println("🤩 ${this.javaClass.simpleName} #${this.hashCode()}  onViewCreated() view: $view")
    }


    override fun onDestroyView() {
        super.onDestroyView()
        println("🥵 ${this.javaClass.simpleName} #${this.hashCode()}  onDestroyView()")

        /*
            🔥 Without nullifying dataBinding ViewPager2 gets data binding related MEMORY LEAKS
         */
        dataBinding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        println("🥶 ${this.javaClass.simpleName} #${this.hashCode()}  onDestroy()")
    }

    override fun onDetach() {
        super.onDetach()
//        println("💀 BaseDataBindingFragment onDetach() $this")
    }

    override fun onResume() {
        super.onResume()
//        println("🎃 ${this.javaClass.simpleName} #${this.hashCode()} onResume()")
    }

    override fun onPause() {
        super.onPause()
//        println("😱 ${this.javaClass.simpleName} #${this.hashCode()} onPause()")
    }

}