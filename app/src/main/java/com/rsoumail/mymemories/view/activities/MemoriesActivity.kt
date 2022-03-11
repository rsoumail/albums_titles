package com.rsoumail.mymemories.view.activities

import android.view.LayoutInflater
import com.rsoumail.mymemories.R
import com.rsoumail.mymemories.databinding.ActivityMemoriesBinding
import com.rsoumail.mymemories.view.fragments.MemoriesFragment
import com.rsoumail.mymemories.view.viewmodels.MemoriesActivityViewModel
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MemoriesActivity : FragmentWrapperActivity<MemoriesActivityViewModel, ActivityMemoriesBinding>() {


    override fun initViews() {
        viewModel = getViewModel()
        setFragment(MemoriesFragment())
    }

    override fun initObservers() {

    }

    override fun setupViewBinding(layoutInflater: LayoutInflater): ActivityMemoriesBinding {
        return ActivityMemoriesBinding.inflate(layoutInflater)
    }
}