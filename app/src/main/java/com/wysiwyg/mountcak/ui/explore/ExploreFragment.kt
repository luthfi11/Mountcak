package com.wysiwyg.mountcak.ui.explore

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.ui.search.SearchActivity
import kotlinx.android.synthetic.main.fragment_explore.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity

class ExploreFragment : Fragment(), ExploreView {

    private lateinit var presenter: ExplorePresenter
    private lateinit var adapter: MountAdapter
    private val mount: MutableList<Mount?> = mutableListOf()

    override fun showLoading() {
        srlExplore.isRefreshing = true
    }

    override fun hideLoading() {
        srlExplore.isRefreshing = false
    }

    override fun showMountList(mount: List<Mount?>) {
        this.mount.clear()
        this.mount.addAll(mount)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbarExplore)

        adapter = MountAdapter(mount)
        rvHome.setHasFixedSize(true)
        rvHome.layoutManager = LinearLayoutManager(context)
        rvHome.adapter = adapter

        presenter = ExplorePresenter(this)
        presenter.getData()

        srlExplore.onRefresh { presenter.getData() }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.menu_search, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.nav_search) startActivity<SearchActivity>("type" to 1)
        return super.onOptionsItemSelected(item)
    }
}