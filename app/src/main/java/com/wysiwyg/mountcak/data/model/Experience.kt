package com.wysiwyg.mountcak.data.model

data class Experience (
    val id: String? = null,
    val userId: String? = null,
    val mountId: String? = null,
    val experience: String? = null,
    val timeStamp: Long? = System.currentTimeMillis()
)