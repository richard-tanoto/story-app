package com.richard.storyapp.feature.maps

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.richard.storyapp.R
import com.richard.storyapp.core.data.local.room.Story
import com.richard.storyapp.core.data.remote.response.ApiResult
import com.richard.storyapp.core.ui.BaseFragment
import com.richard.storyapp.core.ui.InfoWindowAdapter
import com.richard.storyapp.databinding.FragmentMapsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : BaseFragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentMapsBinding
    private lateinit var map: GoogleMap

    private val viewModel: MapsViewModel by viewModels()

    private var firstLaunch = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupMap()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        try {
            map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
        } catch (exception: Resources.NotFoundException) {
            map.mapType = GoogleMap.MAP_TYPE_NORMAL
        }
        setupObserver()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            title = getString(R.string.maps)
            navigationIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_back)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setupMap() {
        val mapFragment = binding.mapLayout.getFragment() as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setupObserver() {
        viewModel.stories.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResult.Success -> {
                    showLoading(false)
                    showMarkers(response.data.listStory)
                }

                is ApiResult.Loading -> {
                    showLoading(true)
                }

                is ApiResult.Error -> {
                    showLoading(false)
                    showToast(response.message)
                }
            }
        }
    }

    private fun showMarkers(stories: List<Story>) {
        stories.forEachIndexed { index, story ->
            if (story.lat != null && story.lon != null) {
                val area = LatLng(story.lat, story.lon)
                map.addMarker(
                    MarkerOptions()
                        .position(area)
                        .title("${story.id} ${story.name} ${story.description}")
                        .snippet(story.photoUrl)
                )
                if (index == 0 && firstLaunch) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(area, 5f))
                    firstLaunch = false
                }
                map.setOnCameraMoveStartedListener {
                    binding.toolbar.alpha = 1f
                    binding.toolbar.visibility = VISIBLE
                    binding.toolbar.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                binding.toolbar.visibility = INVISIBLE
                            }
                        })
                }
                map.setOnCameraIdleListener {
                    binding.toolbar.alpha = 0f
                    binding.toolbar.visibility = VISIBLE
                    binding.toolbar.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .setListener(object : AnimatorListenerAdapter() {
                            override fun onAnimationEnd(animation: Animator) {
                                binding.toolbar.visibility = VISIBLE
                            }
                        })
                }
            }
        }
        map.setInfoWindowAdapter(InfoWindowAdapter(binding.root))
        map.setOnInfoWindowClickListener { marker ->
            marker.title?.split(' ')?.getOrNull(0)?.let {
                findNavController().navigate(
                    MapsFragmentDirections.actionMapsFragmentToDetailFragment(
                        it
                    )
                )
            }
        }
    }
}