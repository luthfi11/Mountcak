package com.wysiwyg.mountcak.data.model

data class Chat(
    val id: String? = null,
    val senderId: String? = null,
    val receiverId: String? = null,
    val msgContent: String? = null,
    val timeStamp: Long? = null,
    val read: Boolean? = false,
    val joinMsg: Boolean? = false,
    val joinId: String? = null,
    val eventId: String? = null
)