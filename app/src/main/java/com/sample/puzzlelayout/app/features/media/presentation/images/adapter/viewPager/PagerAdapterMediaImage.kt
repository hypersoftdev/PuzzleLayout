package com.sample.puzzlelayout.app.features.media.presentation.images.adapter.viewPager

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.sample.puzzlelayout.app.features.media.presentation.images.ui.FragmentMediaImageDetail
import com.sample.puzzlelayout.app.features.media.domain.images.entities.ItemMediaImageFolder
import com.sample.puzzlelayout.utilities.utils.ConstantUtils

class PagerAdapterMediaImage(fragment: Fragment) : FragmentStateAdapter(fragment) {

    private var folderList = arrayListOf<ItemMediaImageFolder>()

    override fun getItemCount(): Int = folderList.size

    override fun createFragment(position: Int): Fragment {
        val folderName = when (position == 0) {
            true -> ConstantUtils.GALLERY_ALL
            false -> folderList[position].folderName
        }
        return FragmentMediaImageDetail.newInstance(folderName)
    }

    fun addFolder(folder: ItemMediaImageFolder) {
        folderList.add(folder)
    }

    fun getFolderName(position: Int): String {
        return folderList[position].folderName
    }
}