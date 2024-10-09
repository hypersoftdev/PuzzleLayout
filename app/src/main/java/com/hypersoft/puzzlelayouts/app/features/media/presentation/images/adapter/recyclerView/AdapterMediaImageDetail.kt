package com.hypersoft.puzzlelayouts.app.features.media.presentation.images.adapter.recyclerView

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.hypersoft.puzzlelayouts.app.features.media.domain.images.entities.ItemMediaImagePhoto
import com.hypersoft.puzzlelayouts.databinding.ItemMediaImageDetailBinding
import com.hypersoft.puzzlelayouts.utilities.extensions.setImageFromUri

class AdapterMediaImageDetail(private val itemClick: (imageUri: Uri) -> Unit) : ListAdapter<ItemMediaImagePhoto, AdapterMediaImageDetail.CustomViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemMediaImageDetailBinding.inflate(layoutInflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = getItem(position)
        bindViews(holder, currentItem)

        holder.binding.root.setOnClickListener {
            itemClick.invoke(currentItem.uri)
        }
    }

    private fun bindViews(holder: CustomViewHolder, currentItem: ItemMediaImagePhoto) {
        holder.binding.ifv.setImageFromUri(currentItem.uri)
    }

    inner class CustomViewHolder(val binding: ItemMediaImageDetailBinding) : RecyclerView.ViewHolder(binding.root)

    class DiffCallback : DiffUtil.ItemCallback<ItemMediaImagePhoto>() {
        override fun areItemsTheSame(oldItem: ItemMediaImagePhoto, newItem: ItemMediaImagePhoto): Boolean {
            return oldItem.uri == newItem.uri
        }

        override fun areContentsTheSame(oldItem: ItemMediaImagePhoto, newItem: ItemMediaImagePhoto): Boolean {
            return oldItem == newItem
        }
    }
}