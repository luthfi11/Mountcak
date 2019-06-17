package com.wysiwyg.mountcak.ui.chatroom

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Chat
import com.wysiwyg.mountcak.data.model.Join
import com.wysiwyg.mountcak.util.DateUtil
import com.wysiwyg.mountcak.util.DateUtil.dayAgo
import com.wysiwyg.mountcak.util.FirebaseUtil
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.invisible
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.item_chat_room.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.selector

class ChatRoomAdapter(private val chats: MutableList<Chat?>) : RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_chat_room, p0, false))
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        if (p1 > 0) {
            if (dayAgo(chats[p1 - 1]?.timeStamp!!) != dayAgo(chats[p1]?.timeStamp!!)) p0.showTime(chats[p1])
            else p0.hideTime()
        }

        if ((p1 == 0) && (chats.size != 0)) p0.showTime(chats[0])

        p0.bindItem(chats[p1])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(chat: Chat?) {
            if (chat?.joinMsg == true) {
                itemView.viewOutgoing.gone()
                itemView.viewIncoming.gone()
                checkJoin(chat.eventId, chat.joinId)
                if (chat.senderId == FirebaseUtil.currentUser()) {
                    itemView.viewJoin.gone()
                    itemView.viewJoinMe.visible()
                    itemView.tvJoinMsgMe.text = chat.msgContent
                    itemView.tvJoinTimeMe.text = DateUtil.timeFormat("HH:mm", chat.timeStamp!!)
                    itemView.btnCancelMe.onClick { cancelRequest(chat.eventId, chat.joinId) }

                    if (chat.read == true) itemView.tvReadJoin.visible()
                    else itemView.tvReadJoin.invisible()
                } else {
                    itemView.viewJoin.visible()
                    itemView.viewJoinMe.gone()
                    itemView.tvJoinMsg.text = chat.msgContent
                    itemView.tvJoinTime.text = DateUtil.timeFormat("HH:mm", chat.timeStamp!!)
                    itemView.btnAccept.onClick { confirmReq(chat.eventId, chat.joinId, 1) }
                    itemView.btnDecline.onClick { confirmReq(chat.eventId, chat.joinId, 0) }
                }

            } else {
                if (chat?.senderId == FirebaseUtil.currentUser()) {
                    itemView.tvMessageOut.text = chat.msgContent
                    itemView.tvSentTime.text = DateUtil.timeFormat("HH:mm", chat.timeStamp!!)
                    itemView.viewJoin.gone()
                    itemView.viewJoinMe.gone()
                    itemView.viewOutgoing.visible()
                    itemView.viewIncoming.gone()
                    itemView.tvMessageOut.copyToClipboard(chat.msgContent)

                    if (chat.read == true) itemView.tvRead.visible()
                    else itemView.tvRead.invisible()

                } else {
                    FirebaseUtil.getUserData(itemView.context, chat?.senderId!!, null, itemView.imgSender)
                    itemView.tvMessageIn.text = chat.msgContent
                    itemView.tvReceiveTime.text = DateUtil.timeFormat("HH:mm", chat.timeStamp!!)
                    itemView.viewJoin.gone()
                    itemView.viewJoinMe.gone()
                    itemView.viewOutgoing.gone()
                    itemView.viewIncoming.visible()
                    itemView.tvMessageIn.copyToClipboard(chat.msgContent)
                }
            }
        }

        fun showTime(chat: Chat?) {
            itemView.tvTime.visible()
            itemView.tvTime.text = dayAgo(chat?.timeStamp!!)
        }

        fun hideTime() {
            itemView.tvTime.gone()
        }

        private fun confirmReq(eid: String?, joinId: String?, stat: Int) {
            val db = FirebaseDatabase.getInstance().reference
            db.child("join").child(eid!!).child(joinId!!).child("status").setValue(stat)
        }

        private fun cancelRequest(eid: String?, joinId: String?) {
            val db = FirebaseDatabase.getInstance().reference
            db.child("join").child(eid!!).child(joinId!!).removeValue()
        }

        private fun checkJoin(eid: String?, joinId: String?) {
            val db = FirebaseDatabase.getInstance().reference
            db.child("join").child(eid!!).child(joinId!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        if (p0.exists()) {
                            val data = p0.getValue(Join::class.java)
                            when (data?.status) {
                                0 -> showConfirm(R.string.declined)
                                1 -> showConfirm(R.string.accepted)
                            }
                        } else showConfirm(R.string.cancelled)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }

        private fun View.copyToClipboard(msg: String?) {
            setOnLongClickListener {
                val menu = listOf("Copy Text")
                itemView.context.selector(null, menu) { _, i ->
                    when (i) {
                        0 -> {
                            val clipboard =
                                itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("", msg)
                            clipboard.primaryClip = clip
                        }
                    }
                }
                true
            }
        }

        private fun showConfirm(text: Int) {
            itemView.btnCancelMe.gone()
            itemView.btnAccept.gone()
            itemView.btnDecline.gone()
            itemView.tvConfirmed.visible()
            itemView.tvConfirmedMe.visible()
            itemView.tvConfirmed.text = itemView.context.getString(text)
            itemView.tvConfirmedMe.text = itemView.context.getString(text)
        }
    }
}