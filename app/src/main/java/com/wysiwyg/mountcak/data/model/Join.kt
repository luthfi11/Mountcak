package com.wysiwyg.mountcak.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Join (
    val id: String? = null,
    val eventId: String? = null,
    val postSender: String? = null,
    val userReqId: String? = null,
    val status: Int? = null,
    val timeStamp: Long? = null,
    val confirmTime: Long? = null
): Parcelable