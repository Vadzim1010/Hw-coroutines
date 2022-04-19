package com.example.hw_catsapi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hw_catsapi.databinding.ItemCatsBreedBinding
import com.example.hw_catsapi.databinding.ItemLoadingBinding
import com.example.hw_catsapi.model.LoadState

class CatsBreedAdapter(context: Context) :
    ListAdapter<LoadState, RecyclerView.ViewHolder>(DIF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is LoadState.CatBreed -> TYPE_CATS_BREEDS
            LoadState.Loading -> TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CATS_BREEDS -> {
                CatsBreedViewHolder(
                    binding = ItemCatsBreedBinding.inflate(layoutInflater, parent, false)
                )
            }
            TYPE_LOADING -> {
                LoadingViewHolder(
                    binding = ItemLoadingBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> error("incorrect loading type : $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val catsBreedsHolder = holder as? CatsBreedViewHolder ?: return
        val item = getItem(position) as? LoadState.CatBreed ?: return
        catsBreedsHolder.bind(item)
    }

    companion object {

        private const val TYPE_CATS_BREEDS = 1
        private const val TYPE_LOADING = 0

        private val DIF_UTIL = object : DiffUtil.ItemCallback<LoadState>() {
            override fun areItemsTheSame(
                oldItem: LoadState,
                newItem: LoadState
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: LoadState,
                newItem: LoadState
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class CatsBreedViewHolder(private val binding: ItemCatsBreedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(catBreed: LoadState.CatBreed) = with(binding) {
        catBreedTextView.text = catBreed.breed
        catImageView.load(catBreed.catImageUrl)
    }
}

class LoadingViewHolder(binding: ItemLoadingBinding) : RecyclerView.ViewHolder(binding.root)
