package com.richard.storyapp.feature.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.richard.storyapp.R
import com.richard.storyapp.core.data.local.room.Story
import com.richard.storyapp.core.ui.BaseFragment
import com.richard.storyapp.core.ui.LoadingStateAdapter
import com.richard.storyapp.core.ui.StoryAdapter
import com.richard.storyapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : BaseFragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var storyAdapter: StoryAdapter
    private val viewModel: HomeViewModel by viewModels()

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
        setupRecyclerView()
        setupObserver()
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

    private fun setupRecyclerView() {
        binding.rvStory.layoutManager = LinearLayoutManager(requireContext())
        storyAdapter = StoryAdapter()
        binding.rvStory.adapter = storyAdapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                storyAdapter.retry()
            }
        )
        storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Story, viewHolder: StoryAdapter.ViewHolder) {
                val extras = FragmentNavigatorExtras(
                    viewHolder.binding.ivStoryPhoto to "photoTransition"
                )
                findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToDetailFragment(),
                    extras
                )
            }
        })
    }

    private fun setupObserver() {
        viewModel.stories.observe(viewLifecycleOwner) {
            lifecycleScope.launch { storyAdapter.submitData(it) }
        }
    }

    private fun setEventClickListener() {
        binding.apply {
            btnAdd.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToAddStoryFragment())
            }
        }
    }

}