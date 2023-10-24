package com.richard.storyapp.feature.add

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.ImageDecoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.net.toUri
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.richard.storyapp.BuildConfig
import com.richard.storyapp.R
import com.richard.storyapp.core.data.remote.response.ApiResult
import com.richard.storyapp.core.ui.BaseFragment
import com.richard.storyapp.core.util.CameraUtil.CAMERA_BUNDLE_KEY
import com.richard.storyapp.core.util.CameraUtil.CAMERA_REQUEST_KEY
import com.richard.storyapp.core.util.ImageUtil.uriToFile
import com.richard.storyapp.core.util.InputCheckResult
import com.richard.storyapp.core.util.StoryPermission.CAMERA
import com.richard.storyapp.core.util.StoryPermission
import com.richard.storyapp.databinding.DialogSelectImageBinding
import com.richard.storyapp.databinding.FragmentAddStoryBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@AndroidEntryPoint
class AddStoryFragment : BaseFragment() {

    private lateinit var binding: FragmentAddStoryBinding
    private val viewModel: AddStoryViewModel by viewModels()

    @Inject
    lateinit var fusedLocationClient: FusedLocationProviderClient

    private var count = 0

    private var location: Location? = null

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let {
            viewModel.setUri(it)
        } ?: run {
            showToast("No image selected")
        }
    }

    private val cameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isPermitted ->
        if (isPermitted)
            navigateToCamera()
        else
            showToast("Please allow the camera permission to use this feature")
    }

    private val locationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            when {
                isGranted -> getLocation()
                count == 2 -> {
                    showToast("Please grant the location permission via device settings")
                    binding.btnLocation.isChecked = false
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts(
                            "package",
                            BuildConfig.APPLICATION_ID,
                            null
                        )
                    ).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        startActivity(this)
                    }
                }
                else -> {
                    showToast("Please allow the location permission to use this feature")
                    binding.btnLocation.isChecked = false
                    count += 1
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setEventClickListener()
        setupObserver()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            title = getString(R.string.add_story)
            navigationIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_back)
            setNavigationOnClickListener { findNavController().popBackStack() }
            inflateMenu(R.menu.add_story_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.destSend -> {
                        val checkResult = validImageAndDescription()
                        if (checkResult.isValid) uploadStory() else showToast(checkResult.message)
                    }
                }
                true
            }
        }
    }

    private fun validImageAndDescription(): InputCheckResult {
        return when {
            viewModel.uri.value == null -> InputCheckResult(
                isValid = false,
                message = "Please insert an image"
            )

            binding.tfDescription.text.toString().isEmpty() -> InputCheckResult(
                isValid = false,
                message = "Please insert the description"
            )

            else -> InputCheckResult(isValid = true)
        }
    }

    private fun uploadStory() {
        viewModel.uri.value?.let { uri ->
            showLoading(true) { viewModel.cancelRequest() }
            lifecycleScope.launch {
                val imageFile = uriToFile(uri, requireContext())
                val description =
                    binding.tfDescription.text.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
                val image = MultipartBody.Part.createFormData(
                    "photo",
                    imageFile.name,
                    requestImageFile
                )
                if (binding.btnLocation.isChecked) {
                    location?.let {
                        val lat = it.latitude.toString().toRequestBody("text/plain".toMediaType())
                        val lon = it.longitude.toString().toRequestBody("text/plain".toMediaType())
                        viewModel.uploadStory(
                            image = image,
                            description = description,
                            lat = lat,
                            lon = lon,
                        )
                    } ?: run {
                        showToast("Cannot obtain your location")
                    }
                } else {
                    viewModel.uploadStory(
                        image = image,
                        description = description
                    )
                }
            }
        }
    }

    private fun setEventClickListener() {
        binding.btnAddImage.root.setOnClickListener {
            showDialog(false)
        }
        binding.imgStory.setOnClickListener {
            showDialog(true)
        }
        binding.btnLocation.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) getLocation()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (permissionGranted(StoryPermission.LOCATION_PRECISE) && permissionGranted(StoryPermission.LOCATION_APPROXIMATE)) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    this.location = location
                } ?: run {
                    binding.btnLocation.isChecked = false
                }
            }
        } else {
            locationPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun showDialog(hasImage: Boolean) {
        val dialog = BottomSheetDialog(requireContext())
        val dialogBinding = DialogSelectImageBinding.inflate(layoutInflater)
        dialog.setContentView(dialogBinding.root)
        dialog.setCanceledOnTouchOutside(true)
        dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
        dialog.behavior.maxWidth = ViewGroup.LayoutParams.MATCH_PARENT
        dialogBinding.apply {
            btnDelete.visibility = if (hasImage) VISIBLE else GONE
            divider.visibility = if (hasImage) VISIBLE else GONE
            btnCamera.setOnClickListener {
                if (permissionGranted(CAMERA)) {
                    navigateToCamera()
                } else {
                    cameraPermissionLauncher.launch(CAMERA.value)
                }
                dialog.dismiss()
            }
            btnGallery.setOnClickListener {
                navigateToGallery()
                dialog.dismiss()
            }
            btnDelete.setOnClickListener {
                showImage(false)
                viewModel.clearUri()
                dialog.dismiss()
            }
        }
        dialog.show()
    }


    private fun setupObserver() {
        viewModel.uri.observe(viewLifecycleOwner) { uri ->
            uri?.let {
                showImage(true)
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.decodeBitmap(
                        ImageDecoder.createSource(
                            requireContext().contentResolver,
                            it
                        )
                    )
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
                }
                binding.imgStory.setImageBitmap(bitmap)
            }
        }
        viewModel.addStoryResponse.observe(viewLifecycleOwner) { response ->
            when (response) {
                is ApiResult.Success -> {
                    showLoading(false)
                    showToast("Add story success")
                    findNavController().navigate(AddStoryFragmentDirections.actionAddStoryFragmentToHomeFragment())
                }

                is ApiResult.Loading -> {
                    showLoading(true) { viewModel.cancelRequest() }
                }

                is ApiResult.Error -> {
                    showLoading(false)
                    showToast(response.message)
                }
            }
        }
    }

    private fun navigateToCamera() {
        setFragmentResultListener(CAMERA_REQUEST_KEY) { _, bundle ->
            val result = bundle.getString(CAMERA_BUNDLE_KEY)?.toUri()
            result?.let {
                viewModel.setUri(it)
            } ?: run {
                showToast("No image captured")
            }
        }
        findNavController().navigate(AddStoryFragmentDirections.actionAddStoryFragmentToCameraFragment())
    }

    private fun navigateToGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun showImage(hasImage: Boolean) {
        binding.imgStory.visibility = if (hasImage) VISIBLE else GONE
        binding.imgBorder.visibility = if (hasImage) VISIBLE else GONE
        binding.btnAddImage.root.visibility = if (hasImage) GONE else VISIBLE
    }

}