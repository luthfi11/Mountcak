package com.wysiwyg.mountcak.ui.chatroom

import android.content.Context
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.wysiwyg.mountcak.data.model.Chat
import com.wysiwyg.mountcak.util.FirebaseUtil

class ChatRoomPresenter(private val view: ChatRoomView, private val friendId: String) {

    private val db = FirebaseDatabase.getInstance().reference.child("chat")
    private val uid = FirebaseAuth.getInstance().currentUser?.uid
    private val chatRef = db.child(uid!!)

    private val chatListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            try {
                val chat: MutableList<Chat?> = mutableListOf()
                for (data: DataSnapshot in p0.children) {
                    val c = data.getValue(Chat::class.java)
                    chat.add(c)
                }
                view.showChat(chat)
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    private lateinit var readRef: DatabaseReference
    private val readListener = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            try {
                for (data: DataSnapshot in p0.children) {
                    val id = data.child("senderId").getValue(String::class.java)
                    if (id != uid) {
                        val msgId = data.child("id").getValue(String::class.java)!!
                        readRef.child(msgId).child("read").setValue(true)
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    private lateinit var readRefFriend: DatabaseReference
    private val readListenerFriend = object : ValueEventListener {
        override fun onDataChange(p0: DataSnapshot) {
            try {
                for (data: DataSnapshot in p0.children) {
                    val id = data.child("receiverId").getValue(String::class.java)
                    if (id == uid) {
                        val msgId = data.child("id").getValue(String::class.java)!!
                        readRefFriend.child(msgId).child("read").setValue(true)
                    }
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }

        override fun onCancelled(p0: DatabaseError) {

        }
    }

    fun getFriendName(ctx: Context, tv: TextView) {
        FirebaseUtil.getUserData(ctx, friendId, tv, null)
    }

    fun getChatList() {
        chatRef.child(friendId).addValueEventListener(chatListener)
    }

    fun sendMessage(message: String) {
        try {
            val chatId = db.push().key
            val chat = Chat(chatId, uid, friendId, message, System.currentTimeMillis())

            chatRef.child(friendId).child(chatId!!).setValue(chat)
                .addOnSuccessListener {
                    view.sendSuccess()
                }
                .addOnFailureListener {
                    view.sendFailed()
                }

            db.child(friendId).child(uid!!).child(chatId).setValue(chat)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun setRead() {
        readRef = chatRef.child(friendId)
        readRef.addValueEventListener(readListener)

        readRefFriend = db.child(friendId).child(uid!!)
        readRefFriend.addValueEventListener(readListenerFriend)
    }

    fun onClose() {
        chatRef.child(friendId).removeEventListener(chatListener)
        chatRef.child(friendId).removeEventListener(readListener)
        db.child(friendId).child(uid!!).removeEventListener(readListenerFriend)
    }
}