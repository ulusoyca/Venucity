package com.ulusoyapps.venucity.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ulusoyapps.venucity.databinding.FragmentHomeBinding
import com.ulusoyapps.venucity.domain.entities.SuccessfulVenueOperation
import com.ulusoyapps.venucity.main.home.epoxy.HomeEpoxyController
import dagger.android.support.DaggerFragment
import timber.log.Timber
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
            nearbyVenues.observe(
                viewLifecycleOwner,
                Observer { venues ->
                    Timber.d("Updating venues...")
                    controller.updateData(venues)
                }
            )

            venueOperationResultListener.observe(
                viewLifecycleOwner,
                Observer { message ->
                    if (message is SuccessfulVenueOperation) {
                        Timber.d("Successful venue operation")
                    }
                }
            )
            onStartFetchingVenues(10000, 15)
        }
    }
}
