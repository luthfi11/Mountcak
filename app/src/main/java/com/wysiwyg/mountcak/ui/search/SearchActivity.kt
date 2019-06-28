package com.wysiwyg.mountcak.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import android.view.MenuItem
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.Rental
import com.wysiwyg.mountcak.ui.explore.MountAdapter
import com.wysiwyg.mountcak.ui.rental.RentalAdapter
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : AppCompatActivity(), SearchView, android.widget.SearchView.OnQueryTextListener {

    private lateinit var presenter: SearchPresenter
    private lateinit var mountAdapter: MountAdapter
    private lateinit var rentalAdapter: RentalAdapter
    private val mount: MutableList<Mount?> = mutableListOf()
    private val rental: MutableList<Rental?> = mutableListOf()

    override fun showLoading() {
        progressSearch.visible()
        rvSearch.gone()
    }

    override fun hideLoading() {
        progressSearch.gone()
        rvSearch.visible()
    }

    override fun showMountData(mount: List<Mount?>) {
        rvSearch.adapter = mountAdapter
        this.mount.clear()
        this.mount.addAll(mount)
        mountAdapter.notifyDataSetChanged()
    }

    override fun showRentalData(rental: List<Rental?>) {
        rvSearch.adapter = rentalAdapter
        this.rental.clear()
        this.rental.addAll(rental)
        rentalAdapter.notifyDataSetChanged()
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        presenter.search(p0!!)
        return true
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        presenter.search(p0!!)
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbarSearch)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val type = intent.getIntExtra("type", 1)

        presenter = SearchPresenter(this, type)

        mountAdapter = MountAdapter(mount)
        rentalAdapter = RentalAdapter(rental)

        rvSearch.setHasFixedSize(true)
        rvSearch.layoutManager = LinearLayoutManager(this)

        search.setOnQueryTextListener(this)
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
