package com.wysiwyg.mountcak.ui.chatroom

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.bigbox.APIClient
import com.wysiwyg.mountcak.bigbox.APIService
import com.wysiwyg.mountcak.data.model.Chat
import com.wysiwyg.mountcak.data.model.Join
import com.wysiwyg.mountcak.data.model.SMSResponse
import com.wysiwyg.mountcak.data.model.User
import com.wysiwyg.mountcak.util.DateUtil
import com.wysiwyg.mountcak.util.DateUtil.dayAgo
import com.wysiwyg.mountcak.util.FirebaseUtil
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.invisible
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.item_chat_room.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.selector
import org.jetbrains.anko.textColorResource
import org.jetbrains.anko.textResource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class ChatRoomAdapter(private val chats: MutableList<Chat?>) :
    RecyclerView.Adapter<ChatRoomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(
            LayoutInflater.from(p0.context).inflate(
                R.layout.item_chat_room,
                p0,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return chats.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        if (p1 > 1) {
            if (dayAgo(chats[p1 - 1]?.timeStamp!!) != dayAgo(chats[p1]?.timeStamp!!)) p0.showTime(
                chats[p1]
            )
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
                getEventData(chat.eventId)
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
                    FirebaseUtil.getUserData(
                        itemView.context,
                        chat.senderId!!,
                        null,
                        itemView.imgSenderJoin
                    )
                    itemView.tvJoinMsg.text = chat.msgContent
                    itemView.tvJoinTime.text = DateUtil.timeFormat("HH:mm", chat.timeStamp!!)
                    itemView.btnAccept.onClick { confirmReq(chat.eventId, chat.joinId, 1) }
                    itemView.btnDecline.onClick { confirmReq(chat.eventId, chat.joinId, 0) }

                    getUserData(chat.senderId)
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
                    FirebaseUtil.getUserData(
                        itemView.context,
                        chat?.senderId!!,
                        null,
                        itemView.imgSender
                    )
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

        private var title: String? = null
        private var maxPar: Int? = null
        private var participant: Int? = null
        private var eventDate: String? = null
        private fun getEventData(eid: String?) {
            val db = FirebaseDatabase.getInstance().reference
            db.child("event").child(eid!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        title = p0.child("title").getValue(String::class.java)
                        maxPar = p0.child("maxParticipant").getValue(Int::class.java)
                        participant = p0.child("joinedParticipant").getValue(Int::class.java)
                        eventDate = p0.child("dateStart").getValue(String::class.java)

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }

        private var friendName: String? = null
        private var phone: String? = null
        fun getUserData(uid: String?) {
            val db = FirebaseDatabase.getInstance().reference
            db.child("user").child(uid!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        val data = p0.getValue(User::class.java)
                        friendName = data?.name
                        phone = data?.phone

                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
        }

        private fun confirmReq(eid: String?, joinId: String?, stat: Int) {
            val db = FirebaseDatabase.getInstance().reference
            db.child("join").child(eid!!).child(joinId!!).child("status").setValue(stat)
            db.child("join").child(eid).child(joinId).child("confirmTime")
                .setValue(System.currentTimeMillis())

            var smsContent = ""
            if (stat == 1) {
                db.child("event").child(eid).child("joinedParticipant")
                    .setValue(participant?.plus(1))
                smsContent =
                    "Hallo $friendName, permintaan anda untuk bergabung dengan acara \"$title\" telah disetujui. Silahkan buka aplikasi Mountcak anda, Terima kasih."
            } else {
                smsContent =
                    "Hallo $friendName, permintaan anda untuk bergabung dengan acara \"$title\" telah ditolak. Terima kasih."
            }

            /*
            val apiKey = "ig1RMz09vsjWSlIp7cx5fuNjoxrefqqR"
            val apiInterface: APIService = APIClient.getClient().create(APIService::class.java)
            if (phone != null) {
                val send = apiInterface.postSMS(apiKey, phone!!, smsContent)
                send.enqueue(object : Callback<SMSResponse> {
                    override fun onFailure(call: Call<SMSResponse>?, t: Throwable?) {
                        Log.d("AAAAAAAAAAAAAA", "GAGAL")
                    }

                    override fun onResponse(
                        call: Call<SMSResponse>?,
                        response: Response<SMSResponse>?
                    ) {
                        Log.d("AAAAAAAAAAAAAA", "${response?.body()?.message}")
                    }

                })
            }
             */
        }

        private fun cancelRequest(eid: String?, joinId: String?) {
            val db = FirebaseDatabase.getInstance().reference
            db.child("join").child(eid!!).child(joinId!!).removeValue()
        }

        private fun checkJoin(eid: String?, joinId: String?) {
            val db = FirebaseDatabase.getInstance().reference
            db.child("join").child(eid!!).child(joinId!!)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        try {
                            if (p0.exists()) {
                                val parseDate: Date =
                                    SimpleDateFormat("dd/MM/yy", Locale.getDefault()).parse(
                                        eventDate
                                    )
                                if (Date().after(parseDate)) showConfirm(
                                    R.string.expired,
                                    android.R.color.darker_gray
                                )
                                else {
                                    if (participant!! >= maxPar!!) showConfirm(
                                        R.string.waiting_list,
                                        android.R.color.darker_gray
                                    )
                                    else {
                                        val data = p0.getValue(Join::class.java)
                                        when (data?.status) {
                                            0 -> showConfirm(
                                                R.string.declined,
                                                android.R.color.holo_red_light
                                            )
                                            1 -> showConfirm(
                                                R.string.accepted,
                                                R.color.colorPrimary
                                            )
                                            2 -> showDefault()
                                        }
                                    }
                                }

                            } else showConfirm(R.string.cancelled, android.R.color.darker_gray)

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

        private fun showDefault() {
            itemView.btnCancelMe.visible()
            itemView.btnAccept.visible()
            itemView.btnDecline.visible()
            itemView.tvConfirmed.gone()
            itemView.tvConfirmedMe.gone()
        }

        private fun showConfirm(text: Int, color: Int) {
            itemView.btnCancelMe.gone()
            itemView.btnAccept.gone()
            itemView.btnDecline.gone()
            itemView.tvConfirmed.visible()
            itemView.tvConfirmedMe.visible()
            itemView.tvConfirmed.textResource = text
            itemView.tvConfirmedMe.textResource = text
            itemView.tvConfirmed.textColorResource = color
            itemView.tvConfirmedMe.textColorResource = color
        }
    }
}