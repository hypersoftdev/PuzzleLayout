package com.hypersoft.puzzlelayouts.utilities.base.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

open class BasePermission : Fragment() {

    private val sharedPreferences by lazy { context?.getSharedPreferences("permission_preferences", Context.MODE_PRIVATE) }
    private val editor by lazy { sharedPreferences?.edit() }
    private var callback: ((Boolean) -> Unit)? = null

    private val storagePermission by lazy {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                Manifest.permission.READ_MEDIA_IMAGES
            }

            else -> {
                Manifest.permission.READ_EXTERNAL_STORAGE
            }
        }
    }

    private val storagePermissionArray by lazy {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                )
            }

            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                arrayOf(
                    Manifest.permission.READ_MEDIA_IMAGES,
                )
            }

            else -> {
                arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            }
        }
    }

    private val permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
        val contains = it.values.contains(true)

        callback?.invoke(contains)
    }

    private var settingStorageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
        val permissionResult = checkStoragePermission()
        callback?.invoke(permissionResult)
    }

    fun checkStoragePermission(): Boolean {
        context?.let {
            return when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                        ContextCompat.checkSelfPermission(it, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED -> {
                    // Full access on Android 13 (API level 33) or higher
                    true
                }

                Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
                        ContextCompat.checkSelfPermission(it, Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED) == PackageManager.PERMISSION_GRANTED -> {
                    // Partial access on Android 14 (API level 34) or higher
                    true
                }

                ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED -> {
                    true
                }

                else -> false
            }
        } ?: return false
    }

    fun askStoragePermission(callback: (Boolean) -> Unit) {
        this.callback = callback
        context?.let {
            if (checkStoragePermission()) {
                callback.invoke(true)
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(it as Activity, storagePermission)) {
                    permissionLauncher.launch(storagePermissionArray)
                } else {
                    if (sharedPreferences?.getBoolean(storagePermission, true) == true) {
                        editor?.putBoolean(storagePermission, false)
                        editor?.apply()
                        permissionLauncher.launch(storagePermissionArray)
                    } else {
                        openSettingPage(1)
                    }
                }
            }
        }
    }

    private fun openSettingPage(caseType: Int) {
        context?.let {
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
            val uri: Uri = Uri.fromParts("package", it.packageName, null)
            intent.data = uri
            when (caseType) {
                0 -> {

                }

                else -> {
                    settingStorageLauncher.launch(intent)
                }
            }
        }
    }
}
