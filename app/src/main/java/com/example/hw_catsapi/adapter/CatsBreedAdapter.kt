package com.example.hw_catsapi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hw_catsapi.databinding.ItemCatsBreedBinding
import com.example.hw_catsapi.databinding.ItemErrorBinding
import com.example.hw_catsapi.databinding.ItemLoadingBinding
import com.example.hw_catsapi.model.Item

class CatsBreedAdapter(context: Context) :
    ListAdapter<Item, ItemViewHolder>(DIF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Item.CatBreed -> TYPE_CATS_BREEDS
            is Item.Error -> TYPE_ERROR
            Item.Loading -> TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
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
            TYPE_ERROR -> {
                ErrorViewHolder(
                    binding = ItemErrorBinding.inflate(layoutInflater, parent, false)
                )
            }
            else -> error("incorrect loading type : $viewType")
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        private const val TYPE_CATS_BREEDS = 0
        private const val TYPE_ERROR = 1
        private const val TYPE_LOADING = 2

        private val DIF_UTIL = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Item,
                newItem: Item
            ): Boolean {
                val oldCatBreedItem = oldItem as? Item.CatBreed ?: return false
                val newCatBreedItem = oldItem as? Item.CatBreed ?: return false
                return oldCatBreedItem.breed == newCatBreedItem.breed &&
                        oldCatBreedItem.catImageUrl == newCatBreedItem.catImageUrl
            }
        }
    }
}

abstract class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: Item)
}

class CatsBreedViewHolder(private val binding: ItemCatsBreedBinding) :
    ItemViewHolder(binding.root) {

    override fun bind(item: Item) {
        val itemCat = item as? Item.CatBreed ?: return
        binding.catBreedTextView.text = itemCat.breed
        binding.catImageView.load(itemCat.catImageUrl)
    }
}

class ErrorViewHolder(
    private val binding: ItemErrorBinding
) : ItemViewHolder(binding.root) {

    override fun bind(item: Item) {
        val errorItem = item as? Item.Error ?: return
        binding.errorTextView.text = errorItem.error
    }
}

class LoadingViewHolder(
    binding: ItemLoadingBinding
) : ItemViewHolder(binding.root) {

    override fun bind(item: Item) {
        // do nothing
    }
}