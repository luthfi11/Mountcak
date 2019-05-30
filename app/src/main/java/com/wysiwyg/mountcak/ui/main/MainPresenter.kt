package com.wysiwyg.mountcak.ui.main

import android.support.design.widget.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.ui.activities.ActivitiesFragmentManager
import com.wysiwyg.mountcak.ui.explore.ExploreFragment
import com.wysiwyg.mountcak.ui.home.HomeFragment
import com.wysiwyg.mountcak.ui.profile.ProfileFragment
import com.wysiwyg.mountcak.ui.rental.RentalFragment

class MainPresenter(private val view: MainView) {

    private val home = HomeFragment()
    private val explore = ExploreFragment()
    private val rental = RentalFragment()
    private val activities = ActivitiesFragmentManager()
    private val profile = ProfileFragment()

    fun checkLogin() {
        if (FirebaseAuth.getInstance().currentUser == null) view.toLogin()
    }

    fun addView() {
        val fragment = arrayOf(home, explore, rental, activities, profile)
        view.addView(fragment)
    }

    fun initialView() {
        view.changeView(home)
    }

    fun selectedView() = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                view.changeView(home)
            }
            R.id.navigation_explore -> {
                view.changeView(explore)
            }
            R.id.navigation_rental -> {
                view.changeView(rental)
            }
            R.id.navigation_activity -> {
                view.changeView(activities)
            }
            R.id.navigation_profile -> {
                view.changeView(profile)
            }
        }
        true
    }
}