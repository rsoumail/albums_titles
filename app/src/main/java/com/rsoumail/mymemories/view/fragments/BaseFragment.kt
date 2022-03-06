package com.rsoumail.mymemories.view.fragments

import androidx.fragment.app.Fragment
import com.rsoumail.mymemories.view.viewmodels.BaseViewModel

abstract class BaseFragment<VM: BaseViewModel>: Fragment() {

    lateinit var viewModel: VM

    abstract fun initViews()

    abstract fun initObservers()

    override fun onStart() {
        super.onStart()
        initViews()
        viewModel.viewReady()
        initObservers()
    }
}