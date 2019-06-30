package com.wysiwyg.mountcak.ui.rental

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Rental
import com.wysiwyg.mountcak.ui.search.SearchActivity
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.fragment_rental.*
import org.jetbrains.anko.support.v4.onRefresh
import org.jetbrains.anko.support.v4.startActivity

class RentalFragment : Fragment(), RentalView {

    private lateinit var presenter: RentalPresenter
    private lateinit var adapter: RentalAdapter
    private val rental = mutableListOf<Rental?>()

    override fun showLoading() {
        srlRental.isRefreshing = true
    }

    override fun hideLoading() {
        srlRental.isRefreshing = false
    }

    override fun showData(rental: List<Rental?>) {
        this.rental.clear()
        this.rental.addAll(rental)
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_rental, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbarRental)

        adapter = RentalAdapter(rental)
        rvRental.setHasFixedSize(true)
        rvRental.layoutManager = LinearLayoutManager(context)
        rvRental.adapter = adapter

        presenter = RentalPresenter(this)
        presenter.getRentalStore()

        srlRental.onRefresh { presenter.getRentalStore() }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.menu_search, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.nav_search) startActivity<SearchActivity>("type" to 2)
        return super.onOptionsItemSelected(item)
    }
}
