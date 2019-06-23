package com.wysiwyg.mountcak.ui.chatroom

import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Chat
import com.wysiwyg.mountcak.ui.userdetail.UserDetailActivity
import com.wysiwyg.mountcak.util.ValidateUtil.etToString
import kotlinx.android.synthetic.main.activity_chat_room.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class ChatRoomActivity : AppCompatActivity(), ChatRoomView, View.OnClickListener, TextWatcher {

    private lateinit var presenter: ChatRoomPresenter
    private lateinit var adapter: ChatRoomAdapter
    private val chat: MutableList<Chat?> = mutableListOf()
    private lateinit var friendId: String

    override fun showChat(chat: List<Chat?>) {
        this.chat.clear()
        this.chat.addAll(chat)
        adapter.notifyDataSetChanged()
        rvChat.scrollToPosition(chat.size-1)
    }

    override fun sendSuccess() {
        etMessageText.text = null
        rvChat.scrollToPosition(chat.size-1)
    }

    override fun sendFailed() {
        toast("Sending message failed, Please try again !")
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        if (etMessageText.text.isNotEmpty()) buttonEnable(true, R.color.colorPrimary)
        else buttonEnable(false, R.color.colorMuted)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

    }

    override fun afterTextChanged(s: Editable?) {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)
        setSupportActionBar(toolbarChat)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = ChatRoomAdapter(chat)

        rvChat.setHasFixedSize(true)
        rvChat.layoutManager = LinearLayoutManager(this)
        rvChat.adapter = adapter

        friendId = intent.getStringExtra("userId")
        presenter = ChatRoomPresenter(this, friendId)
        presenter.getFriendName(this, tvFriendName)
        presenter.getChatList()
        presenter.setRead()

        buttonEnable(false, R.color.colorMuted)
        etMessageText.addTextChangedListener(this)
    }

    private fun buttonEnable(ena: Boolean, color: Int) {
        btnSend.setColorFilter(ContextCompat.getColor(this, color))
        btnSend.isEnabled = ena
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnSend -> presenter.sendMessage(etToString(etMessageText))
            R.id.toolbarChat -> startActivity<UserDetailActivity>("userId" to friendId)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onClose()
    }
}
