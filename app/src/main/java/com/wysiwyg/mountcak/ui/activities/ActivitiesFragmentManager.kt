package com.wysiwyg.mountcak.ui.activities

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.ui.login.LoginFragment
import com.wysiwyg.mountcak.ui.message.MessageFragment
import com.wysiwyg.mountcak.ui.notification.NotificationFragment
import com.wysiwyg.mountcak.ui.signup.SignUpFragment
import kotlinx.android.synthetic.main.fragment_manager_activities.*

class ActivitiesFragmentManager : Fragment() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_manager_activities, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(false)

        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)

        viewpager.adapter = mSectionsPagerAdapter

        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewpager))
    }

    inner class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            when (position) {
                0 -> return MessageFragment()
                1 -> return NotificationFragment()
            }
            return MessageFragment()
        }

        override fun getCount(): Int {
            return 2
        }
    }
}
