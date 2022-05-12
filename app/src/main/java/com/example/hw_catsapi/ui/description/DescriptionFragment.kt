package com.example.hw_catsapi.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.hw_catsapi.CatsApplication
import com.example.hw_catsapi.databinding.FragementDescriptionBinding
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DescriptionFragment : Fragment() {

    private var _binding: FragementDescriptionBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is null $_binding" }
    private val args by navArgs<DescriptionFragmentArgs>()
    private val viewModel by viewModels<DescriptionViewModel> {
        DescriptionViewModelFactory((requireActivity().application as CatsApplication).repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragementDescriptionBinding.inflate(layoutInflater, container, false)
            .also { _binding = it }
            .root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val id = args.itemId

        with(binding) {
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.fetchDescription(id).collect { description ->
                    if (description != null) {
                        catImage.load(description.catImageUrl)
                        catBreed.text = description.breed
                        catDescription.text = description.description
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
