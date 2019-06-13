package com.wysiwyg.mountcak.ui.message

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.data.model.Chat

class MessagePresenter(private val view: MessageView) {

    private val uid = FirebaseAuth.getInstance().currentUser?.uid!!
    private val db = FirebaseDatabase.getInstance().reference.child("chat").child(uid)

    private val messageListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(chatSnapshot: DataSnapshot) {
            try {
                view.hideLoading()
                val chatList: MutableList<Chat?> = mutableListOf()
                var chat: Chat? = null
                for (dt: DataSnapshot in chatSnapshot.children) {
                    for (dt2: DataSnapshot in dt.children) {
                        chat = dt2.getValue(Chat::class.java)
                    }
                    chatList.add(chat)
                }

                chatList.sortBy { it?.timeStamp }
                view.showMessageList(chatList)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun getMessageList() {
        view.showLoading()
        db.addValueEventListener(messageListener)
    }
}