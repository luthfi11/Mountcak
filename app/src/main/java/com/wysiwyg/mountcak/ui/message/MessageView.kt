package com.wysiwyg.mountcak.ui.message

import com.wysiwyg.mountcak.data.model.Chat

interface MessageView {
    fun showLoading()
    fun hideLoading()
    fun showMessageList(chat: List<Chat?>)
    fun emptyMessage()
    fun notEmptyMessage()
}