package com.example.hw_catsapi.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.hw_catsapi.databinding.ItemCatsBreedBinding
import com.example.hw_catsapi.ui.model.CatBreed

class CatsBreedAdapter(context: Context) : ListAdapter<CatBreed, CatsBreedViewHolder>(DIF_UTIL) {

    private val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatsBreedViewHolder {
        return CatsBreedViewHolder(
            binding = ItemCatsBreedBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CatsBreedViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {

        private val DIF_UTIL = object : DiffUtil.ItemCallback<CatBreed>() {
            override fun areItemsTheSame(oldItem: CatBreed, newItem: CatBreed): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: CatBreed, newItem: CatBreed): Boolean {
                return oldItem.breed == newItem.breed &&
                        oldItem.catImageUrl == newItem.catImageUrl
            }
        }
    }
}

class CatsBreedViewHolder(private val binding: ItemCatsBreedBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(catBreed: CatBreed) = with(binding) {
        catBreedTextView.text = catBreed.breed
        catImageView.load(catBreed.catImageUrl)
    }
}
