package com.richard.storyapp.core.util

import android.Manifest

enum class StoryPermission(val value: String) {
    // Camera and Media
    CAMERA(Manifest.permission.CAMERA),

    // Location
    LOCATION_PRECISE(Manifest.permission.ACCESS_FINE_LOCATION),
    LOCATION_APPROXIMATE(Manifest.permission.ACCESS_COARSE_LOCATION)
}