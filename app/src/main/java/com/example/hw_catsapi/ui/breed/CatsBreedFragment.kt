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
import com.example.hw_catsapi.databinding.FragmentCatsBreedBinding
import com.example.hw_catsapi.model.CatBreed
import com.example.hw_catsapi.model.PagingItem
import com.example.hw_catsapi.utils.addBottomSpaceDecorationRes
import com.example.hw_catsapi.utils.addPagingScrollListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CatsBreedFragment : Fragment() {

    private var _binding: FragmentCatsBreedBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is null $_binding" }
    private val viewModel by viewModels<CatsBreedViewModel> {
        CatsBreedViewModelFactory((requireActivity().application as CatsApplication).repository)
    }
    private val catsAdapter by lazy {
        CatsBreedAdapter(requireContext()) {
            findNavController().navigate(CatsBreedFragmentDirections.toDescription(it))
        }
    }

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
        fetchPage()

        with(binding) {

            swipeRefresh
                .onRefreshListener()
                .onEach {
                    viewModel.refresh()
                    viewModel.fetchBreeds()
                    swipeRefresh.isRefreshing = false
                }
                .launchIn(viewLifecycleOwner.lifecycleScope)

            recycler.apply {
                val layout = LinearLayoutManager(requireContext())
                adapter = catsAdapter
                layoutManager = layout
                addBottomSpaceDecorationRes(resources.getDimensionPixelSize(R.dimen.item_space_bottom))
                addPagingScrollListener(layout, 10) {
                    lifecycleScope.launch {
                        viewModel.fetchBreeds()
                    }
                }
            }

            lifecycleScope.launch {
                viewModel.fetchBreeds()
            }
        }
    }

    private fun fetchPage() {
        viewModel.sharedFlow
            .onEach {
                catsAdapter.submitList(it)
            }
            .launchIn(viewLifecycleOwner.lifecycleScope)
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
