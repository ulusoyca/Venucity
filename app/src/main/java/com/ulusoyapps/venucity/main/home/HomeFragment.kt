/*
 *  Copyright 2020 Cagatay Ulusoy (Ulus Oy Apps). All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.ulusoyapps.venucity.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.ulusoyapps.venucity.R
import com.ulusoyapps.venucity.databinding.FragmentHomeBinding
import com.ulusoyapps.venucity.domain.entities.HttpError
import com.ulusoyapps.venucity.domain.entities.LocationMessage
import com.ulusoyapps.venucity.domain.entities.NetworkError
import com.ulusoyapps.venucity.domain.entities.VenueAddFailure
import com.ulusoyapps.venucity.domain.entities.VenueMessage
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
                        is VenuesUiState.Success -> handleSuccess(controller, state)
                        is VenuesUiState.Error -> handleError(state)
                        VenuesUiState.Loading -> handleLoading()
                    }
                }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStartFetchingVenues(3000, 15)
    }

    override fun onStop() {
        viewModel.onStop()
        super.onStop()
    }

    private fun handleLoading() {
        binding.loadingAnimation.visibility = View.VISIBLE
        binding.failGroup.visibility = View.GONE
        binding.successGroup.visibility = View.GONE
    }

    private fun handleSuccess(
        controller: HomeEpoxyController,
        state: VenuesUiState.Success
    ) {
        binding.loadingAnimation.visibility = View.GONE
        binding.failGroup.visibility = View.GONE
        binding.successGroup.visibility = View.VISIBLE
        controller.updateData(state.venues)
    }

    private fun handleError(state: VenuesUiState.Error) {
        binding.loadingAnimation.visibility = View.GONE
        val messageResId: Int = when (state.message) {
            is NetworkError -> R.string.network_error
            is HttpError -> R.string.http_error
            is VenueMessage -> R.string.venue_message
            is LocationMessage -> R.string.location_message
        }
        if (state.message is VenueAddFailure) {
            Snackbar.make(binding.root, getString(messageResId), Snackbar.LENGTH_LONG).show()
        } else {
            binding.errorTextDesc.text = getText(messageResId)
            binding.loadingAnimation.visibility = View.GONE
            binding.failGroup.visibility = View.VISIBLE
            binding.successGroup.visibility = View.GONE
        }
    }
}
