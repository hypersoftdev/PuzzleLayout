package com.hypersoft.puzzlelayouts.app.features.media.presentation.images.ui

import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.hypersoft.puzzlelayouts.R
import com.hypersoft.puzzlelayouts.app.features.media.data.images.dataSources.MediaStoreMediaImages
import com.hypersoft.puzzlelayouts.app.features.media.data.images.repository.RepositoryMediaImages
import com.hypersoft.puzzlelayouts.app.features.media.domain.images.entities.ItemMediaImageFolder
import com.hypersoft.puzzlelayouts.app.features.media.domain.images.useCases.UseCaseMediaImage
import com.hypersoft.puzzlelayouts.app.features.media.presentation.images.adapter.viewPager.PagerAdapterMediaImage
import com.hypersoft.puzzlelayouts.app.features.media.presentation.images.viewModels.ViewModelMediaImage
import com.hypersoft.puzzlelayouts.app.features.media.presentation.images.viewModels.ViewModelMediaImageProvider
import com.hypersoft.puzzlelayouts.databinding.FragmentMediaImageBinding
import com.hypersoft.puzzlelayouts.utilities.base.fragment.BaseFragment
import com.hypersoft.puzzlelayouts.utilities.extensions.getResString
import com.hypersoft.puzzlelayouts.utilities.extensions.navigateTo
import com.hypersoft.puzzlelayouts.utilities.extensions.popFrom
import com.hypersoft.puzzlelayouts.utilities.extensions.showToast
import com.hypersoft.puzzlelayouts.utilities.transformations.PagerZoomOutTransformer

class FragmentMediaImage : BaseFragment<FragmentMediaImageBinding>(FragmentMediaImageBinding::inflate) {

    private lateinit var pagerAdapter: PagerAdapterMediaImage

    // MVVM
    private val mediaStoreMediaImages by lazy { MediaStoreMediaImages(context?.contentResolver) }
    private val repositoryMediaImages by lazy { RepositoryMediaImages(mediaStoreMediaImages) }
    private val useCaseMediaImage by lazy { UseCaseMediaImage(repositoryMediaImages) }
    private val viewModelMediaImage by viewModels<ViewModelMediaImage> { ViewModelMediaImageProvider(useCaseMediaImage) }

    override fun onViewCreated() {
        initObservers()

        binding.toolbarMediaImage.setOnClickListener { popFrom(R.id.fragmentMediaImage) }
    }

    private fun initObservers() {
        viewModelMediaImage.foldersLiveData.observe(viewLifecycleOwner) { folderList ->
            initViewPager(folderList)
        }
        viewModelMediaImage.errorLiveData.observe(viewLifecycleOwner) {
            context.showToast(it)
            initViewPager(emptyList())
        }

        diComponent.generalObserver.navigationDirectionsMediaImageLiveData.observe(viewLifecycleOwner) {
            navigateTo(R.id.fragmentMediaImage, it)
        }
    }

    private fun initViewPager(folderList: List<ItemMediaImageFolder>) {
        pagerAdapter = PagerAdapterMediaImage(this)
        fillTabs(folderList)

        binding.vpContainerMediaImage.adapter = pagerAdapter
        binding.vpContainerMediaImage.setPageTransformer(PagerZoomOutTransformer())
        initTabLayout()
    }

    private fun fillTabs(folderList: List<ItemMediaImageFolder>) {
        addTab(context.getResString(R.string.all))
        folderList.forEach { addTab(it.folderName) }
    }

    private fun addTab(tabName: String) {
        binding.tlContainerMediaImage.addTab(binding.tlContainerMediaImage.newTab().setText(tabName))
        pagerAdapter.addFolder(ItemMediaImageFolder(tabName))
    }

    private fun initTabLayout() {
        TabLayoutMediator(binding.tlContainerMediaImage, binding.vpContainerMediaImage) { tab, position ->
            tab.text = pagerAdapter.getFolderName(position)
        }.attach()
    }
}