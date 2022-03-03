package com.rsoumail.mymemories.view.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.rsoumail.mymemories.view.viewmodels.BaseViewModel

abstract class BaseActivity<V: BaseViewModel> : AppCompatActivity() {

    lateinit var viewModel: V

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
    }

    open fun initObservers() {

    }

    open fun initViews() {

    }
}