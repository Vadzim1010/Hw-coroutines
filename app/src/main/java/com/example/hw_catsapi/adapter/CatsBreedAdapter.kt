package com.example.hw_catsapi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.size.ViewSizeResolver
import com.example.hw_catsapi.databinding.ItemCatsBreedBinding
import com.example.hw_catsapi.databinding.ItemErrorBinding
import com.example.hw_catsapi.databinding.ItemLoadingBinding
import com.example.hw_catsapi.model.Cat
import com.example.hw_catsapi.model.PagingItem


class CatsBreedAdapter(
    context: Context,
    private val itemClick: (String) -> Unit
) : ListAdapter<PagingItem<Cat>, ItemViewHolder>(DIF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PagingItem.Content -> TYPE_CATS_BREEDS
            is PagingItem.Error -> TYPE_ERROR
            PagingItem.Loading -> TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return when (viewType) {
            TYPE_CATS_BREEDS -> {
                CatsBreedViewHolder(
                    binding = ItemCatsBreedBinding.inflate(layoutInflater, parent, false),
                    itemClick = itemClick
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

        private val DIF_UTIL = object : DiffUtil.ItemCallback<PagingItem<Cat>>() {
            override fun areItemsTheSame(
                oldPagingItem: PagingItem<Cat>,
                newPagingItem: PagingItem<Cat>
            ): Boolean {
                return oldPagingItem == newPagingItem
            }

            override fun areContentsTheSame(
                oldPagingItem: PagingItem<Cat>,
                newPagingItem: PagingItem<Cat>
            ): Boolean {
                val oldCatBreedItem =
                    oldPagingItem as? PagingItem.Content<Cat> ?: return false
                val newCatBreedItem =
                    oldPagingItem as? PagingItem.Content<Cat> ?: return false
                return oldCatBreedItem.data.breed == newCatBreedItem.data.breed &&
                        oldCatBreedItem.data.catImageUrl == newCatBreedItem.data.catImageUrl
            }
        }
    }
}


abstract class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    abstract fun bind(item: PagingItem<Cat>)
}

class CatsBreedViewHolder(
    private val binding: ItemCatsBreedBinding,
    private val itemClick: (String) -> Unit
) : ItemViewHolder(binding.root) {

    override fun bind(item: PagingItem<Cat>) {
        val itemCat = item as? PagingItem.Content<Cat> ?: return
        with(binding) {
            catBreedTextView.text = itemCat.data.breed
            catImageView.load(itemCat.data.catImageUrl) {
                scale(Scale.FILL)
                size(ViewSizeResolver(root))
            }
            cardView.setOnClickListener {
                itemClick(itemCat.data.id)
            }
        }
    }
}

class ErrorViewHolder(
    private val binding: ItemErrorBinding
) : ItemViewHolder(binding.root) {

    override fun bind(item: PagingItem<Cat>) {
        val errorItem = item as? PagingItem.Error ?: return
        binding.errorTextView.text = errorItem.error.message
    }
}

class LoadingViewHolder(
    binding: ItemLoadingBinding
) : ItemViewHolder(binding.root) {

    override fun bind(item: PagingItem<Cat>) {
        // do nothing
    }
}