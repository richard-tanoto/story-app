package com.richard.storyapp.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.richard.storyapp.R
import com.richard.storyapp.core.ui.BaseFragment
import com.richard.storyapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding

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
        setupToolbar()
        setEventClickListener()
    }

    private fun setupToolbar() {
        binding.toolbar.apply {
            title = getString(R.string.app_name)
            navigationIcon = null
            inflateMenu(R.menu.home_menu)
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.destMaps -> findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMapsFragment())
                }
                true
            }
        }
    }

    private fun setEventClickListener() {
        binding.apply {
            btnAdd.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddStoryFragment())
            }
            btnToDetail.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailFragment())
            }
            btnToLogin.setOnClickListener {
                navigateToLogin(R.id.homeFragment)
            }
        }
    }

}