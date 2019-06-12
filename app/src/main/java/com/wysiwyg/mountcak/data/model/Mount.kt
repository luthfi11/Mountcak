package com.wysiwyg.mountcak.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Mount (
    val id: Int? = null,
    val mountName: String? = null,
    val location: String? = null,
    val city: String? = null,
    val region: String? = null,
    val height: String? = null,
    val description: String? = null,
    val contact: String? = null,
    val instagram: String? = null,
    val route: String? = null,
    val cover: String? = null,
    val linkGMaps: String? = null,
    val linkPhotoGM: String? = null,
    val longLat: String? = null
): Parcelable