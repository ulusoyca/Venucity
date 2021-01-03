package com.ulusoyapps.venucity.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ulusoyapps.venucity.databinding.FragmentHomeBinding
import com.ulusoyapps.venucity.main.home.epoxy.HomeEpoxyController
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class HomeFragment : DaggerFragment() {

    private lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: HomeViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.layoutManager =
            LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false
            )
        val controller = HomeEpoxyController(viewModel)
        binding.recyclerView.setController(controller)

        with(viewModel) {
            uiState.observe(
                viewLifecycleOwner,
                { state ->
                    when (state) {
                        is VenuesUiState.Success -> {
                            binding.loadingAnimation.visibility = View.GONE
                            controller.updateData(state.venues)
                        }
                        is VenuesUiState.Error -> {
                            binding.loadingAnimation.visibility = View.GONE
                        }
                        VenuesUiState.Loading -> {
                            binding.loadingAnimation.visibility = View.VISIBLE
                        }
                    }
                }
            )
            onStartFetchingVenues(10000, 15)
        }
    }
}
