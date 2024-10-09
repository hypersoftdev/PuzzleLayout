package com.hypersoft.puzzlelayouts.app.flow.permissons

import com.hypersoft.puzzlelayouts.R
import com.hypersoft.puzzlelayouts.databinding.FragmentPermissonBinding
import com.hypersoft.puzzlelayouts.utilities.base.fragment.BaseFragment
import com.hypersoft.puzzlelayouts.utilities.extensions.navigateTo

class FragmentPermission : BaseFragment<FragmentPermissonBinding>(FragmentPermissonBinding::inflate) {

    override fun onViewCreated() {
        binding.mBtnPermission.setOnClickListener {
            askStoragePermission {
                navigateTo(R.id.fragmentPermission, R.id.action_global_fragmentHome)
            }
        }
    }

}