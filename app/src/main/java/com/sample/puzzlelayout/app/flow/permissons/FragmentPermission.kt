package com.sample.puzzlelayout.app.flow.permissons

import com.sample.puzzlelayout.R
import com.sample.puzzlelayout.databinding.FragmentPermissonBinding
import com.sample.puzzlelayout.utilities.base.fragment.BaseFragment
import com.sample.puzzlelayout.utilities.extensions.navigateTo

class FragmentPermission : BaseFragment<FragmentPermissonBinding>(FragmentPermissonBinding::inflate) {

    override fun onViewCreated() {
        binding.mBtnPermission.setOnClickListener {
            askStoragePermission {
                navigateTo(R.id.fragmentPermission, R.id.action_global_fragmentHome)
            }
        }
    }

}