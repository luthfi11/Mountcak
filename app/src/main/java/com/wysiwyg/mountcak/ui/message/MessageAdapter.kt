package com.wysiwyg.mountcak.ui.message

import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import android.text.format.DateUtils.isToday
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.FirebaseDatabase
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Chat
import com.wysiwyg.mountcak.ui.chatroom.ChatRoomActivity
import com.wysiwyg.mountcak.ui.userdetail.UserDetailActivity
import com.wysiwyg.mountcak.util.DateUtil.dayAgo
import com.wysiwyg.mountcak.util.DateUtil.isYesterday
import com.wysiwyg.mountcak.util.DateUtil.timeFormat
import com.wysiwyg.mountcak.util.FirebaseUtil
import kotlinx.android.synthetic.main.item_message.view.*
import org.jetbrains.anko.*

class MessageAdapter(private val msg: MutableList<Chat?>) : RecyclerView.Adapter<MessageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_message, p0, false))
    }

    override fun getItemCount(): Int {
        return msg.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindItem(msg[p1])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(message: Chat?) {
            var uid = message?.receiverId
            if (uid == FirebaseUtil.currentUser()) uid = message?.senderId

            when {
                isToday(message?.timeStamp!!) -> itemView.tvTimeMsg.text = timeFormat("HH:mm", message.timeStamp)
                isYesterday(message.timeStamp) -> itemView.tvTimeMsg.text = dayAgo(message.timeStamp)
                else -> itemView.tvTimeMsg.text = timeFormat("dd/MM/yy", message.timeStamp)
            }

            FirebaseUtil.getUserData(itemView.context, uid, itemView.tvUserMsg, itemView.imgUserMsg)

            itemView.tvContentMsg.text = message.msgContent
            itemView.setOnClickListener { itemView.context.startActivity<ChatRoomActivity>("userId" to uid) }

            itemView.setOnLongClickListener {
                showMenu(uid)
                true
            }

            if (message.read == false && message.senderId != FirebaseUtil.currentUser()) readFont(Typeface.DEFAULT_BOLD)
            else readFont(Typeface.DEFAULT)
        }

        private fun showMenu(uid: String?) {
            val menu = listOf("View Profile", "Chat", "Delete")
            itemView.context.selector(itemView.tvUserMsg.text, menu) { _, i ->
                when (i) {
                    0 -> itemView.context.startActivity<UserDetailActivity>("userId" to uid)
                    1 -> itemView.context.startActivity<ChatRoomActivity>("userId" to uid)
                    2 -> showConfirmDelete(uid!!)
                }
            }
        }

        private fun showConfirmDelete(uid: String) {
            itemView.context.alert(itemView.context.getString(R.string.delete_chat)) {
                yesButton {
                    try {
                        FirebaseDatabase.getInstance().reference
                            .child("chat").child(FirebaseUtil.currentUser()).child(uid).removeValue()
                    } catch (ex: Exception) { ex.printStackTrace() }
                }
                noButton { it.dismiss() }
            }.show()
        }

        private fun readFont(font: Typeface) {
            itemView.tvUserMsg.typeface = font
            itemView.tvTimeMsg.typeface = font
            itemView.tvContentMsg.typeface = font
        }
    }
}