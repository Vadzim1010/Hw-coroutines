package com.example.hw_catsapi.adapter.holder

import androidx.recyclerview.widget.RecyclerView
import com.example.hw_catsapi.databinding.ItemErrorBinding
import com.example.hw_catsapi.model.PagingItem


class ErrorViewHolder(
    private val binding: ItemErrorBinding
) : RecyclerView.ViewHolder(binding.root) {


    fun bind(errorItem: PagingItem.Error) {
        binding.errorTextView.text = errorItem.error.message
    }
}
