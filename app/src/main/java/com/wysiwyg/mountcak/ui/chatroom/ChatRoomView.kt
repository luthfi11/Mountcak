package com.wysiwyg.mountcak.ui.chatroom

import com.wysiwyg.mountcak.data.model.Chat

interface ChatRoomView {
    fun showChat(chat: List<Chat?>)
    fun sendSuccess()
    fun sendFailed()
}