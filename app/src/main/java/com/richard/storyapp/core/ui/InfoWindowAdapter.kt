package com.richard.storyapp.core.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.richard.storyapp.R
import com.richard.storyapp.databinding.LayoutInfoWindowBinding

class InfoWindowAdapter(
    private val parent: ViewGroup,
) : GoogleMap.InfoWindowAdapter {

    private val binding =
        LayoutInfoWindowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

    override fun getInfoContents(marker: Marker): View {
        setData(marker)
        return binding.root
    }

    override fun getInfoWindow(marker: Marker): View? = null

    private fun setData(marker: Marker) {
        val list = marker.title?.split(' ')
        binding.apply {
            tvName.text = list?.getOrNull(1)
            tvDescription.text = list?.getOrNull(2)
            Glide.with(parent.context)
                .load(marker.snippet)
                .error(R.drawable.image_error)
                .placeholder(R.color.md_theme_light_outline)
                .into(imgStory)
        }
    }

}