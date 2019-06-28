package com.wysiwyg.mountcak.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Rental (
    val id: Int? = null,
    val storeName: String? = null,
    val address: String? = null,
    val city: String? = null,
    val region: String? = null,
    val contact: String? = null,
    val instagram: String? = null,
    val openHour: String? = null,
    val storePict: String? = null,
    val priceList: String? = null,
    val linkMap: String? = null,
    val longLat: String? = null
): Parcelable