package com.example.hw_catsapi.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.hw_catsapi.adapter.holder.CatViewHolder
import com.example.hw_catsapi.adapter.holder.ErrorViewHolder
import com.example.hw_catsapi.adapter.holder.LoadingViewHolder
import com.example.hw_catsapi.databinding.ItemCatBinding
import com.example.hw_catsapi.databinding.ItemErrorBinding
import com.example.hw_catsapi.databinding.ItemLoadingBinding
import com.example.hw_catsapi.model.Cat
import com.example.hw_catsapi.model.PagingItem


class CatsAdapter(
    context: Context,
    private val itemClick: (String) -> Unit
) : ListAdapter<PagingItem<Cat>, RecyclerView.ViewHolder>(DIF_UTIL) {


    private val layoutInflater = LayoutInflater.from(context)


    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is PagingItem.Content -> TYPE_CONTENT
            is PagingItem.Error -> TYPE_ERROR
            PagingItem.Loading -> TYPE_LOADING
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_CONTENT -> {
                CatViewHolder(
                    binding = ItemCatBinding.inflate(layoutInflater, parent, false),
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            TYPE_CONTENT -> {
                val typedHolder = holder as? CatViewHolder ?: return
                val typedItem = getItem(position) as? PagingItem.Content ?: return
                typedHolder.bind(typedItem)
            }
            TYPE_ERROR -> {
                val typedHolder = holder as? ErrorViewHolder ?: return
                val typedItem = getItem(position) as? PagingItem.Error ?: return
                typedHolder.bind(typedItem)
            }
        }
    }


    companion object {


        private const val TYPE_CONTENT = 0
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
