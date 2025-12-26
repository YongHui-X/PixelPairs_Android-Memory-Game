package iss.nus.edu.sg.appfiles.androidca.models

import android.graphics.Bitmap

data class ImageItem(
    val bitmap: Bitmap,
    var isSelected: Boolean = false
)