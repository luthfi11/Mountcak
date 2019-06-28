package com.wysiwyg.mountcak.ui.notification

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Join
import com.wysiwyg.mountcak.data.model.User
import com.wysiwyg.mountcak.ui.eventdetail.EventDetailActivity
import com.wysiwyg.mountcak.ui.userdetail.UserDetailActivity
import com.wysiwyg.mountcak.util.DateUtil
import kotlinx.android.synthetic.main.item_notification.view.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.startActivity

class NotificationAdapter(private val joins: MutableList<Join?>) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(p0.context).inflate(R.layout.item_notification, p0, false))
    }

    override fun getItemCount(): Int {
        return joins.size
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.bindItem(joins[p1])
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        fun bindItem(join: Join?) {
            getNotifData(join?.postSender!!, join.eventId!!, join.status)
            itemView.tvTimeNotif.text = DateUtil.timeFormat("HH:mm", join.confirmTime!!)
            itemView.imgUserNotif.onClick { itemView.context.startActivity<UserDetailActivity>("userId" to join.postSender) }
            itemView.lytNotif.onClick { itemView.context.startActivity<EventDetailActivity>("eid" to join.eventId) }
        }

        private fun checkStatus(stat: Int?): String {
            return if (stat == 0) "Declined"
            else "Accepted"
        }

        private fun getNotifData(sender: String, eid: String, stat: Int?) {
            try {
                var name: String?
                var mount: String?
                val db = FirebaseDatabase.getInstance().reference
                db.child("user").child(sender).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        try {
                            val user = p0.getValue(User::class.java)
                            Glide.with(itemView.context).load(user?.photo).into(itemView.imgUserNotif)
                            name = user?.name

                            db.child("event").child(eid).child("mountId")
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(p0: DataSnapshot) {
                                        try {
                                            if (p0.exists()) {
                                                val mountId = p0.getValue(String::class.java)
                                                db.child("mount").child(mountId!!).child("mountName")
                                                    .addValueEventListener(object : ValueEventListener {
                                                        override fun onDataChange(p0: DataSnapshot) {
                                                            try {
                                                                mount = p0.getValue(String::class.java)
                                                                val status = String.format(
                                                                    itemView.context.getString(R.string.notification_title),
                                                                    name,
                                                                    checkStatus(stat),
                                                                    mount
                                                                )

                                                                itemView.tvNotification.text =
                                                                    setBlackSpannable(status, name)
                                                            } finally {
                                                            }
                                                        }

                                                        override fun onCancelled(p0: DatabaseError) {

                                                        }
                                                    })
                                            }
                                        } finally {
                                        }
                                    }

                                    override fun onCancelled(p0: DatabaseError) {

                                    }
                                })
                        } finally {
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })

            } catch (ex: Exception) {
                ex.printStackTrace()
            }

        }

        private fun setBlackSpannable(text: String, span: String?): SpannableString {
            val spannableContent = SpannableString(text)
            spannableContent.setSpan(
                ForegroundColorSpan(ContextCompat.getColor(itemView.context, android.R.color.black)),
                0,
                span!!.length,
                Spannable.SPAN_INCLUSIVE_INCLUSIVE
            )
            return spannableContent
        }
    }
}