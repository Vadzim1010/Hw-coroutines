package com.example.hw_catsapi.ui.breed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hw_catsapi.databinding.FragmentCatsBreedBinding
import com.example.hw_catsapi.ui.CatsApplication
import com.example.hw_catsapi.ui.adapter.CatsBreedAdapter

class CatsBreedFragment : Fragment() {

    private var _binding: FragmentCatsBreedBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is null $_binding" }
    private val viewModel by viewModels<CatsBreedViewModel> {
        CatsBreedViewModelFactory((requireActivity().application as CatsApplication).repository)
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

        val catsAdapter = CatsBreedAdapter(requireContext())

        with(binding) {
            recycler.apply {
                adapter = catsAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            viewModel.fetchCatsBreeds().observe(viewLifecycleOwner) {
                catsAdapter.submitList(it.toList())
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}