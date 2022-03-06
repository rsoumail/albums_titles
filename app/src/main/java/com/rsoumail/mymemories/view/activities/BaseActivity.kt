package com.rsoumail.mymemories.view.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.rsoumail.mymemories.view.viewmodels.BaseViewModel

abstract class BaseActivity<VM : BaseViewModel, ViewBindingType : ViewBinding> : AppCompatActivity() {

    lateinit var viewModel: VM
    private var _binding: ViewBindingType? = null
    // Binding variable to be used for accessing views.
    protected val binding
        get() = requireNotNull(_binding)

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        onCreate(savedInstanceState)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = setupViewBinding(layoutInflater)
        setContentView(requireNotNull(_binding).root)
        initViews()
        viewModel.viewReady()
        initObservers()
    }

    abstract fun setupViewBinding(layoutInflater: LayoutInflater): ViewBindingType?

    abstract fun initObservers()

    abstract fun initViews()

    fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}