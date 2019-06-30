package com.wysiwyg.mountcak.ui.notification

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Join
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.fragment_notification.*
import org.jetbrains.anko.support.v4.onRefresh

class NotificationFragment : Fragment(), NotificationView {

    private lateinit var presenter: NotificationPresenter
    private lateinit var adapter: NotificationAdapter
    private val join: MutableList<Join?> = mutableListOf()

    override fun showLoading() {
        srlNotif.isRefreshing = true
    }

    override fun hideLoading() {
        srlNotif.isRefreshing = false
    }

    override fun showNotification(join: List<Join?>) {
        this.join.clear()
        this.join.addAll(join)
        adapter.notifyDataSetChanged()
    }

    override fun emptyNotification() {
        rvNotif.gone()
        tvEmpty.visible()
    }

    override fun notEmptyNotification() {
        rvNotif.visible()
        tvEmpty.gone()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = NotificationAdapter(join)

        rvNotif.setHasFixedSize(true)
        rvNotif.layoutManager = LinearLayoutManager(activity)
        rvNotif.adapter = adapter

        presenter = NotificationPresenter(this)
        presenter.getNotification()

        srlNotif.onRefresh { presenter.getNotification() }
    }

}
