package com.wysiwyg.mountcak.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import com.bumptech.glide.Glide
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.User
import com.wysiwyg.mountcak.ui.editprofile.EditProfileActivity
import com.wysiwyg.mountcak.ui.home.EventAdapter
import com.wysiwyg.mountcak.ui.login.LoginFragmentManager
import com.wysiwyg.mountcak.ui.viewphoto.ViewPhotoActivity
import com.wysiwyg.temanolga.utilities.gone
import com.wysiwyg.temanolga.utilities.visible
import kotlinx.android.synthetic.main.fragment_profile.*
import org.jetbrains.anko.noButton
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.browse
import org.jetbrains.anko.support.v4.startActivity
import org.jetbrains.anko.yesButton

class ProfileFragment : Fragment(), ProfileView {

    private lateinit var presenter: ProfilePresenter
    private lateinit var adapter: EventAdapter
    private val event: MutableList<Event?> = mutableListOf()
    private var user: User? = null

    override fun showLoading() {
        progressProfile.visible()
        rvUserEvent.gone()
    }

    override fun hideLoading() {
        progressProfile.gone()
        rvUserEvent.visible()
    }

    override fun showData(user: User?) {
        this.user = user

        Glide.with(this).load(user?.photo).placeholder(R.color.colorMuted).into(imgAva)
        tvName.text = user?.name
        tvEmail.text = user?.email
        tvCity.text = user?.city

        imgAva.onClick { startActivity<ViewPhotoActivity>("photo" to user?.photo) }
    }

    override fun showEvent(event: List<Event?>) {
        this.event.clear()
        this.event.addAll(event)
        adapter.notifyDataSetChanged()
    }

    override fun doLogout() {
        alert {
            title = "Logout from this account ?"
            yesButton {
                startActivity<LoginFragmentManager>()
                activity?.finish()
            }
            noButton { it.dismiss() }
            isCancelable = false
        }.show()
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

    override fun onResume() {
        super.onResume()
        presenter.getUserData()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        menu?.clear()
        inflater?.inflate(R.menu.menu_profile, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.nav_edit -> {
                startActivity<EditProfileActivity>("user" to user)
                true
            }
            R.id.nav_about -> {
                val mDialogView = LayoutInflater.from(activity).inflate(R.layout.layout_about, null)
                AlertDialog.Builder(context!!)
                    .setView(mDialogView)
                    .show()
                true
            }
            R.id.nav_report -> {
                val i = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:luthfialfarizi98@gmail.com"))
                i.putExtra(Intent.EXTRA_SUBJECT,"Mountcak Feedback")
                startActivity(i)
                true
            }
            R.id.nav_privacy -> {
                browse("https://mountcak-16aa7.web.app/privacy_policy.html")
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
