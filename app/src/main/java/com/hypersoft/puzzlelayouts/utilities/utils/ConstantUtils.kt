package com.hypersoft.puzzlelayouts.utilities.utils

object ConstantUtils {

    const val TAG = "TAG_MyTag"
    const val TAG_ADS = "TAG_ADS"
    const val TAG_REMOTE_CONFIG = "TAG_REMOTE_CONFIG"


    // Media > Images
    const val GALLERY_ALL = "_All_"
    const val GALLERY_UNKNOWN = "Unknown"

    var isOneStraightLayout = 0
    fun returnOneStraightLayout(count: Int): Int {
        return if (count == 5) {
            isOneStraightLayout = 0
            5
        } else isOneStraightLayout++
    }

    var isFourStraightLayout = 0
    fun returnFourStraightLayout(count: Int): Int {
        return if (count == 7) {
            isFourStraightLayout = 0
            7
        } else isFourStraightLayout++
    }

    var isFiveStraightLayout = 0
    fun returnFiveStraightLayout(count: Int): Int {
        return if (count == 16) {
            isFiveStraightLayout = 0
            16
        } else isFiveStraightLayout++
    }

}