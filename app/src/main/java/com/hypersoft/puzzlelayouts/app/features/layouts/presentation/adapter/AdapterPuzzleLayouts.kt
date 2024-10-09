package com.hypersoft.puzzlelayouts.app.features.layouts.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hypersoft.pzlayout.interfaces.PuzzleLayout
import com.hypersoft.pzlayout.layouts.slant.NumberSlantLayout
import com.hypersoft.pzlayout.layouts.straight.NumberStraightLayout
import com.hypersoft.puzzlelayouts.databinding.ItemPuzzleLayoutsBinding


class AdapterPuzzleLayouts(private val itemClick: (puzzleLayout: PuzzleLayout, theme: Int) -> Unit) : RecyclerView.Adapter<AdapterPuzzleLayouts.CustomViewHolder>() {

    private var puzzleLayouts: List<PuzzleLayout> = emptyList()

    fun setPuzzleLayouts(newPuzzleLayouts: List<PuzzleLayout>) {
        this.puzzleLayouts = newPuzzleLayouts
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemPuzzleLayoutsBinding.inflate(layoutInflater, parent, false)
        return CustomViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val currentItem = puzzleLayouts[position]
        bindViews(holder, currentItem)

        holder.binding.root.setOnClickListener {
            var theme = 0
            if (currentItem is NumberSlantLayout) {
                theme = currentItem.theme
                //  theme = currentItem.getThemeCount()
            } else if (currentItem is NumberStraightLayout) {
                theme = currentItem.theme
                //   theme = currentItem.getThemeCount()
            }
            itemClick.invoke(currentItem, theme)
        }

    }

    override fun getItemCount(): Int {
        return puzzleLayouts.size
    }

    private fun bindViews(holder: CustomViewHolder, currentItem: PuzzleLayout) {
        holder.binding.puzzle.needDrawLine = true
        holder.binding.puzzle.needDrawOuterLine = true
        holder.binding.puzzle.isTouchEnable = false

        holder.binding.puzzle.setPuzzleLayout(currentItem)
    }

    inner class CustomViewHolder(val binding: ItemPuzzleLayoutsBinding) : RecyclerView.ViewHolder(binding.root)
}
