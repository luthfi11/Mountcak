package com.wysiwyg.mountcak.ui.main

import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
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
        try {
            if (FirebaseAuth.getInstance().currentUser == null) view.toLogin()
        } catch (ex: Exception) {
            view.toLogin()
            ex.printStackTrace()
        }
    }

    fun changeView(fm: FragmentManager, fragment: Fragment) {
        val transaction = fm.beginTransaction()

        if (!fragment.isAdded) transaction.add(R.id.content, fragment, fragment::class.java.simpleName)
        else transaction.show(fragment)

        val current = fm.primaryNavigationFragment
        if (current != null) transaction.hide(current)

        transaction.apply {
            setPrimaryNavigationFragment(fragment)
            setReorderingAllowed(true)
        }.commitNowAllowingStateLoss()
    }

    private fun removeView(fm: FragmentManager, fragment: Fragment) {
        val transaction = fm.beginTransaction()
        val tag = fm.findFragmentByTag(fragment::class.java.simpleName)
        if (tag != null) {
            transaction.remove(fragment)
            transaction.commitNowAllowingStateLoss()
        }
    }

    fun selectedView(fm: FragmentManager) = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> changeView(fm, home)
            R.id.navigation_explore -> changeView(fm, explore)
            R.id.navigation_rental -> changeView(fm, rental)
            R.id.navigation_activity -> changeView(fm, activities)
            R.id.navigation_profile -> changeView(fm, profile)
        }
        true
    }

    fun reselectedView(fm: FragmentManager) = BottomNavigationView.OnNavigationItemReselectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                removeView(fm, home)
                changeView(fm, home)
            }
            R.id.navigation_explore -> {
                removeView(fm, explore)
                changeView(fm, explore)
            }
            R.id.navigation_rental -> {
                removeView(fm, rental)
                changeView(fm, rental)
            }
            R.id.navigation_activity -> {
                removeView(fm, activities)
                changeView(fm, activities)
            }
            R.id.navigation_profile -> {
                removeView(fm, profile)
                changeView(fm, profile)
            }
        }
    }
}