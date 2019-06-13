package com.wysiwyg.mountcak.ui.message

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Chat
import kotlinx.android.synthetic.main.fragment_message.*
import org.jetbrains.anko.support.v4.onRefresh

class MessageFragment : Fragment(), MessageView {

    private lateinit var presenter: MessagePresenter
    private lateinit var adapter: MessageAdapter
    private val message: MutableList<Chat?> = mutableListOf()

    override fun showLoading() {
        srlMessage.isRefreshing = true
    }

    override fun hideLoading() {
        srlMessage.isRefreshing = false
    }

    override fun showMessageList(chat: List<Chat?>) {
        this.message.clear()
        this.message.addAll(chat)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = MessageAdapter(message)

        rvMessage.setHasFixedSize(true)
        rvMessage.layoutManager = LinearLayoutManager(context)
        rvMessage.adapter = adapter

        presenter = MessagePresenter(this)
        presenter.getMessageList()

        srlMessage.onRefresh { presenter.getMessageList() }
    }

}
