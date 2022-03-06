package com.rsoumail.mymemories.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.rsoumail.mymemories.databinding.FragmentMemoriesBinding
import com.rsoumail.mymemories.view.adapters.MemoriesAdapter
import com.rsoumail.mymemories.view.adapters.OnMemoryClickListener
import com.rsoumail.mymemories.view.viewmodels.MemoriesFragmentViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.getViewModel

class MemoriesFragment : BaseFragment<MemoriesFragmentViewModel>() {

    private var _binding: FragmentMemoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: MemoriesAdapter
    private var isDataAlreadyObserved = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMemoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun initViews() {
        viewModel = getViewModel()
        binding.shimmerLayout.startShimmer()
        adapter = MemoriesAdapter(OnMemoryClickListener {  })
        binding.memoriesRecyclerView.adapter = adapter
        if (viewModel.isUnavailableNetwork()) {
            stopShimmer()
            showUnavailableNetwork()
            observeData(false)
        } else {
            observeData(true)
        }
    }

    override fun initObservers() {
        lifecycleScope.launchWhenCreated {
            viewModel.showUnavailableNetwork.collect { show ->
                when {
                    show -> {
                        stopShimmer()
                        showUnavailableNetwork()
                    }
                    else -> {
                        binding.shimmerLayout.startShimmer()
                        binding.unavailableNetworkContainer.visibility = View.GONE
                        if (!isDataAlreadyObserved) {
                            observeData(true)
                        }
                    }
                }
            }
        }

    }

    private fun stopShimmer() {
        binding.shimmerLayout.stopShimmer()
        binding.shimmerLayout.visibility = View.GONE
    }

    private fun showUnavailableNetwork() {
        binding.unavailableNetworkContainer.visibility = View.VISIBLE
    }

    private fun observeData(canObserve: Boolean) {
        if (canObserve) {
            lifecycleScope.launch {
                viewModel.fetchMemories().collect {
                    viewModel.updateFirstLaunch()
                    stopShimmer()
                    adapter.submitData(it)
                }
            }
            isDataAlreadyObserved = true
        }
    }
}