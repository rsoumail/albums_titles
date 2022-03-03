package com.rsoumail.mymemories.view.fragments

import androidx.fragment.app.Fragment
import com.rsoumail.mymemories.view.viewmodels.BaseViewModel

abstract class BaseFragment<V: BaseViewModel>: Fragment() {

    protected lateinit var viewModel: V
}