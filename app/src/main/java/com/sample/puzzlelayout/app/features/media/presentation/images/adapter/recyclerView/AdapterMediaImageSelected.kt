package com.sample.puzzlelayout.app.features.media.presentation.images.adapter.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sample.puzzlelayout.app.features.media.domain.images.entities.ItemMediaImagePhoto
import com.sample.puzzlelayout.databinding.ItemMediaImageSelectedBinding
import com.sample.puzzlelayout.utilities.extensions.setImageFromUri

class AdapterMediaImageSelected() : ListAdapter<ItemMediaImagePhoto, AdapterMediaImageSelected.CustomViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMediaImageSelectedBinding.inflate(layoutInflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = getItem(position)
        bindViews(holder, currentItem)

        holder.binding.root.setOnClickListener {

        }
    }

    private fun bindViews(holder: CustomViewHolder, currentItem: ItemMediaImagePhoto) {
        holder.binding.ifv.setImageFromUri(currentItem.uri)
    }

    inner class CustomViewHolder(val binding: ItemMediaImageSelectedBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback : DiffUtil.ItemCallback<ItemMediaImagePhoto>() {
        override fun areItemsTheSame(oldItem: ItemMediaImagePhoto, newItem: ItemMediaImagePhoto): Boolean {
            return oldItem.uri == newItem.uri
        }

        override fun areContentsTheSame(oldItem: ItemMediaImagePhoto, newItem: ItemMediaImagePhoto): Boolean {
            return oldItem == newItem
        }
    }
}