package com.example.hw_catsapi.ui.breed

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.hw_catsapi.databinding.FragmentCatsBreedBinding

class CatsBreedFragment : Fragment() {

    private var _binding: FragmentCatsBreedBinding? = null
    private val binding get() = requireNotNull(_binding) { "binding is null $_binding" }

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

        with(binding) {}

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}