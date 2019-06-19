package com.wysiwyg.mountcak.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Event (
    val id: String? = null,
    val title: String? = null,
    val userId: String? = null,
    val mountId: String? = null,
    val dateStart: String? = null,
    val dateEnd: String? = null,
    val eventNote: String? = null,
    val cost: Int? = 0,
    val maxParticipant: Int? = 0,
    val meetLocation: String? = null,
    val joinedParticipant: Int? = 0
) : Parcelable