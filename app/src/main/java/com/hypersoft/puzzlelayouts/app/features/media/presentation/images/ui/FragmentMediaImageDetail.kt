package com.hypersoft.puzzlelayouts.app.features.media.presentation.images.ui

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.hypersoft.puzzlelayouts.R
import com.hypersoft.puzzlelayouts.app.features.media.data.images.dataSources.MediaStoreMediaImages
import com.hypersoft.puzzlelayouts.app.features.media.data.images.repository.RepositoryMediaImages
import com.hypersoft.puzzlelayouts.app.features.media.domain.images.entities.ItemMediaImagePhoto
import com.hypersoft.puzzlelayouts.app.features.media.domain.images.useCases.UseCaseMediaImageDetail
import com.hypersoft.puzzlelayouts.app.features.media.presentation.images.adapter.recyclerView.AdapterMediaImageDetail
import com.hypersoft.puzzlelayouts.app.features.media.presentation.images.adapter.recyclerView.AdapterMediaImageSelected
import com.hypersoft.puzzlelayouts.app.features.media.presentation.images.viewModels.ViewModelMediaImageDetail
import com.hypersoft.puzzlelayouts.app.features.media.presentation.images.viewModels.ViewModelMediaImageDetailProvider
import com.hypersoft.puzzlelayouts.databinding.FragmentMediaImageDetailBinding
import com.hypersoft.puzzlelayouts.utilities.base.fragment.BaseFragment
import com.hypersoft.puzzlelayouts.utilities.extensions.navigateTo
import com.hypersoft.puzzlelayouts.utilities.extensions.showToast
import com.hypersoft.puzzlelayouts.utilities.utils.ConstantUtils

class FragmentMediaImageDetail : BaseFragment<FragmentMediaImageDetailBinding>(FragmentMediaImageDetailBinding::inflate) {

    private val adapterEnhanceGalleryDetail by lazy { AdapterMediaImageDetail(itemClick) }
    private val adapterMediaImageSelected by lazy { AdapterMediaImageSelected() }
    private val argFolderName by lazy { arguments?.getString(ARG_FOLDER_NAME, ConstantUtils.GALLERY_UNKNOWN) ?: ConstantUtils.GALLERY_UNKNOWN }

    private val mediaStoreMediaImages by lazy { MediaStoreMediaImages(context?.contentResolver) }
    private val repositoryMediaImages by lazy { RepositoryMediaImages(mediaStoreMediaImages) }
    private val useCaseMediaImageDetail by lazy { UseCaseMediaImageDetail(repositoryMediaImages) }
    private val viewModelMediaImageDetail by activityViewModels<ViewModelMediaImageDetail> { ViewModelMediaImageDetailProvider(useCaseMediaImageDetail) }

    private var selectedImage: List<ItemMediaImagePhoto> = mutableListOf()

    override fun onViewCreated() {
        initRecyclerView()
        initObservers()
        fetchData()
        setUpListener()

        binding.srlRefreshMediaImageDetail.setOnRefreshListener { fetchData() }
    }

    private fun initRecyclerView() {
        binding.rcvListMediaImageDetail.adapter = adapterEnhanceGalleryDetail
        binding.SelectedImages.adapter = adapterMediaImageSelected
    }

    private fun initObservers() {
        viewModelMediaImageDetail.imagesLiveData.observe(viewLifecycleOwner) {
            binding.progressBarMediaImageDetail.visibility = View.GONE
            binding.srlRefreshMediaImageDetail.isRefreshing = false
            adapterEnhanceGalleryDetail.submitList(it)
        }
        viewModelMediaImageDetail.refreshLiveData.observe(viewLifecycleOwner) {
            fetchData()
        }
        viewModelMediaImageDetail.errorLiveData.observe(viewLifecycleOwner) {
            binding.progressBarMediaImageDetail.visibility = View.GONE
            context.showToast(it)
        }
        viewModelMediaImageDetail.clickedImagesLiveData.observe(viewLifecycleOwner) { clickedImages ->
            // Update the second RecyclerView's adapter with the clicked images
            selectedImage = clickedImages
            updateUI(clickedImages.size)
            adapterMediaImageSelected.submitList(clickedImages)
        }

    }

    private fun updateUI(size: Int) {
        binding.CountImagesSelected.text = "$size Images Selected"
    }

    private fun fetchData() {
        viewModelMediaImageDetail.getImages(argFolderName)
    }

    private fun setUpListener() = binding.apply {
        nextButtonId.setOnClickListener {
            if (selectedImage.isNotEmpty()) {
                if (selectedImage.size <= 5) {
                    navigateTo(R.id.fragmentMediaImage, R.id.action_fragmentMediaImage_to_fragmentMediaImagesLayouts)
                } else {
                    context?.showToast(R.string.select_only)
                }
            } else {
                context?.showToast(R.string.select)
            }
        }
    }

    private val itemClick: ((Uri) -> Unit) = { viewModelMediaImageDetail.imageClick(it) }

    companion object {
        private const val ARG_FOLDER_NAME = "folderName"

        fun newInstance(folderName: String): FragmentMediaImageDetail {
            val fragment = FragmentMediaImageDetail()
            val bundle = Bundle().apply {
                putString(ARG_FOLDER_NAME, folderName)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}