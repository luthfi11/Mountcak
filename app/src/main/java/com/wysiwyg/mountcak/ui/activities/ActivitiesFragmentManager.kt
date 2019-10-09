package com.wysiwyg.mountcak.ui.activities

import android.os.Bundle
import android.view.*
import com.google.android.material.tabs.TabLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wysiwyg.mountcak.R
import com.wysiwyg.mountcak.ui.message.MessageFragment
import com.wysiwyg.mountcak.ui.notification.NotificationFragment
import kotlinx.android.synthetic.main.fragment_manager_activities.*

class ActivitiesFragmentManager : Fragment() {

    private var mSectionsPagerAdapter: SectionsPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_manager_activities, container, false)
        setHasOptionsMenu(false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mSectionsPagerAdapter = SectionsPagerAdapter(childFragmentManager)

        viewpager.adapter = mSectionsPagerAdapter

        viewpager.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(viewpager))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        super.onCreateOptionsMenu(menu, inflater)
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
