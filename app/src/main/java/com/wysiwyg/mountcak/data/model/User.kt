package com.wysiwyg.mountcak.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String? = null,
    val name: String? = null,
    val city: String? = null,
    val email: String? = null
) : Parcelable