package com.wysiwyg.mountcak.ui.eventdetail

import android.graphics.Color
import android.text.SpannableString
import android.widget.TextView
import com.wysiwyg.mountcak.util.color
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.*
import com.wysiwyg.mountcak.util.DateUtil.dateFormat
import com.wysiwyg.mountcak.util.DateUtil.dateToLong
import com.wysiwyg.mountcak.util.DateUtil.locale
import org.jetbrains.anko.textResource
import com.wysiwyg.mountcak.util.spannable
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class EventDetailPresenter(private val view: EventDetailView, private val eid: String) {

    private val db = FirebaseDatabase.getInstance().reference
    private val user = FirebaseAuth.getInstance().currentUser!!.uid
    private var participant: Int? = null

    private fun checkSender(event: Event) {
        if (event.userId == user) view.showOwnPost(event)
        else view.isNotOwnPost(event.userId)
    }

    private val eventListener = object : ValueEventListener {
        override fun onCancelled(p0: DatabaseError) {

        }

        override fun onDataChange(p0: DataSnapshot) {
            try {
                val event = p0.getValue(Event::class.java)
                view.hideLoading()
                view.showEventDetail(event)

                mountData(event?.mountId!!)
                userData(event.userId!!)
                checkSender(event)
                checkIsJoin(event.dateStart!!, event.joinedParticipant!!, event.maxParticipant!!)
                getJoinedId()
                participant = event.joinedParticipant
            } catch (ex: Exception) {
                ex.printStackTrace()
                view.eventNotFound()
            }
        }
    }

    private fun mountData(id: String) {
        db.child("mount").child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val mount = p0.getValue(Mount::class.java)
                    view.showMountData(mount)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun userData(id: String) {
        db.child("user").child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val user = p0.getValue(User::class.java)
                    view.showUserData(user)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun checkIsJoin(dateStart: String, par: Int, maxPar: Int) {
        db.child("join").child(eid).orderByChild("userReqId").equalTo(user)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        var status: Int? = null
                        if (p0.exists()) {
                            var data: Join? = null
                            p0.children.forEach {
                                data = it.getValue(Join::class.java)
                            }

                            when (data?.status) {
                                0 -> view.showDefault()
                                1 -> view.showIsJoined(data?.id)
                                2 -> view.showIsRequested(data?.id)
                            }

                            status = data?.status
                        } else view.showDefault()

                        checkExpire(dateStart, status, par, maxPar)
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }

                override fun onCancelled(p0: DatabaseError) {

                }
            })
    }

    private fun checkExpire(dateStart: String, status: Int?, par: Int, maxPar: Int) {
        val parseDate: Date = SimpleDateFormat("dd/MM/yy", locale).parse(dateStart)
        if (Date().after(parseDate)) view.disableRequest()
        else {
            if (status != 1)
                if (par >= maxPar) view.disableRequest()
        }
    }

    private fun getJoinedId() {
        db.child("join").child(eid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                try {
                    val user = mutableListOf<String?>()
                    p0.children.forEach {
                        val data = it.getValue(Join::class.java)
                        if (data?.status == 1)
                            user.add(data.userReqId)
                    }
                    getJoinedPict(user)
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun getJoinedPict(uid: MutableList<String?>) {
        val user = mutableListOf<User?>()
        if (uid.size > 0) {
            uid.forEach {
                db.child("user").child(it!!).addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(p0: DataSnapshot) {
                        try {
                            val data = p0.getValue(User::class.java)
                            user.add(data)
                            view.showJoinedUser(user)
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }

                    override fun onCancelled(p0: DatabaseError) {

                    }
                })
            }
        }
    }

    fun getEventDetail() {
        view.showLoading()
        db.child("event").child(eid).addValueEventListener(eventListener)
    }

    fun checkDate(dateStart: String?, dateEnd: String?): String {
        return if (dateStart.equals(dateEnd))
            dateFormat(dateEnd, "EEEE, dd MMMM yyyy")
        else
            dateFormat(dateStart, "EEEE, dd MMMM") + " - " + dateFormat(dateEnd, "EEEE, dd MMMM yyyy")
    }

    fun checkDayLeft(dateStart: String?): SpannableString {
        val diff = dateToLong(dateStart!!) - System.currentTimeMillis()
        val day = (diff / (24 * 60 * 60 * 1000)).toInt()
        val parseDate: Date = SimpleDateFormat("dd/MM/yy", locale).parse(dateStart)

        var span: SpannableString = spannable { color(Color.parseColor("#808080"), "${day + 1} Days Left") }
        if (Date().after(parseDate)) span = spannable { color(Color.RED, "Trip Has Ended") }
        return span
    }

    fun checkParticipant(joined: Int?, maxPar: Int?): SpannableString {
        val remain = maxPar!! - joined!!
        return if (joined >= maxPar) spannable { color(Color.RED, "$joined People Joined, Quota Full") }
        else spannable { color(Color.parseColor("#808080"), "$joined People Joined, $remain Place Left") }
    }

    fun checkCost(cost: Int?, tv: TextView) {
        return if (cost == 0) {
            tv.textResource = R.string.free
        } else {
            val local = Locale("in", "ID")
            val currency = NumberFormat.getCurrencyInstance(local)
            tv.text = currency.format(cost).toString()
        }
    }

    fun requestJoin(uid: String?, mountName: String?) {
        try {
            val ts = System.currentTimeMillis()
            val msgContent =
                "Hello, I want to join your trip to $mountName. Please respond my request immediately. Thank you."
            val joinId = db.child("join").push().key!!

            db.child("join").child(eid).child(joinId).setValue(Join(joinId, eid, uid, user, 2, ts, ts))
                .addOnSuccessListener {
                    val chatId = db.child("chat").child(user).child(uid!!).push().key!!
                    val chat = Chat(chatId, user, uid, msgContent, ts,
                        read = false,
                        joinMsg = true,
                        joinId = joinId,
                        eventId = eid
                    )

                    db.child("chat").child(user).child(uid).child(chatId).setValue(chat)
                    db.child("chat").child(uid).child(user).child(chatId).setValue(chat)
                        .addOnSuccessListener {
                            view.requestSuccess(uid)
                        }

                }
                .addOnFailureListener {
                    view.requestFailed()
                }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun cancelRequest(joinId: String?) {
        try {
            db.child("join").child(eid).child(joinId!!).removeValue()
        } finally { }
    }

    fun cancelJoin(joinId: String?) {
        cancelRequest(joinId)
        val newPar = participant?.minus(1)
        try {
            db.child("event").child(eid).child("joinedParticipant").setValue(newPar)
        } finally { }
    }

    fun deletePost() {
        try {
            db.child("event").child(eid).removeValue().addOnSuccessListener {
                view.eventNotFound()
                db.child("join").child(eid).removeValue()
            }
        } finally { }
    }

    fun addToCalendar(event: Event?) {
        val start = dateFormat(event?.dateStart, "yyyy/MM/dd").split("/")
        val dateStart = GregorianCalendar(start[0].toInt(), start[1].toInt()-1, start[2].toInt())

        val end = dateFormat(event?.dateEnd, "yyyy/MM/dd").split("/")
        val dateEnd = GregorianCalendar(end[0].toInt(), end[1].toInt()-1, end[2].toInt()+1)

        view.addToCalendar(event, dateStart, dateEnd)
    }

    fun onClose() {
        db.child("event").child(eid).removeEventListener(eventListener)
    }
}