package com.example.hw_catsapi.ui.description

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.setupWithNavController
import coil.load
import com.example.hw_catsapi.CatsApplication
import com.example.hw_catsapi.databinding.FragementDescriptionBinding
import com.example.hw_catsapi.utils.repository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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

        initDescription(id)
        initButtons()
    }

    private fun initButtons() = with(binding) {
        toolbar.setupWithNavController(findNavController())
    }

    private fun initDescription(id: String) = with(binding) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.fetchDescription(id)
                ?.onEach { description ->
                    checkNotNull(description)
                    catImage.load(description.catImageUrl)
                    catBreed.text = description.breed
                    catDescription.text = description.description
                }
                ?.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
