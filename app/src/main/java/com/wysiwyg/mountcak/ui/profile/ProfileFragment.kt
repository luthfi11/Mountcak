package com.wysiwyg.mountcak.ui.profile

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import bold
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.data.model.Event
import com.wysiwyg.mountcak.data.model.Mount
import com.wysiwyg.mountcak.data.model.User
import com.wysiwyg.mountcak.ui.editprofile.EditProfileActivity
import com.wysiwyg.mountcak.ui.explore.MountAdapter
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
import plus
import size
import spannable

class ProfileFragment : Fragment(), ProfileView, TabLayout.OnTabSelectedListener {

    private lateinit var presenter: ProfilePresenter
    private lateinit var adapter: EventAdapter
    private lateinit var mountAdapter: MountAdapter
    private val event: MutableList<Event?> = mutableListOf()
    private val trip: MutableList<Event?> = mutableListOf()
    private val mount: MutableList<Mount?> = mutableListOf()
    private var user: User? = null

    override fun showLoading() {
        progressProfile.visible()
        rvProfile.gone()
    }

    override fun hideLoading() {
        progressProfile.gone()
        rvProfile.visible()
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

        tabs.getTabAt(0)?.text = spannable { bold(size(1.4F, "${event.size}")) + "\nPost" }
    }

    override fun showTrip(event: List<Event?>) {
        trip.clear()
        trip.addAll(event)
        adapter.notifyDataSetChanged()

        tabs.getTabAt(1)?.text = spannable { bold(size(1.4F, "${event.size}")) + "\nTrip" }
    }

    override fun showMount(mount: List<Mount?>) {
        this.mount.clear()
        this.mount.addAll(mount)
        this.mount.sortBy { it?.mountName }
        mountAdapter.notifyDataSetChanged()

        tabs.getTabAt(2)?.text = spannable { bold(size(1.4F, "${mount.size.plus(0)}")) + "\nFavorite" }
    }

    override fun emptyPost(title: String) {
        rvProfile.gone()
        tvEmpty.visible()
        tvEmpty.text = title
    }

    override fun notEmptyPost() {
        rvProfile.visible()
        tvEmpty.gone()
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

    override fun onTabReselected(p0: TabLayout.Tab?) {

    }

    override fun onTabUnselected(p0: TabLayout.Tab?) {

    }

    override fun onTabSelected(p0: TabLayout.Tab?) {
        when (p0?.position) {
            0 -> {
                adapter = EventAdapter(event)
                rvProfile.adapter = adapter

                if (event.isEmpty()) emptyPost("No Post Yet")
                else notEmptyPost()
            }
            1 -> {
                adapter = EventAdapter(trip)
                rvProfile.adapter = adapter

                if (trip.isEmpty()) emptyPost("No Trip Yet")
                else notEmptyPost()
            }
            2 -> {
                mountAdapter = MountAdapter(mount)
                rvProfile.adapter = mountAdapter

                if (mount.isEmpty()) emptyPost("No Favorite List Yet")
                else notEmptyPost()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(toolbarProfile)

        presenter = ProfilePresenter(this)

        getData()
        setupRecyclerView()

        tabs.addOnTabSelectedListener(this)
        btnEditProfile.onClick { startActivity<EditProfileActivity>("user" to user) }
    }

    private fun getData() {
        presenter.getUserData()
        presenter.getUserPost()
        presenter.getUserTrip()
        presenter.getUserFav()
    }

    private fun setupRecyclerView() {
        adapter = EventAdapter(event)
        mountAdapter = MountAdapter(mount)
        rvProfile.setHasFixedSize(true)
        rvProfile.layoutManager = LinearLayoutManager(context)
        rvProfile.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        getData()
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()
        inflater?.inflate(R.menu.menu_profile, menu)
    }

    @SuppressLint("InflateParams")
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.nav_about -> {
                val mDialogView = LayoutInflater.from(activity).inflate(R.layout.layout_about, null)
                AlertDialog.Builder(context!!)
                    .setView(mDialogView)
                    .show()
            }
            R.id.nav_feedback -> {
                val i = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:luthfialfarizi98@gmail.com"))
                i.putExtra(Intent.EXTRA_SUBJECT, "Mountcak Feedback")
                startActivity(i)
            }
            R.id.nav_privacy -> browse("https://mountcak-16aa7.web.app/privacy_policy.html")
            R.id.nav_logout -> presenter.logout()
            R.id.nav_refresh -> getData()
        }
        return super.onOptionsItemSelected(item)
    }
}
