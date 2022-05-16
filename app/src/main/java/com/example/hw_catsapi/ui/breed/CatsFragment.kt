package com.example.hw_catsapi.ui.breed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.hw_catsapi.R
import com.example.hw_catsapi.adapter.CatsAdapter
import com.example.hw_catsapi.databinding.FragmentCatsBinding
import com.example.hw_catsapi.utils.addBottomSpaceDecorationRes
import com.example.hw_catsapi.utils.addPagingScrollFlow
import com.example.hw_catsapi.utils.onNetworkChanges
import com.example.hw_catsapi.utils.repository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class CatsFragment : Fragment() {


    private var _binding: FragmentCatsBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is null $_binding" }
    private val viewModel by viewModels<CatsViewModel> {
        CatsViewModelFactory(requireContext().repository)
    }
    private val catsAdapter by lazy {
        CatsAdapter(requireContext()) {
            findNavController().navigate(CatsFragmentDirections.toDescription(it))
        }
    }
    private var isLoading = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentCatsBinding.inflate(layoutInflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        subscribeOnPagingFlow()
        loadNextPage()
        addRefreshListener()
        addNetworkStateListener()
    }

    private fun loadNextPage() {
        viewLifecycleOwner.lifecycleScope.launch {
            if (!isLoading) {
                isLoading = true
                viewModel.fetchPage()
                isLoading = false
            }
        }
    }

    private fun subscribeOnPagingFlow() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.sharedFlow
                .onEach { pagingList ->
                    catsAdapter.submitList(pagingList)
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    private fun addNetworkStateListener() {
        requireContext().onNetworkChanges
            .filter { isNetworkAvailable ->
                !isNetworkAvailable
            }
            .onEach {
                Toast.makeText(requireContext(), "Lost internet connection.", Toast.LENGTH_SHORT)
                    .show()
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun addRefreshListener() = with(binding) {
        swipeRefresh
            .onRefreshListener()
            .onEach {
                viewModel.refresh()
                swipeRefresh.isRefreshing = false
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun addScrollListener(manager: LinearLayoutManager) = with(binding) {
        recycler.apply {
            addBottomSpaceDecorationRes(resources.getDimensionPixelSize(R.dimen.item_space_bottom))
            addPagingScrollFlow(manager, ITEMS_TO_LOAD, LAST_ITEM)
                .onEach { loadNextPage() }
                .launchIn(viewLifecycleOwner.lifecycleScope)
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

    companion object {

        private const val LAST_ITEM =
            67 // костыль потому что не разобрался как достсать размер списка из апи
        private const val ITEMS_TO_LOAD = 10
    }
}
