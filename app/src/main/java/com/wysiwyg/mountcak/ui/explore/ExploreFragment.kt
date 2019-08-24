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

class ExploreFragment : Fragment(), ExploreView, RegionView {

    private lateinit var presenter: ExplorePresenter
    private lateinit var adapter: MountAdapter
    private lateinit var regionAdapter: RegionAdapter
    private val mountList = mutableListOf<Mount?>()
    private val regionList = mutableListOf<String?>()

    override fun showLoading() {
        srlExplore.isRefreshing = true
    }

    override fun hideLoading() {
        srlExplore.isRefreshing = false
    }

    override fun showRegionList(region: List<String?>) {
        regionList.clear()
        regionList.addAll(region.distinct())
        regionAdapter.notifyDataSetChanged()
    }

    override fun showMountList(mount: List<Mount?>) {
        mountList.clear()
        mountList.addAll(mount)
        adapter.notifyDataSetChanged()
    }

    override fun onRegionChange(region: String) {
        if (regionAdapter.regionPos == 0)
            presenter.getData()
        else
            presenter.filterData(region)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_explore, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbarExplore)

        presenter = ExplorePresenter(this)
        presenter.getData()

        setupRecyclerView()
        srlExplore.onRefresh { presenter.getData() }

    }

    private fun setupRecyclerView() {
        adapter = MountAdapter(mountList)
        rvMount.setHasFixedSize(true)
        rvMount.layoutManager = LinearLayoutManager(context)
        rvMount.adapter = adapter

        regionAdapter = RegionAdapter(regionList, this)
        rvRegion.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvRegion.adapter = regionAdapter
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