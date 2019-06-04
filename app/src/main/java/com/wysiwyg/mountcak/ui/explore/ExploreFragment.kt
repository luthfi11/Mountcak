package com.wysiwyg.mountcak.ui.explore

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.fragment_explore.*

class ExploreFragment : Fragment(), ExploreView {

    private lateinit var presenter: ExplorePresenter
    private lateinit var adapter: MountAdapter
    private val mount: MutableList<Mount?> = mutableListOf()

    override fun showLoading() {
        progressExplore.visible()
        rvHome.gone()
    }

    override fun hideLoading() {
        progressExplore.gone()
        rvHome.visible()
    }

    override fun showMountList(mount: MutableList<Mount?>) {
        this.mount.clear()
        this.mount.addAll(mount)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_explore, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbarExplore)
        setHasOptionsMenu(true)

        adapter = MountAdapter(mount)
        rvHome.setHasFixedSize(true)
        rvHome.layoutManager = LinearLayoutManager(context)
        rvHome.adapter = adapter

        presenter = ExplorePresenter(this)
        presenter.getData()
    }
}