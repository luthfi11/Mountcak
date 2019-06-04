package com.wysiwyg.mountcak.ui.profile

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.bumptech.glide.Glide

import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.User
import com.wysiwyg.mountcak.ui.home.EventAdapter
import com.wysiwyg.mountcak.ui.login.LoginFragmentManager
import com.wysiwyg.mountcak.ui.viewphoto.ViewPhotoActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.startActivity

class ProfileFragment : Fragment(), ProfileView {

    private lateinit var presenter: ProfilePresenter
    private lateinit var adapter: EventAdapter
    private val event: MutableList<Event?> = mutableListOf()

    override fun showLoading() {

    }

    override fun hideLoading() {

    }

    override fun showData(user: User?) {
        Glide.with(this).load(user?.photo).placeholder(R.color.colorMuted).into(imgAva)
        tvName.text = user?.name
        tvEmail.text = user?.email
        tvCity.text = user?.city

        imgAva.onClick { startActivity<ViewPhotoActivity>("photo" to user?.photo) }
    }

    override fun showEvent(event: MutableList<Event?>) {
        this.event.clear()
        this.event.addAll(event)
        adapter.notifyDataSetChanged()
    }

    override fun doLogout() {
        startActivity<LoginFragmentManager>()
        activity?.finish()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbarProfile)
        setHasOptionsMenu(true)

        adapter = EventAdapter(event)

        rvUserEvent.setHasFixedSize(true)
        rvUserEvent.layoutManager = LinearLayoutManager(context)
        rvUserEvent.adapter = adapter

        presenter = ProfilePresenter(this)
        presenter.getUserData()
        presenter.getUserPost()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_profile, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.nav_about -> {
                true
            }
            R.id.nav_report -> {
                true
            }
            R.id.nav_logout -> {
                presenter.logout()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
