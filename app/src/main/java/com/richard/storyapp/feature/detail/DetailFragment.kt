package com.richard.storyapp.feature.detail

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.richard.storyapp.R
import com.richard.storyapp.core.data.remote.response.ApiResult
import com.richard.storyapp.core.data.remote.response.StoryDetail
import com.richard.storyapp.core.ui.BaseFragment
import com.richard.storyapp.core.util.DateUtil.getFormattedDate
import com.richard.storyapp.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addAnimation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setId()
        setupObserver()

        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    override fun onDestroyView() {
        super.onDestroyView()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
    }

    private fun addAnimation() {
        sharedElementEnterTransition =
            TransitionInflater.from(requireContext()).inflateTransition(android.R.transition.move)
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            title = getString(R.string.detail)
            navigationIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_back)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

    private fun setId() {
        val id = viewModel.getId() ?: args.id
        viewModel.setId(id)
    }

    private fun setupObserver() {
        viewModel.storyDetail.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ApiResult.Success -> {
                    showLoading(false)
                    setData(result.data.story)
                }

                is ApiResult.Loading -> {
                    showLoading(true)
                }

                is ApiResult.Error -> {
                    showLoading(false)
                    showToast(result.message)
                }
            }
        }
    }

    private fun setData(data: StoryDetail) {
        binding.apply {
            Glide.with(this@DetailFragment)
                .load(data.photoUrl)
                .into(imgStory)
            tvName.text = data.name
            tvDescription.text = data.description
            tvDate.text = getFormattedDate(data.createdAt)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.loading.visibility = if (isLoading) VISIBLE else GONE
    }
}