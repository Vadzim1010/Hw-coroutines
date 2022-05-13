package com.example.hw_catsapi.ui.breed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.hw_catsapi.CatsApplication
import com.example.hw_catsapi.R
import com.example.hw_catsapi.adapter.CatsBreedAdapter
import com.example.hw_catsapi.database.CatEntity
import com.example.hw_catsapi.databinding.FragmentCatsBreedBinding
import com.example.hw_catsapi.model.Cat
import com.example.hw_catsapi.model.PagingItem
import com.example.hw_catsapi.utils.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*

class CatsBreedFragment : Fragment() {

    private var _binding: FragmentCatsBreedBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is null $_binding" }
    private val viewModel by viewModels<CatsBreedViewModel> {
        CatsBreedViewModelFactory(requireContext().repository)
    }
    private val catsAdapter by lazy {
        CatsBreedAdapter(requireContext()) {
            findNavController().navigate(CatsBreedFragmentDirections.toDescription(it))
        }
    }
    private val pagingFlow = MutableSharedFlow<Unit>(
        replay = 1
    )


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCatsBreedBinding.inflate(layoutInflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadPages()
        initRecycler()
        addRefreshListener()
        requireContext().onNetworkChanges
            .onEach { isNetWorkAvailable ->

            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.loadCacheCats()
            viewModel.fetchPage()
        }
        pagingFlow
            .onEach {
                viewModel.loadCacheCats()
                viewModel.fetchPage()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun loadPages() {
        viewModel.sharedFlow
            .onEach { pagingList ->
                catsAdapter.submitList(pagingList)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun addRefreshListener() = with(binding) {
        swipeRefresh
            .onRefreshListener()
            .onEach {
                viewModel.refresh()
                viewModel.fetchPage()
                swipeRefresh.isRefreshing = false
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun addScrollListener(manager: LinearLayoutManager) = with(binding) {
        recycler.apply {
            addBottomSpaceDecorationRes(resources.getDimensionPixelSize(R.dimen.item_space_bottom))
            addPagingScrollListener(manager, 10) {
                pagingFlow.tryEmit(Unit)
            }
        }
    }


    private fun initRecycler() = with(binding) {
        recycler.apply {
            val manager = LinearLayoutManager(requireContext())
            adapter = catsAdapter
            layoutManager = manager
            addScrollListener(manager)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun SwipeRefreshLayout.onRefreshListener() = callbackFlow {
        this@onRefreshListener.setOnRefreshListener {
            this.trySend(Unit)
        }
        this.awaitClose()
    }
}
