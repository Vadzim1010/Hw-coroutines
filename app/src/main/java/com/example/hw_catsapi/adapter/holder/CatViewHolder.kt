package com.example.hw_catsapi.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.size.ViewSizeResolver
import com.example.hw_catsapi.databinding.ItemCatBinding
import com.example.hw_catsapi.model.Cat
import com.example.hw_catsapi.model.PagingItem


class CatViewHolder(
    private val binding: ItemCatBinding,
    private val itemClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(itemCat: PagingItem.Content<Cat>) {
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
