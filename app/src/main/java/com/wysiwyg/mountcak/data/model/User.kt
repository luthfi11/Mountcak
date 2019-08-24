package com.wysiwyg.mountcak.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val id: String? = null,
    val name: String? = null,
    val city: String? = null,
    val email: String? = null,
    val phone: String? = null,
    val photo: String? = "https://firebasestorage.googleapis.com/v0/b/mountcak-16aa7.appspot.com/o/default.png?alt=media&token=3cea56c0-22c2-4615-aeda-44cb02014d47"
) : Parcelable