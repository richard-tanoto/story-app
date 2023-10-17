package com.richard.storyapp.feature.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.navigation.fragment.findNavController
import com.richard.storyapp.R
import com.richard.storyapp.core.ui.BaseFragment
import com.richard.storyapp.databinding.FragmentAddStoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddStoryFragment : BaseFragment() {

    private lateinit var binding: FragmentAddStoryBinding

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
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            title = getString(R.string.add_story)
            navigationIcon = AppCompatResources.getDrawable(requireContext(), R.drawable.ic_back)
            setNavigationOnClickListener { findNavController().popBackStack() }
        }
    }

}