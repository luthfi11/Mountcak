package com.wysiwyg.mountcak.ui.message

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Chat
import com.wysiwyg.mountcak.util.gone
import com.wysiwyg.mountcak.util.visible
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

    override fun emptyMessage() {
        rvMessage.gone()
        tvEmpty.visible()
    }

    override fun notEmptyMessage() {
        rvMessage.visible()
        tvEmpty.gone()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        presenter = MessagePresenter(this)
        presenter.getMessageList()

        setupRecyclerView()
        srlMessage.onRefresh { presenter.getMessageList() }
    }

    private fun setupRecyclerView() {
        adapter = MessageAdapter(message)
        rvMessage.setHasFixedSize(true)
        rvMessage.layoutManager = LinearLayoutManager(context)
        rvMessage.adapter = adapter
    }
}
